/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine.module;

import com.sirius.utils.servlet3.WebApplicationInitializer;
import com.sirius.spring.ApplicationContextLauncher;
import com.sirius.spring.DispatcherServletRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * User: pippo
 * Date: 13-12-13-14:07
 */
public abstract class Plugin extends XmlWebApplicationContext implements Module, WebApplicationInitializer {

	public static final String PLUGIN_WEB_PREFIX = "";

	public Plugin() {

	}

	@Override
	public Type getType() {
		return Type.plugin;
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		ConfigurableApplicationContext parent = ApplicationContextLauncher.getApplicationContext();
		setParent(parent);
		setClassLoader(parent.getClassLoader());
		setConfigLocation(String.format("classpath*:META-INF/%s/dispatcher.context.xml", getName()));
		setServletContext(servletContext);

		//addBeanFactoryPostProcessor(new ServiceBeanPostprocessor());

		refresh();
		//getBeanFactory().freezeConfiguration();
	}

	@Override
	public void init() {
		String path = PLUGIN_WEB_PREFIX + "/" + getName() + "/*";
		dispatcher = DispatcherServletRegistry.regist(getName(), path, this);
	}

	protected ServletRegistration dispatcher;

	public ServletRegistration getDispatcher() {
		return dispatcher;
	}
}
