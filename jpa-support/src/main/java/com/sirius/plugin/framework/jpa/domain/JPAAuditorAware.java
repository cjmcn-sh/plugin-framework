/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain;

import com.sirius.utils.thread.ThreadContext;
import org.springframework.data.domain.AuditorAware;

/**
 * User: pippo
 * Date: 13-12-6-21:38
 */
public class JPAAuditorAware implements AuditorAware<String> {

	public static final String THREAD_AUDITOR_KEY = "sirius.auditor";

	public static final String DEFAULT_AUDITOR = "anonymous";

	@Override
	public String getCurrentAuditor() {
		String auditor = ThreadContext.get(THREAD_AUDITOR_KEY);
		return auditor != null ? auditor : DEFAULT_AUDITOR;
	}
}
