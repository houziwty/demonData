package com.data.libs.chash;


public class NodeImpl<I,T> implements Node<I, T> {
	
	private I id;
	private T obj;
	private Object attachment;
	
	public NodeImpl(I id,T obj){
		this.id=id;
		this.obj=obj;
	}

    @Override
    public I getId() {
        return id;
    }

    @Override
    public T getObject() {
        return obj;
    }

	@Override
	public Object attachment() {
		return attachment;
	}

	@Override
	public Node<I,T> attach(Object obj) {
		attachment = obj;
		return this;
	}

}
