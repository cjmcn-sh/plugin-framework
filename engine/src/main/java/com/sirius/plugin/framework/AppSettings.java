/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : cas-app1
 * user created : pippo
 * date created : 2010-10-21 - 下午01:32:20
 */
package com.sirius.plugin.framework;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @since 2010-10-21
 * @author pippo
 */
public class AppSettings {

	private static Logger logger = LoggerFactory.getLogger(AppSettings.class);

	private static AppSettings instance = null;

	private Properties properties = new Properties();

	public static void init(String... paths) {
		if (instance == null) {
			instance = new AppSettings();
		}

		for (String path : paths) {
			if (StringUtils.isBlank(path)) {
				continue;
			}

			logger.info("try to load resource:[{}]", path);

			try {
				Properties _properties = new Properties();
				File file = new File(path);

				if (file.exists()) {
					_properties.load(new FileInputStream(path));
				} else {
					_properties.load(AppSettings.class.getResourceAsStream(path));
				}

				for (Object key : _properties.keySet()) {
					if (instance.properties.containsKey(key)) {
						continue;
					}

					logger.debug("load setting {}={}", key, _properties.get(key));
					instance.properties.put(key, _properties.get(key));
				}

			} catch (Exception e) {
				throw new RuntimeException("init settings due to error", e);
			}
		}

	}

	public static AppSettings getInstance() {
		if (instance == null) {
			instance = new AppSettings();
		}
		return instance;
	}

	public String get(String name) {
		return properties.getProperty(name);
	}

	public Integer getInt(String name) {
		return NumberUtils.toInt(this.get(name), -1);
	}

	public Long getLong(String name) {
		return NumberUtils.toLong(this.get(name), -1);
	}

	public Boolean getBoolean(String name) {
		return BooleanUtils.toBoolean(this.get(name));
	}

	public String[] getByPrefix(String prefix) {
		Set<String> values = Sets.newHashSet();
		String value = this.properties.getProperty(prefix);
		if (StringUtils.isNotBlank(value)) {
			values.add(value);
		}

		Set<Object> keys = properties.keySet();
		for (Object key : keys) {
			if (key.toString().startsWith(prefix)) {
				value = this.properties.getProperty(key.toString());
				if (StringUtils.isNotBlank(value)) {
					values.add(value);
				}
			}
		}
		return values.toArray(new String[0]);
	}

	public Map<String, String> getAll() {
		Map<String, String> settings = Maps.newHashMap();
		for (Object key : this.properties.keySet()) {
			settings.put(key.toString(), properties.getProperty((String) key));
		}
		return settings;
	}

}
