/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
/**
 * project : service-gateway
 * user created : pippo
 * date created : 2011-11-15 - 下午1:10:53
 */
package com.sirius.utils.jetty;

import com.sirius.utils.thread.ContextSupportJettyQueuedThreadPool;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LowResourceMonitor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.URL;

/**
 * @author pippo
 * @since 2011-11-15
 */
public abstract class AbstractJettyInbound {

	private static Logger logger = LoggerFactory.getLogger(AbstractJettyInbound.class);

	protected Server server = null;

	public void start() {
		if (server != null) {
			logger.warn("the jetty server:[{}] is exists, ignore start operation");
			return;
		}

		try {
			createServer();
			//createResourcesMonitor();
			createJmxSupport();
			createStatistics();

			if (server != null) {
				server.start();
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public void stop() {
		if (server == null) {
			logger.debug("the jetty server not exists, ignore stop operation");
			return;
		}

		try {
			server.stop();
			server = null;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public boolean isOpen() {
		return this.server != null && this.server.isRunning();
	}

	protected void createServer() throws Exception {
		server = new Server(createThreadPool());
		server.setDumpAfterStart(false);
		server.setDumpBeforeStop(false);
		server.setStopAtShutdown(true);
		server.addConnector(createConnector());
		server.setHandler(createWebAppHandler());
		logger.debug("create jetty server");
	}

	protected QueuedThreadPool createThreadPool() {
		QueuedThreadPool threadPool = new ContextSupportJettyQueuedThreadPool();
		threadPool.setName("jetty inbound thread pool");
		threadPool.setIdleTimeout(1000 * 60);

		if (maxThreads < minThreads) {
			maxThreads = minThreads;
		}

		threadPool.setMinThreads(minThreads);
		threadPool.setMaxThreads(maxThreads);
		logger.debug("create jetty thread pool:[{}]", threadPool);
		return threadPool;
	}

	protected Connector createConnector() {
		ServerConnector connector = new ServerConnector(server);
		connector.setHost(this.host);
		connector.setPort(this.port);

		logger.debug("create jetty connector:[{}]", connector);
		return connector;
	}

	protected HandlerList createWebAppHandler() {
		HandlerList handlers = new HandlerList();
		/*webApp静态资源目录*/
		ResourceHandler resourceHandler = createWebAppResourceHandler();
		if (resourceHandler != null) {
			handlers.addHandler(createWebAppResourceHandler());
		}
		/*webApp程序所在目录*/
		handlers.addHandler(createWebAppContext());
		return handlers;
	}

	protected ResourceHandler createWebAppResourceHandler() {
		URL webAppResource = AbstractJettyInbound.class.getResource("/webapp");
		if (webAppResource == null) {
			return null;
		}

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(welcomeFiles);
		resource_handler.setResourceBase(webAppResource.getFile());
		return resource_handler;
	}

	protected WebAppContext createWebAppContext() {
		WebAppContext context = new WebAppContext(getContextRoot(), "/" + webRoot);

		context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");

		if (!use_session) {
			context.setSessionHandler(new SessionHandler(new EmptySessionManager()));
		}

		logger.info("create webapp context:[{}] with root:[{}]", context, getContextRoot());
		return context;
	}

	protected void createResourcesMonitor() {
		LowResourceMonitor lowResourcesMonitor = new LowResourceMonitor(server);
		lowResourcesMonitor.setPeriod(1000);
		lowResourcesMonitor.setLowResourcesIdleTimeout(200);
		lowResourcesMonitor.setMonitorThreads(true);
		lowResourcesMonitor.setMaxConnections(10240);
		lowResourcesMonitor.setMaxMemory((long) (Runtime.getRuntime().totalMemory() * 0.8));
		lowResourcesMonitor.setMaxLowResourcesTime(5000);
		server.addBean(lowResourcesMonitor);
	}

	protected void createJmxSupport() {
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());

		if (StringUtils.isBlank(jmx_domain)) {
			jmx_domain = webRoot;
		}

		if (StringUtils.isBlank(jmx_domain)) {
			jmx_domain = String.format("jetty-%s", System.currentTimeMillis());
		}

		mbContainer.setDomain(jmx_domain);
		server.addBean(mbContainer);
	}

	protected void createStatistics() {
		StatisticsHandler stats = new StatisticsHandler();
		stats.setHandler(server.getHandler());
		server.setHandler(stats);
	}

	protected String getContextRoot() {
		return AbstractJettyInbound.class.getResource("/").getFile();
	}

	protected int port;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	protected String host;

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	protected String webRoot = "";

	public String getWebRoot() {
		return this.webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	protected String[] welcomeFiles = new String[] { "index.html" };

	public String[] getWelcomeFiles() {
		return welcomeFiles;
	}

	public void setWelcomeFiles(String[] welcomeFiles) {
		this.welcomeFiles = welcomeFiles;
	}

	protected String jmx_domain;

	public String getJmx_domain() {
		return jmx_domain;
	}

	public void setJmx_domain(String jmx_domain) {
		this.jmx_domain = jmx_domain;
	}

	protected int minThreads = Math.max(4, Runtime.getRuntime().availableProcessors());

	protected int maxThreads = minThreads * 40 + 1;

	public int getMinThreads() {
		return minThreads;
	}

	public void setMinThreads(int minThreads) {
		if (minThreads > 4) {
			this.minThreads = minThreads;
		}
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		if (maxThreads > 4) {
			this.maxThreads = maxThreads;
		}
	}

	private boolean use_session = false;

	public boolean isUse_session() {
		return use_session;
	}

	public void setUse_session(boolean use_session) {
		this.use_session = use_session;
	}

	public static void main(String[] args) {
		AbstractJettyInbound jettyInbound = new AbstractJettyInbound() {

		};

		jettyInbound.host = "127.0.0.1";
		jettyInbound.port = 8080;

		jettyInbound.start();
	}
}
