package com.data.queue;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NativeQueueClient implements QueueClient {

	private BlockingQueue<Object> queue;

	private static final int DEFAULT_TIMEOUT = 5;
	private static final int DEFAULT_COUNT = 10;

	private String name;

	public NativeQueueClient(String name) {
		this.name = name;
		queue = new ArrayBlockingQueue(DEFAULT_COUNT);
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

	}

	@Override
	public void enqueueBatch(List<byte[]> values) {

	}

	@Override
	public void enqueuehead(byte[] value) {

	}

	@Override
	public List<byte[]> dequeue() {
		return null;
	}

	@Override
	public byte[] dequeueDelay() {
		return null;
	}

	@Override
	public Long llen() {
		return null;
	}

	@Override
	public void delbyvalue(byte[] value) {

	}

	@Override
	public void delqueue() {

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
