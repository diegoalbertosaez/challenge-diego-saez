package com.mahva.diego.saez.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mahva.diego.saez.exception.BadArgumentException;
import com.mahva.diego.saez.exception.BusinessException;
import com.mahva.diego.saez.exception.NotFoundException;
import com.mahva.diego.saez.util.LoggerHelper;

/**
 * Exception handler for controller.
 * 
 * @author diegosaez
 *
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {

	private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleException(NotFoundException ex) {
		LoggerHelper.error(logger, ex.getMessage());
		return ex.getMessage();
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(BusinessException ex) {
		LoggerHelper.error(logger, ex.getMessage());
		return ex.getMessage();
	}

	@ExceptionHandler(BadArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleException(BadArgumentException ex) {
		LoggerHelper.error(logger, ex.getMessage());
		return ex.getMessage();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception ex) {
		LoggerHelper.error(logger, ex.getMessage());
		return ex.getMessage();
	}

}
