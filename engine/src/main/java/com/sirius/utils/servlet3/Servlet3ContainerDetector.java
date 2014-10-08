/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.sirius.utils.servlet3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 定义自己的servlet3容器初始化入口 保证通用的初始化工作可在内部项目初始化之前完成
 * <p/>
 * User: pippo Date: 13-11-21-09:29
 */
public abstract class Servlet3ContainerDetector implements ServletContainerInitializer {

	private static final Logger logger = LoggerFactory.getLogger(Servlet3ContainerDetector.class);

	@Override
	public final void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext ctx) throws ServletException {
		logger.info("detected current run in servlet3 container:[{}]", ctx.getServerInfo());
		Environment.setServletContext(ctx);

		beforeInit(ctx);

		List<WebApplicationInitializer> initializers = new LinkedList<WebApplicationInitializer>();
		if (webAppInitializerClasses != null) {
			for (Class<?> waiClass : webAppInitializerClasses) {
				// Be defensive: Some servlet containers provide us with invalid classes,
				// no matter what @HandlesTypes says...
				if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers())
						&& WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
					try {
						initializers.add((WebApplicationInitializer) waiClass.newInstance());
					} catch (Throwable ex) {
						throw new ServletException("Failed to instantiate CTUWebApplicationInitializer class", ex);
					}
				}
			}
		}

		for (WebApplicationInitializer initializer : initializers) {
			init(ctx, initializer);
		}

		afterInit(ctx);
	}

	protected abstract void beforeInit(ServletContext ctx) throws ServletException;

	protected abstract void afterInit(ServletContext ctx) throws ServletException;

	protected abstract void init(ServletContext ctx, WebApplicationInitializer initializer) throws ServletException;

}
