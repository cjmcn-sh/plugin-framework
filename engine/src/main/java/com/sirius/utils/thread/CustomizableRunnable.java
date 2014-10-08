/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-8-9 - 下午2:47:29
 */
package com.sirius.utils.thread;

import java.util.Map;

/**
 * @since 2012-8-9
 * @author pippo
 */
public abstract class CustomizableRunnable implements Runnable {

	private Map<String, Object> _context;

	public CustomizableRunnable() {
		_context = ThreadContext.getContext();
	}

	@Override
	public final void run() {
		ThreadContext.setContext(_context);
		doRun();
	}

	protected abstract void doRun();

}
