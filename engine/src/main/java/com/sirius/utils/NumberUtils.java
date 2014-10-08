/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-8-5 - 下午7:52:27
 */
package com.sirius.utils;

/**
 * @since 2012-8-5
 * @author pippo
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

	public static Long createLong(Number number) {
		if (number == null) {
			return null;
		}
		return number.longValue();
	}

}
