package com.mahva.diego.saez.exception;

public class BadArgumentException extends RuntimeException {

	private static final long serialVersionUID = 5605934840515347214L;

	public BadArgumentException(String message) {
		super(message);
	}
}
