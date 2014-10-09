/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : connection-manager
 * user created : pippo
 * date created : 2010-12-8 - 下午01:59:15
 */
package com.sirius.spring;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * @since 2010-12-8
 * @author pippo
 */
public class ShareContextLoader extends ContextLoader {

	public static final String shared_context_name = "shared.parent.context";

	@Override
	protected ApplicationContext loadParentContext(ServletContext servletContext) throws BeansException {
		//		return (ApplicationContext) servletContext.getAttribute(shared_context_name);
		return BeanLocator.getApplicationContext();
	}
}
