package com.data.queue;

import java.util.List;

public class NativeQueueClient implements QueueClient {

	@Override
	public QueueClient getQueueDelayClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueClient getQueueNormalClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enqueue(byte[] value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enqueueBatch(List<byte[]> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enqueuehead(byte[] value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<byte[]> dequeue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] dequeueDelay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long llen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delbyvalue(byte[] value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delqueue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] lpop() {
		// TODO Auto-generated method stub
		return null;
	}

}
