package com.data.queue;

import com.feinno.app.common.queue.QueueClient;
import com.feinno.app.common.queue.QueueListener.TaskHandler;

public class QueueListener implements Runnable {
public	static interface TaskHandler {
		void proccess(byte[] taskInfo);
	}
	
	public static class QueueDelayListener implements Runnable{
		private static final int DELAY_THRESHOLD_DEFUALT = 1000;
		private QueueClient queueClient;
		private TaskHandler handler;
		
		public QueueDelayListener(QueueClient client, TaskHandler handler){
			this.queueClient = client;
			this.handler = handler;
		}
		@Override
		public void run() {
			
		}
		
	}

	
	private QueueClient queueClient;
	private TaskHandler handler;
	private int threads = DEFAULT_THREAD_COUNT;
	private boolean delayThread = false;
	private static final int SLEEP_MILLIS = 1000;
	private static final int DEFAULT_THREAD_COUNT = 1;
	
	public QueueListener(QueueClient client,TaskHandler handler){
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
	@Override
	public void run() {

	}

}
