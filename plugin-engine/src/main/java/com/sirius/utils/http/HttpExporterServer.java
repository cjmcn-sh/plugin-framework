package com.sirius.utils.http;

import com.sirius.utils.jetty.Servlet3JettyInbound;
import com.sirius.utils.servlet3.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.Ordered;

/**
 * Created by pippo on 14-10-9.
 */
public class HttpExporterServer extends Servlet3JettyInbound implements ExporterServer,
		ApplicationListener<ApplicationContextEvent>, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(HttpExporterServer.class);

	/* 在applicationcontext启动完成后再启动内嵌jetty */
	/* 避免内置的ioc依赖还没有被初始化或循环依赖 */
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event instanceof ContextStartedEvent) {
			start();
		}

		if (event instanceof ContextClosedEvent) {
			stop();
		}
	}

	/* 该应用应该在所有bean之后初始化 */
	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void start() {
		/* 如果在serlvet3容器内,那么没有必要再启动新的webserver */
		if (Environment.isServletContainer()) {
			logger.warn(
					"current application in servlet3 container, ignore start embedded jetty server, the real export service path is:[{}]",
					Environment.getServletContext().getContextPath());
			return;
		}

		setJmxDomain(String.format("jetty-%s", System.currentTimeMillis()));
		super.start();
	}

}
