package com.sirius.spring;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @since 2013-7-16
 * @author pippo
 */
public class CacheKeyGenerator implements KeyGenerator {

	private static final String key_pattern = "%s@%s(%s)";

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return String.format(key_pattern, target.getClass().getSimpleName(), method.getName(), Arrays.hashCode(params));
	}

}

