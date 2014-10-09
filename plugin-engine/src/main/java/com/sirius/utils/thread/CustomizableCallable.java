/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-8-9 - 下午2:47:29
 */
package com.sirius.utils.thread;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @since 2012-8-9
 * @author pippo
 */
public abstract class CustomizableCallable<V> implements Callable<V> {

	private Map<String, Object> _context;

	public CustomizableCallable() {
		_context = ThreadContext.getContext();
	}

	@Override
	public final V call() throws Exception {
		ThreadContext.setContext(_context);
		return doCall();
	}

	protected abstract V doCall() throws Exception;

}
