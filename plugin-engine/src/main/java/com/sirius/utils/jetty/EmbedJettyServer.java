package com.sirius.utils.jetty;

import com.sirius.spring.ClassScanner;
import com.sirius.spring.ClassScanner.Handler;
import com.sirius.utils.thread.ContextSupportJettyQueuedThreadPool;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LowResourceMonitor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 14-10-21.
 */
public abstract class EmbedJettyServer {

	private static final Logger logger = LoggerFactory.getLogger(EmbedJettyServer.class);

	protected static Server server = null;

	public static Server getServer() {
		return server;
	}

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
		final HandlerList handlers = new HandlerList();

		defaultWebAppContext = createDefaultWebAppContext();
		if (defaultWebAppContext != null) {
			handlers.addHandler(defaultWebAppContext);
		}

		ClassScanner scanner = new ClassScanner();
		scanner.addIncludeFilter(new AnnotationTypeFilter(WebAppRegistry.class));
		scanner.scan(webAppScanPackage, "**/package-info.class", new Handler() {

			@Override
			public void handle(MetadataReader metadataReader) {
				AnnotationAttributes[] webapps = (AnnotationAttributes[]) metadataReader.getAnnotationMetadata()
						.getAnnotationAttributes(WebAppRegistry.class.getName())
						.get("value");

				for (AnnotationAttributes webapp : webapps) {
					String dir = webapp.getString("dir");
					String contextPath = webapp.getString("contextPath");
					String[] welcomeFiles = webapp.getStringArray("welcomeFiles");

					WebAppContext context = createWebAppContext(dir, contextPath, welcomeFiles);
					if (context != null) {
						handlers.addHandler(context);
					}
				}
			}
		});

		return handlers;
	}

	protected WebAppContext defaultWebAppContext = null;

	protected WebAppContext createDefaultWebAppContext() {
		defaultWebAppContext = createWebAppContext(defaultWebAppDir,
				defaultWebAppContextPath,
				new String[] { "index.html" });

		return defaultWebAppContext;
	}

	protected Map<String, File> existContextPath = new HashMap<String, File>();

	protected WebAppContext createWebAppContext(String dir, String contextPath, String[] welcomeFiles) {
		File webApp = null;
		if (EmbedJettyServer.class.getResource(dir) != null) {
			webApp = new File(EmbedJettyServer.class.getResource(dir).getFile());
		} else {
			webApp = new File(dir);
		}

		if (!webApp.exists()) {
			logger.warn("invalid webapp dir:[{}], ignore it", webApp);
			return null;
		}

		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}

		if (existContextPath.containsKey(contextPath)) {
			logger.warn("conflict contextPath:[{}] mapping:[{}] and [{}], ignore mapping:[{}]",
					contextPath,
					existContextPath.get(contextPath),
					webApp,
					webApp);

			return null;
		}

		WebAppContext context = new WebAppContext(webApp.getAbsolutePath(), contextPath);
		context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
		context.setWelcomeFiles(welcomeFiles);

		if (!useSession) {
			context.setSessionHandler(new SessionHandler(new EmptySessionManager()));
		}

		existContextPath.put(contextPath, webApp);
		logger.info("create webapp:[{}] with contextPath:[{}]", webApp, contextPath);
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

		if (StringUtils.isBlank(jmxDomain)) {
			jmxDomain = String.format("jetty-%s", System.currentTimeMillis());
		}

		mbContainer.setDomain(jmxDomain);
		server.addBean(mbContainer);
	}

	protected void createStatistics() {
		StatisticsHandler stats = new StatisticsHandler();
		stats.setHandler(server.getHandler());
		server.setHandler(stats);
	}

	protected int minThreads = Math.max(4, Runtime.getRuntime().availableProcessors());
	protected int maxThreads = minThreads * 40 + 1;
	protected String host;
	protected int port;
	protected String jmxDomain;
	protected boolean useSession = false;
	protected String defaultWebAppDir = "/";
	protected String defaultWebAppContextPath = "/";
	protected String webAppScanPackage = "com.sirius";

	public void setMinThreads(int minThreads) {
		if (minThreads > 4) {
			this.minThreads = minThreads;
		}
	}

	public void setMaxThreads(int maxThreads) {
		if (maxThreads > 4) {
			this.maxThreads = maxThreads;
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setJmxDomain(String jmxDomain) {
		this.jmxDomain = jmxDomain;
	}

	public void setUseSession(boolean useSession) {
		this.useSession = useSession;
	}

	public void setDefaultWebAppDir(String defaultWebAppDir) {
		this.defaultWebAppDir = defaultWebAppDir;
	}

	public void setDefaultWebAppContextPath(String defaultWebAppContextPath) {
		this.defaultWebAppContextPath = defaultWebAppContextPath;
	}

	public void setWebAppScanPackage(String webAppScanPackage) {
		this.webAppScanPackage = webAppScanPackage;
	}

	public static void main(String[] args) {
		EmbedJettyServer embedJettyServer = new EmbedJettyServer() {

			@Override
			protected WebAppContext createDefaultWebAppContext() {
				return createWebAppContext("/", "/", new String[] { "index.html" });
			}

		};
		embedJettyServer.setHost("127.0.0.1");
		embedJettyServer.setPort(8080);
		embedJettyServer.start();
	}
}
