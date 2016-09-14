package com.data;

import com.data.queue.NativeQueueClient;
import com.data.queue.QueueListener;
import com.data.queue.QueueListener.TaskHandler;

public class QueueTest {

	private class Test1 implements TaskHandler {

		@Override
		public void proccess(byte[] taskInfo) {
			String str = taskInfo.toString();
			System.out.println(str);
		}

	}

	public static void main(String[] args) {

		System.out.println("begin");
		NativeQueueClient a = new NativeQueueClient("测试");
		for (int i = 0; i < 10; i++) {
			String aa = "测试" + i;
			a.enqueue(aa.getBytes());
		}
		new QueueListener(a, new TaskHandler() {
			@Override
			public void proccess(byte[] taskInfo) {
				String str = taskInfo.toString();
				System.out.println(str);
			}
		}).start();

		System.out.println("end");
	}

}
