/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-8-9 - 下午2:35:27
 */
package com.sirius.utils.thread;

import java.util.Map;

/**
 * @since 2012-8-9
 * @author pippo
 */
public class CustomizableThread extends Thread {

	public CustomizableThread() {

	}

	public CustomizableThread(Runnable target, String name) {
		super(target, name);

	}

	public CustomizableThread(Runnable target) {
		super(target);
	}

	public CustomizableThread(String name) {
		super(name);
	}

	public CustomizableThread(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
	}

	public CustomizableThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
	}

	public CustomizableThread(ThreadGroup group, Runnable target) {
		super(group, target);
	}

	public CustomizableThread(ThreadGroup group, String name) {
		super(group, name);
	}

	public void setThreadContext(Map<String, Object> _context) {
		ThreadContext.setContext(_context);
	}

}
