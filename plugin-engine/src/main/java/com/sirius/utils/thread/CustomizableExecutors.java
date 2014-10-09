/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-5-3 - 下午3:08:05
 */
package com.sirius.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @since 2012-5-3
 * @author pippo
 */
public final class CustomizableExecutors {

	public static ExecutorService newFixedThreadPool(int nThreads, String groupName) {
		return new ContextSupportThreadPoolExecutor(nThreads,
			nThreads,
			0L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(),
			new CustomizableThreadFactory(groupName));
	}

	public static ExecutorService newFixedThreadPool(int nThreads, String groupName, boolean useDaemon) {
		return new ContextSupportThreadPoolExecutor(nThreads,
			nThreads,
			0L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(),
			new CustomizableThreadFactory(groupName, useDaemon));
	}

	public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, String groupName) {
		return new ContextSupportScheduledThreadPoolExecutor(corePoolSize, new CustomizableThreadFactory(groupName));
	}

	public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, String groupName, boolean useDaemon) {
		return new ContextSupportScheduledThreadPoolExecutor(corePoolSize, new CustomizableThreadFactory(groupName,
			useDaemon));
	}

	public static ExecutorService newCachedThreadPool(String groupName) {
		return new ContextSupportThreadPoolExecutor(0,
			Integer.MAX_VALUE,
			60L,
			TimeUnit.SECONDS,
			new SynchronousQueue<Runnable>(),
			new CustomizableThreadFactory(groupName));
	}

	public static ExecutorService newCachedThreadPool(String groupName, boolean useDaemon) {
		return new ContextSupportThreadPoolExecutor(0,
			Integer.MAX_VALUE,
			60L,
			TimeUnit.SECONDS,
			new SynchronousQueue<Runnable>(),
			new CustomizableThreadFactory(groupName, useDaemon));
	}
}
