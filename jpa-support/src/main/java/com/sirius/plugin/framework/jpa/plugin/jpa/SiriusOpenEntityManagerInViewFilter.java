/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.plugin.jpa;

import com.sirius.spring.BeanLocator;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import javax.persistence.EntityManagerFactory;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * User: pippo
 * Date: 14-1-1-16:11
 */
public class SiriusOpenEntityManagerInViewFilter extends OpenEntityManagerInViewFilter {

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
	protected EntityManagerFactory lookupEntityManagerFactory() {
		return BeanLocator.getBean(EntityManagerFactory.class);
	}

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("(.*)/static/(.+)");
		System.out.println(pattern.matcher("/static/aa/bb").find());
		System.out.println(pattern.matcher("/aa/bb/static/cc").find());
	}

}
