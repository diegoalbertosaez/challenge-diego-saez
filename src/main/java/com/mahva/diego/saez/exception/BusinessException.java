package com.mahva.diego.saez.exception;

/**
 * Exception for business layer
 * 
 * @author diegosaez
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -1935418028895041646L;

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

}
