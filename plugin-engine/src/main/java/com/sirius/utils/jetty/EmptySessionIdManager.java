/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : service-gateway-agent
 * user created : pippo
 * date created : 2012-6-6 - 下午8:07:53
 */
package com.sirius.utils.jetty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.session.AbstractSessionIdManager;

/**
 * @since 2012-6-6
 * @author pippo
 */
public class EmptySessionIdManager extends AbstractSessionIdManager {

	public final static String defaultId = "1";

	public final static String nodeId = "node_1";

	public final static String clusterId = "culster_1";

	@Override
	public boolean idInUse(String id) {
		return false;
	}

	@Override
	public void addSession(HttpSession session) {

	}

	@Override
	public void removeSession(HttpSession session) {

	}

	@Override
	public void invalidateAll(String id) {

	}

	@Override
	public String getClusterId(String nodeId) {
		return clusterId;
	}

	@Override
	public String getNodeId(String clusterId, HttpServletRequest request) {
		return nodeId;
	}

	@Override
	public String newSessionId(HttpServletRequest request, long created) {
		return defaultId;
	}

	@Override
	public void renewSessionId(String oldClusterId, String oldNodeId, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}
}
