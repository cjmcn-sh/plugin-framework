/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.plugin.hibernate;

import com.sirius.spring.BeanLocator;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * User: pippo
 * Date: 14-1-21-18:22
 */
public class SiriusOpenSessionInViewFilter extends OpenSessionInViewFilter {

	private static Pattern pattern_resource = Pattern.compile("(.*)/static/(.+)");

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		if (pattern_resource.matcher(request.getRequestURI()).find()) {
			filterChain.doFilter(request, response);
		} else {
			super.doFilterInternal(request, response, filterChain);
		}
	}

	@Override
	protected SessionFactory lookupSessionFactory() {
		return BeanLocator.getBean(SessionFactory.class);
	}
}
