/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : sirius
 * user created : pippo
 * date created : 2007-7-16-下午03:06:03
 */
package com.sirius.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

/**
 * Init spring WebApplicationContext and init utils class SpringLocator
 *
 * @author pippo
 */
public class SpringInitListener extends ContextLoaderListener {

    private static Logger logger = LoggerFactory.getLogger(SpringInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.info("Begin init spring WebApplicationContext");
        super.contextInitialized(event);
        BeanLocator.setApplicationContext(
                (ConfigurableApplicationContext) WebApplicationContextUtils.getWebApplicationContext(
                        event.getServletContext()));
        logger.info("Spring WebApplicationContext init successful");

    }
}
