package com.mahva.diego.saez.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 25183696794019528L;

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(String message) {
		super(message);
	}

}
