/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : songsp
 * date created : 2012-6-20 - 上午10:15:48
 */
package com.sirius.utils;

import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2012-6-20
 * @author songsp
 */
public class OgnlWrapper {

	private static Logger logger = LoggerFactory.getLogger(OgnlWrapper.class);

	private static ObjectMapper om = new ObjectMapper();
	private Map<String, Object> payload;

	public OgnlWrapper(Map<String, Object> playload) {
		Validate.notEmpty(playload, "can not construct with none playload!");
		this.payload = playload;
	}

	@SuppressWarnings("unchecked")
	public OgnlWrapper(Object playload) {
		this.payload = om.convertValue(playload, Map.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String expression) {
		try {
			return (T) Ognl.getValue(expression, this.payload);
		} catch (OgnlException e) {
			logger.trace(String.format("get value with expression:[%s] due to error, return null instead of",
				expression), e);
			return null;
		}
	}

	public Long getLong(String expression) {
		try {
			Object obj = Ognl.getValue(expression, this.payload);
			if (null == obj)
				return null;
			try {
				return Long.parseLong(obj.toString());
			} catch (NumberFormatException nfe) {
				logger.warn(String.format("get value with expression:[%s] due to error, return null. value[%s] cannot be cast to java.lang.Long",
					expression,
					obj.toString()));
				return null;
			}
		} catch (OgnlException e) {
			logger.trace(String.format("get value with expression:[%s] due to error, return null instead of",
				expression), e);
			return null;
		}
	}

	public Integer getInt(String expression) {
		try {
			Object obj = Ognl.getValue(expression, this.payload);
			if (null == obj)
				return null;
			try {
				return Integer.parseInt(obj.toString());
			} catch (NumberFormatException nfe) {
				logger.warn(String.format("get value with expression:[%s] due to error, return null. value[%s] cannot be cast to java.lang.Integer",
					expression,
					obj.toString()));
				return null;
			}
		} catch (OgnlException e) {
			logger.trace(String.format("get value with expression:[%s] due to error, return null instead of",
				expression), e);
			return null;
		}
	}

	@Override
	public String toString() {
		return String.format("OgnlWrapper[%s]", this.payload.toString());
		/*
		 * return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false)
		 * .concat(String.format("@hashCode=%s", this.hashCode()));
		 */
	}

}
