/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2011-11-21 - 上午10:55:52
 */
package com.sirius.utils;

import java.util.Map;

/**
 * @since 2011-11-21
 * @author pippo
 */
public class MapUtils extends org.apache.commons.collections.MapUtils {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map addIfNotNull(Map target, Object key, Object value) {
		if (value != null) {
			target.put(key, value);
		}
		return target;
	}

}
