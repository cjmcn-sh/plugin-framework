/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-5-3 - 下午3:04:49
 */
package com.sirius.utils.thread;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2012-5-3
 * @author pippo
 */
public class UncaughtExceptionLoggerHandler implements UncaughtExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(UncaughtExceptionLoggerHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.error(String.format("the thread:[%s] execute due to error", t), e);
		logger.debug("clear thread context");
		ThreadContext.clear();
	}

}
