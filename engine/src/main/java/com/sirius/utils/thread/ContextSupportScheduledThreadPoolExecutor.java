/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-5-3 - 下午3:14:44
 */
package com.sirius.utils.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2012-5-3
 * @author pippo
 */
public class ContextSupportScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

	private static Logger logger = LoggerFactory.getLogger(ContextSupportScheduledThreadPoolExecutor.class);

	public ContextSupportScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
		super(corePoolSize, handler);
	}

	public ContextSupportScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory,
			RejectedExecutionHandler handler) {
		super(corePoolSize, threadFactory, handler);
	}

	public ContextSupportScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
		super(corePoolSize, threadFactory);
	}

	public ContextSupportScheduledThreadPoolExecutor(int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		ThreadContext.clear();
		logger.trace("clear thread context");
	}

}
