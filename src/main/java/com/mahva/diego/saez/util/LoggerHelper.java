package com.mahva.diego.saez.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * Helper to encapsulate logging logic.
 * 
 * @author diegosaez
 *
 */
public class LoggerHelper {

	private LoggerHelper() {

	}

	public static void debug(Logger logger, String message) {
		if (logger != null && logger.isDebugEnabled() && !StringUtils.isEmpty(message)) {
			logger.debug(message);
		}
	}

	public static void error(Logger logger, String message) {
		if (logger != null && !StringUtils.isEmpty(message)) {
			logger.error(message);
		}
	}

	public static void error(Logger logger, String message, Throwable throwable) {
		if (logger != null && !StringUtils.isEmpty(message)) {
			logger.error(message, throwable);
		}
	}

}
