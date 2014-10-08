/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine.module;

import com.sirius.plugin.framework.engine.ApplicationEngine;
import com.sirius.plugin.framework.engine.event.PluginInitEvent;
import com.sirius.plugin.framework.engine.event.PluginsInitEvent;
import com.sirius.spring.ApplicationContextLauncher;
import com.sirius.utils.servlet3.Servlet3ContainerDetector;
import com.sirius.utils.servlet3.WebApplicationInitializer;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

/**
 * User: pippo
 * Date: 13-11-27-13:37
 */
@HandlesTypes(Plugin.class)
public class WebPluginDetector extends Servlet3ContainerDetector {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationEngine.class);

	protected CharEncodingFilterConfig encodingFilterConfig;

	@Override
	protected void beforeInit(ServletContext ctx) throws ServletException {
		Validate.notNull(ApplicationContextLauncher.getApplicationContext(),
				"the parent spring application context can not be null!");

		ApplicationContextLauncher.getApplicationContext().getBeanFactory().freezeConfiguration();
		encodingFilterConfig = new CharEncodingFilterConfig(ctx);
	}

	@Override
	protected void init(ServletContext ctx, WebApplicationInitializer initializer) throws ServletException {
		Plugin plugin = (Plugin) initializer;

		logger.info("####try to init plugin:[{}]####", plugin.getName());
		plugin.onStartup(ctx);
		plugin.init();
		encodingFilterConfig.addMapping(plugin.getDispatcher());
		logger.info("####plugin:[{}] init finished####", plugin.getName());

		PluginInitEvent event = new PluginInitEvent(plugin);
		ApplicationContextLauncher.getApplicationContext().publishEvent(event);
		ctx.setAttribute(WebPluginInitedListener.PLUGIN_INIT_EVENT + "." + plugin.getName(), event);
	}

	@Override
	protected void afterInit(ServletContext ctx) throws ServletException {
		PluginsInitEvent event = new PluginsInitEvent(ctx);
		ApplicationContextLauncher.getApplicationContext().publishEvent(event);
		ctx.setAttribute(WebPluginInitedListener.PLUGINS_INIT_EVENT, event);
	}

}
