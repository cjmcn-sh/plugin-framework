/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine;

import com.sirius.plugin.framework.engine.event.PluginInitEvent;
import com.sirius.plugin.framework.engine.module.Component;
import com.sirius.plugin.framework.engine.module.Plugin;
import com.sirius.spring.ApplicationContextLauncher;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.SiriusApplicationContext;

import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * User: pippo
 * Date: 13-12-12-13:06
 */
public class PluginEngine implements Constants {

	private static final Logger logger = LoggerFactory.getLogger(PluginEngine.class);
	public static final Set<Component> COMPONENTS = new LinkedHashSet<>();
	public static final Map<String, Plugin> PLUGINS = new LinkedHashMap<>();

	static String _DEFAULT_SETTINGS = DEFAULT_SETTINGS;

	static {
		// Jersey uses java.util.logging - bridge to slf4
		java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		SLF4JBridgeHandler.install();
	}

	public static void main(String[] args) throws Exception {
		// load settings
		AppSettings.init(PluginEngine.class.getResource(getSettingsPath(args)).getFile());

		// start server
		PluginEngine engine = new PluginEngine();
		engine.start();
		Thread.currentThread().join();
	}

	protected static String getSettingsPath(String[] args) {
		String settings = args != null && args.length > 1 && StringUtils.isNotBlank(args[1])
				? args[1]
				: _DEFAULT_SETTINGS;

		if (!settings.startsWith("/")) {
			settings = "/" + settings;
		}
		return settings;
	}

	public static Plugin getPlugin(String pluginName) {
		return PLUGINS.get(pluginName);
	}

	public static <T> T getPluginBean(String pluginName, Class<T> clazz) {
		Plugin plugin = getPlugin(pluginName);

		if (plugin == null) {
			return null;
		}

		return plugin.getBean(clazz);
	}

	public void start() throws PluginEngineException {
		ApplicationContextLauncher.setApplicationContext(createApplicationContext());
		initComponents();
		initPlugins();
		ApplicationContextLauncher.start();
	}

	public void stop() {
		ApplicationContextLauncher.stop();
	}

	protected SiriusApplicationContext createApplicationContext() {
		SiriusApplicationContext context = null;
		if (PluginEngine.class.getResource("/ctu.application.main.context.xml") == null) {
			logger.info(
					"can not find context:[/ctu.application.main.context.xml], use:[/META-INF/ctu.application.main.context.xml]");
			context = new SiriusApplicationContext("/META-INF/ctu.application.main.context.xml");
		} else {
			context = new SiriusApplicationContext("/ctu.application.main.context.xml");
		}

		//context.addBeanFactoryPostProcessor(new ServiceBeanPostprocessor());
		return context;
	}

	protected void initComponents() throws PluginEngineException {
		try {
			Enumeration<URL> modules = ClassLoader.getSystemResources("META-INF/components");
			while (modules.hasMoreElements()) {
				URL url = modules.nextElement();
				logger.info("find modules declaration:[{}]", url);
				List<String> lines = IOUtils.readLines(url.openStream());
				for (String clazz : lines) {
					logger.info("####try to create module:[{}]####", clazz);
					Component module = (Component) Class.forName(clazz)
							.getConstructor(SiriusApplicationContext.class)
							.newInstance(ApplicationContextLauncher.getApplicationContext());

					module.init();
					logger.info("####module:[{}] init finished####", module.getName());
					COMPONENTS.add(module);
				}
			}
		} catch (Exception e) {
			throw new PluginEngineException(e);
		}
	}

	protected void initPlugins() {
		/*plugin被容器侦测到,触发初始化请求事件*/
		ApplicationContextLauncher.getApplicationContext().addApplicationListener(
				new ApplicationListener<PluginInitEvent>() {

					@Override
					public void onApplicationEvent(PluginInitEvent event) {
						Plugin plugin = (Plugin) event.getSource();
						PLUGINS.put(plugin.getName(), plugin);
					}
				}
		);
	}

}
