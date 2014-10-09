/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2011-12-12 - 下午4:27:20
 */
package com.sirius.utils;

import java.util.Map;

/**
 * @since 2011-12-12
 * @author pippo
 */
public class MapBuilder<Key, Value> {

	/**
	 * @param inner
	 */
	public MapBuilder(Map<Key, Value> inner) {
		this.inner = inner;
	}

	private Map<Key, Value> inner;

	public Map<Key, Value> put(Key key, Value value) {
		this.inner.put(key, value);
		return inner;
	}

	public Map<Key, Value> remove(Key key) {
		this.inner.remove(key);
		return inner;
	}

	public Map<Key, Value> build() {
		return this.inner;
	}

}
