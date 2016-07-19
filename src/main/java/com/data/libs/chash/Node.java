package com.data.libs.chash;

public interface Node<I, T> {
	public I getId();

	public T getObject();

	public Object attachment();

	public Node<I, T> attach(Object obj);
}
