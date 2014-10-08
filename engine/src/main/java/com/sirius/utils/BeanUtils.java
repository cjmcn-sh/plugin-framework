/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

	@SuppressWarnings("unchecked")
	public static <T> T cloneQuiet(T bean) {
		try {
			return (T) cloneBean(bean);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getNestedPropertyValue(Object object, String propertyName) {
		try {
			return (T) PropertyUtils.getNestedProperty(object, propertyName);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 取出bean中属性的值,属性路径可以为"bean.bean1.name"的形式,如果取不到所给路径的值那么返回null
	 * 
	 * @param path
	 * @param bean
	 * @return propertyValue
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getNestedDeclaredFieldValue(Object bean, String path) {
		Validate.notNull(bean);
		Validate.notNull(path);

		if (StringUtils.containsNone(path, ".")) {
			try {
				return (T) getDeclaredFieldValue(bean, path);
			} catch (NoSuchFieldException e) {
				logger.warn("can not fine property:" + path, e);
				return null;
			}
		}

		String[] propertyNames = path.split("\\.");

		Object tempValue = null;
		try {
			tempValue = getDeclaredFieldValue(bean, propertyNames[0]);
		} catch (NoSuchFieldException e) {
			logger.warn("can not fine property:" + propertyNames[0], e);
			return null;
		}
		if (tempValue == null) {
			return null;
		}
		return (T) getNestedDeclaredFieldValue(tempValue, StringUtils.join(propertyNames, ".", 1, propertyNames.length));
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDeclaredFieldValue(Object object, String propertyName) throws NoSuchFieldException {
		Validate.notNull(object);
		Validate.notNull(propertyName);

		Field field = getDeclaredField(object.getClass(), propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			throw new NoSuchFieldException("No such field: " + object.getClass() + '.' + propertyName);
		} finally {
			field.setAccessible(accessible);
		}
		return (T) result;
	}

	/**
	 * 暴力设置对象变量值,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static void setDeclaredFieldValue(Object target, String propertyName, Object newValue)
			throws NoSuchFieldException {
		Validate.notNull(target);
		Validate.notEmpty(propertyName);

		Field field = getDeclaredField(target.getClass(), propertyName);
		setFieldValue(target, field, newValue);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) {
		Validate.notNull(clazz);
		LinkedHashSet<Field> fields = new LinkedHashSet<Field>();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
		}
		return fields.toArray(new Field[0]);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	public static Map<String, Field> getDeclaredFieldMap(Class<?> clazz) {
		Field[] fields = getDeclaredFields(clazz);
		Map<String, Field> map = new HashMap<String, Field>();
		for (Field field : fields) {
			if (map.containsKey(field.getName())) {
				continue;
			}
			map.put(field.getName(), field);
		}
		return map;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * 
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
		Validate.notNull(clazz);
		Validate.notEmpty(propertyName);
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	public static Field getNestDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
		Validate.notNull(clazz);
		Validate.notEmpty(propertyName);

		if (StringUtils.containsNone(propertyName, ".")) {
			return getDeclaredField(clazz, propertyName);
		}

		String[] propertyNames = propertyName.split("\\.");
		Field field = getDeclaredField(clazz, propertyNames[0]);
		return getNestDeclaredField(field.getType(), StringUtils.join(propertyNames, ".", 1, propertyNames.length));
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchMethodException 如果没有该Method时抛出.
	 */
	public static Object invokePrivateMethod(Object object, String methodName, Object... params)
			throws NoSuchMethodException {
		Validate.notNull(object);
		Validate.notEmpty(methodName);
		Class<?>[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class<?> clazz = object.getClass();
		Method method = null;
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类定义,继续向上转型
			}
		}

		if (method == null) {
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + "@" + methodName);
		}

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {

		} catch (Exception e) {
			throw new NoSuchMethodException(e.getMessage());
		} finally {
			method.setAccessible(accessible);
		}
		return result;
	}

	/**
	 * 深拷贝可序列化对象
	 */
	public static <T extends Serializable> T deepCloneBean(T bean) {
		return (T) SerializationUtils.clone(bean);
	}

	/**
	 * Copy the property values of the given source bean into the given target bean.
	 * <p>
	 * Note: The source and target classes do not have to match or even be derived from each other, as long as the
	 * properties match. Any bean properties that the source bean exposes but the target bean does not will silently be
	 * ignored.
	 * 
	 * @param source
	 * @param target
	 * @param includeProperties
	 * @param ignoreProperties
	 */
	public static void copyProperties(Object source,
			Object target,
			String[] includeProperties,
			String[] ignoreProperties) {
		Validate.notNull(source);
		Validate.notNull(target);

		Map<String, Field> sourceFields = getDeclaredFieldMap(source.getClass());
		Map<String, Field> targetFields = getDeclaredFieldMap(target.getClass());

		Set<String> includePropertySet = new HashSet<String>();

		/* 要复制的属性 */
		if (includeProperties == null) {
			includePropertySet = targetFields.keySet();
		} else {
			for (String includeProperty : includeProperties) {
				includePropertySet.add(includeProperty);
			}
		}
		/* 忽略的属性 */
		if (ignoreProperties != null && ignoreProperties.length > 0) {
			for (String ignoreProperty : ignoreProperties) {
				includePropertySet.remove(ignoreProperty);
			}
		}

		for (String propertyName : includePropertySet) {
			if (sourceFields.containsKey(propertyName) && targetFields.containsKey(propertyName)) {
				try {
					setFieldValue(target,
						targetFields.get(propertyName),
						getFieldValue(source, sourceFields.get(propertyName)));
				} catch (NoSuchFieldException e) {
					continue;
				}
			}
		}
	}

	public static void setFieldValue(Object target, Field field, Object newValue) throws NoSuchFieldException {
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(target, newValue);
		} catch (IllegalAccessException e) {
			throw new NoSuchFieldException("No such field: " + target.getClass() + '.' + field.getName());
		} finally {
			field.setAccessible(accessible);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object target, Field field) throws NoSuchFieldException {
		T returnVal = null;
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			returnVal = (T) field.get(target);
		} catch (IllegalAccessException e) {
			throw new NoSuchFieldException("No such field: " + target.getClass() + '.' + field.getName());
		} finally {
			field.setAccessible(accessible);
		}
		return returnVal;
	}
}
