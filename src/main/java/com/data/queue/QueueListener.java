package com.data.queue;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueListener implements Runnable {
	public static interface TaskHandler {
		void proccess(byte[] taskInfo);
	}

	public static class QueueDelayListener implements Runnable {
		private static final int DELAY_THRESHOLD_DEFUALT = 1000;
		private QueueClient queueClient;
		private TaskHandler handler;

		public QueueDelayListener(QueueClient client, TaskHandler handler) {
			this.queueClient = client;
			this.handler = handler;
		}

		@Override
		public void run() {
			while (true) {
				try {
					byte[] taskInfo = queueClient.dequeueDelay();
					if (taskInfo != null) {
						try {
							handler.proccess(taskInfo);
						} catch (Exception e) {
							LOGGER.error(e.toString(), e);
						}
					} else {
						QueueClient client = queueClient.getQueueNormalClient();
						long len = client.llen();
						if (len > DELAY_THRESHOLD_DEFUALT) {
							client.delay();
						} else {
							Thread.sleep(SLEEP_MILLIS);
						}
					}
				} catch (Exception e) {
					try {
						Thread.sleep(SLEEP_MILLIS);
					} catch (Exception ex) {
						LOGGER.error(ex.getMessage(), ex);
					}
					LOGGER.error(e.getMessage(), e);
				}
			}
		}

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueListener.class);

	private QueueClient queueClient;
	private TaskHandler handler;
	private int threads = DEFAULT_THREAD_COUNT;
	private boolean delayThread = false;
	private static final int SLEEP_MILLIS = 1000;
	private static final int DEFAULT_THREAD_COUNT = 1;

	public QueueListener(QueueClient client, TaskHandler handler) {
		this.queueClient = client;
		this.handler = handler;
	}

	public QueueListener(QueueClient client, TaskHandler handler, int taskThreads) {
		this.queueClient = client;
		this.handler = handler;
		this.threads = taskThreads;
	}

	public QueueListener(QueueClient client, TaskHandler handler, int taskThreads, boolean delayThread) {
		this.queueClient = client;
		this.handler = handler;
		this.threads = taskThreads;
		this.delayThread = delayThread;
	}

	public void start(boolean hang) {
		// 主线程也包含在内了，兼容已经引用此处代码的逻辑
		int end = hang ? threads - 1 : threads;
		for (int i = 0; i < end; i++) {
			Thread thread = new Thread(new QueueListener(this.queueClient, this.handler, this.threads));
			thread.setDaemon(true);
			thread.start();
		}
		if (hang)
			this.run();
	}

	public void start() {
		// 主线程也包含在内了，兼容已经引用此处代码的逻辑
		for (int i = 0; i < threads - 1; i++) {
			Thread thread = new Thread(new QueueListener(this.queueClient, this.handler, this.threads));
			thread.setDaemon(true);
			thread.start();
		}
		if (delayThread) {
			Thread thread = new Thread(new QueueDelayListener(this.queueClient.getQueueDelayClient(), this.handler));
			thread.setDaemon(true);
			thread.start();
		}
		this.run();

	}

	@Override
	public void run() {
		while (true) {
			try {
				List<byte[]> taskInfo = queueClient.dequeue();
				if (taskInfo != null && taskInfo.size() >= 2) {
					try {
						this.handler.proccess(taskInfo.get(1));
					} catch (Exception e) {
						LOGGER.error(e.toString(), e);
					}
				} else {
					Thread.sleep(SLEEP_MILLIS);
				}
			} catch (Exception e) {
				try {
					Thread.sleep(SLEEP_MILLIS);
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

}
