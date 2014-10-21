/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
/**
 * project : service-gateway
 * user created : pippo
 * date created : 2011-11-15 - 下午1:10:53
 */
package com.sirius.utils.jetty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pippo
 * @since 2011-11-15
 * #Deprecated
 */
@Deprecated
public abstract class AbstractJettyInbound extends EmbedJettyServer {

	private static Logger logger = LoggerFactory.getLogger(AbstractJettyInbound.class);

	public void setWebRoot(String webRoot) {
		setDefaultWebAppContextPath(webRoot);
	}

	public void setJmx_domain(String jmx_domain) {
		setJmxDomain(jmx_domain);
	}

	public void setUse_session(boolean use_session) {
		setUseSession(use_session);
	}

	public static void main(String[] args) {
		AbstractJettyInbound jettyInbound = new AbstractJettyInbound() {

		};

		jettyInbound.host = "127.0.0.1";
		jettyInbound.port = 8080;

		jettyInbound.start();
	}
}
