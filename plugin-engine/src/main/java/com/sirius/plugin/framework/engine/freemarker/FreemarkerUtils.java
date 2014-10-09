/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : lms-job
 * user created : yangy
 * date created : 2013-7-11 - 上午9:56:12
 */
package com.sirius.plugin.framework.engine.freemarker;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @since 2013-7-11
 * @author songsp
 */
public class FreemarkerUtils {

	private static final Logger logger = LoggerFactory.getLogger(FreemarkerUtils.class);

	private static Configuration configuration;

	public void setConfiguration(Configuration configuration) {
		FreemarkerUtils.configuration = configuration;
	}

	//	public static void init() {
	//		if (configuration != null) {
	//			return;
	//		}
	//
	//		configuration = BeanLocator.getBean(Configuration.class);
	//	}

	public static String toString(String template_name, Map<String, Object> context) {
		//		init();

		try {
			Template template = configuration.getTemplate(template_name);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
		} catch (Exception e) {
			logger.error("process template:[{}] with context:[{}] due to error:[{}]",
				template_name,
				context,
				ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

}
