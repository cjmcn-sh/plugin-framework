/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-5-3 - 下午3:22:59
 */
package com.sirius.utils.thread;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2012-5-3
 * @author pippo
 */
public class ContextSupportJettyQueuedThreadPool extends QueuedThreadPool {

	private static Logger logger = LoggerFactory.getLogger(ContextSupportJettyQueuedThreadPool.class);

	@Override
	protected void runJob(Runnable job) {
		try {
			super.runJob(job);
		} finally {
			ThreadContext.clear();
			logger.trace("clear thread:[{}] context", Thread.currentThread());
		}
	}

}
