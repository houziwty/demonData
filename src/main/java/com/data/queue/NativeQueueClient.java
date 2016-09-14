package com.data.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.data.queue.QueueListener.TaskHandler;

public class NativeQueueClient implements QueueClient {

	private BlockingQueue<Object> queue;

	private static final int DEFAULT_COUNT = 10;

	private String name;

	public NativeQueueClient(String name) {
		this.name = name;
		queue = new ArrayBlockingQueue<Object>(DEFAULT_COUNT);
	}

	@Override
	public QueueClient getQueueDelayClient() {
		return null;
	}

	@Override
	public QueueClient getQueueNormalClient() {
		return null;
	}

	@Override
	public void enqueue(byte[] value) {
		try {
			queue.put(value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void enqueueBatch(List<byte[]> values) {
	}

	@Override
	public void enqueuehead(byte[] value) {

	}

	@Override
	public List<byte[]> dequeue() {
		List<byte[]> list = null;
		try {
			Object result = queue.take();
			list = new ArrayList<>();

			list.add((byte[]) result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public byte[] dequeueDelay() {
		return null;
	}

	@Override
	public Long llen() {
		return (long) queue.size();
	}

	@Override
	public void delbyvalue(byte[] value) {

	}

	@Override
	public void delqueue() {
		queue.clear();
	}

	@Override
	public void delay() {

	}

	@Override
	public void quit() {

	}

	@Override
	public byte[] lpop() {
		return null;
	}

}
