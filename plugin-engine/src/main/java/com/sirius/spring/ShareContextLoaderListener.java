/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : connection-manager
 * user created : pippo
 * date created : 2010-12-8 - 下午02:03:42
 */
package com.sirius.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;

/**
 * @author pippo
 * @since 2010-12-8
 */
public class ShareContextLoaderListener extends ContextLoaderListener {

    @Override
    protected ApplicationContext loadParentContext(ServletContext servletContext) {
        return new ShareContextLoader().loadParentContext(servletContext);
    }
}
