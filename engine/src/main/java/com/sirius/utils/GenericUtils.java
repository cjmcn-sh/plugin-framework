/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generics的util类.
 * 
 * @author pippo
 */
@SuppressWarnings("rawtypes")
public final class GenericUtils {

	private static final Logger logger = LoggerFactory.getLogger(GenericUtils.class);

	private GenericUtils() {
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 * 
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 * 
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or <code>Object.class</code> if cannot be determined
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (genType == Object.class) {
			logger.warn(clazz.getSimpleName() + "'s supperclass is Object.class not ParameterizedType");
			return Object.class;
		}

		if (genType instanceof Class) {
			logger.warn(clazz.getSimpleName() + "'s superclass is " + genType + " not ParameterizedType, find "
					+ genType + "'s supperclass generic type");
			return getSuperClassGenricType((Class) genType, index);
		}

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		if (logger.isDebugEnabled())
			logger.debug(clazz.getSimpleName() + "'s superclass is:" + genType);

		ParameterizedType genSupperType = (ParameterizedType) genType;
		return getParameterizedType(clazz, index, genSupperType);
	}

	/**
	 * @param clazz
	 * @param index
	 * @param genSupperType
	 * @return
	 */
	private static Class getParameterizedType(Class clazz, int index, ParameterizedType genSupperType) {
		Type[] params = genSupperType.getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}

	public static Class getInterfaceGenricType(Class clazz, Class interface_type) {
		return getInterfaceGenricType(clazz, interface_type, 0);
	}

	public static Class getInterfaceGenricType(Class clazz, Class interface_type, int index) {
		Type[] types = clazz.getGenericInterfaces();
		for (Type genType : types) {

			if (genType == Object.class) {
				logger.warn(clazz.getSimpleName() + "'s supperclass is Object.class not ParameterizedType");
				continue;
			}

			if (genType instanceof Class) {
				logger.warn(clazz.getSimpleName() + "'s superclass is " + genType + " not ParameterizedType, find "
						+ genType + "'s supperclass generic type");
				return getInterfaceGenricType((Class) genType, interface_type, index);
			}

			if (!(genType instanceof ParameterizedType)) {
				logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
				continue;
			}

			if (logger.isDebugEnabled())
				logger.debug(clazz.getSimpleName() + "'s interface is:" + genType);

			ParameterizedType genInterfaceType = (ParameterizedType) genType;
			if (!genInterfaceType.getRawType().equals(interface_type)) {
				continue;
			}

			return getParameterizedType(clazz, index, genInterfaceType);
		}

		logger.warn(clazz.getSimpleName() + "'s supperclass is Object.class not ParameterizedType");
		return Object.class;
	}
}
