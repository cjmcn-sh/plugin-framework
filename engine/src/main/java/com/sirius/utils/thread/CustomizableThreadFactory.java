/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
  * project : myctu-utils
 * user created : pippo
 * date created : 2011-12-1 - 下午8:00:30
 */
package com.sirius.utils.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @since 2011-12-1
 * @author pippo
 */
public class CustomizableThreadFactory implements ThreadFactory {

	public CustomizableThreadFactory(String groupName) {
		this.group = new ThreadGroup(groupName);
		this.threadNamePrefix = String.format("t_%s", groupName);
	}

	public CustomizableThreadFactory(String groupName, boolean useDaemon) {
		this(groupName);
		this.daemon = useDaemon;
	}

	public CustomizableThreadFactory useDaemon() {
		this.daemon = true;
		return this;
	}

	private String threadNamePrefix;

	private ThreadGroup group;

	private boolean daemon = false;

	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new CustomizableThread(this.group, r, nextThreadName());
		thread.setDaemon(daemon);
		thread.setUncaughtExceptionHandler(uncaughtExceptionLoggerHandler);
		return thread;
	}

	private String nextThreadName() {
		return String.format("%s_%s", this.threadNamePrefix, this.count.addAndGet(1));
	}

	private UncaughtExceptionLoggerHandler uncaughtExceptionLoggerHandler = new UncaughtExceptionLoggerHandler();

}
