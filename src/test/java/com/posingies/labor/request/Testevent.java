package com.posingies.labor.request;

import java.io.Serializable;

public class Testevent<T> {

	private T sender;

	private Serializable payload;

	public Testevent(final T sender, final Serializable payload) {
		this.sender = sender;
		this.payload = payload;
	}

	public T getSender() {
		return sender;
	}

	public void setSender(final T sender) {
		this.sender = sender;
	}

	public Serializable getPayload() {
		return payload;
	}

	public void setPayload(final Serializable payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Testevent [sender=");
		builder.append(sender);
		builder.append(", payload=");
		builder.append(payload);
		builder.append("]");
		return builder.toString();
	}

}
