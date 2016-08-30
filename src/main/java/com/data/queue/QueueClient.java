package com.data.queue;

import java.util.List;

public interface QueueClient {
	QueueClient getQueueDelayClient();

	QueueClient getQueueNormalClient();

	void enqueue(byte[] value);

	void enqueueBatch(final List<byte[]> values);

	void enqueuehead(byte[] value);// 从头部添加

	List<byte[]> dequeue();

	byte[] dequeueDelay();

	Long llen();

	void delbyvalue(byte[] value);

	void delqueue();

	void delay();

	void quit();

	byte[] lpop();
}
