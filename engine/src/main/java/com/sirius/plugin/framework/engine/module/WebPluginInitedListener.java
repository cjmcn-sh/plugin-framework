/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine.module;

import com.sirius.plugin.framework.engine.event.PluginInitEvent;
import com.sirius.plugin.framework.engine.event.PluginsInitEvent;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/**
 * User: pippo
 * Date: 14-3-6-22:03
 */
public abstract class WebPluginInitedListener implements ServletContextAttributeListener {

	public static final String PLUGIN_INIT_EVENT = "ctu.plugin.init";
	public static final String PLUGINS_INIT_EVENT = "ctu.plugins.init";

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		if (event.getName().startsWith(PLUGIN_INIT_EVENT)) {
			PluginInitEvent _event = (PluginInitEvent) event.getValue();
			onPluginInit(event.getServletContext(), (Plugin) _event.getSource());
			return;
		}

		if (event.getName().equals(PLUGINS_INIT_EVENT)) {
			PluginsInitEvent _event = (PluginsInitEvent) event.getValue();
			onPluginsInit((ServletContext) _event.getSource());
			return;
		}
	}

	protected void onPluginInit(ServletContext servletContext, Plugin plugin) {

	}

	protected void onPluginsInit(ServletContext ctx) {

	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {

	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {

	}
}
