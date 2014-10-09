/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.sirius.spring;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * User: pippo
 * Date: 13-11-21-11:07
 */
public class ApplicationContextLauncher extends BeanLocator {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextLauncher.class);

    static {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();
    }

    public static ConfigurableApplicationContext create(String... xmlConfig) {
        if (context == null) {
            context = new ClassPathXmlApplicationContext(xmlConfig, false, null);
            context.registerShutdownHook();
            logger.info("create new root context:[{}]", context);
        }

        return context;
    }

    public static void start() {
        refresh();

        logger.info("start root context:[{}]", context);
        if (context != null) {
            context.start();
        }
    }

    public static void stop() {
        logger.info("stop root context:[{}]", context);
        if (context != null) {
            context.stop();
        }
    }

    public static void refresh() {
        logger.info("refresh root context:[{}]", context);
        if (context != null) {
            context.refresh();
        }
    }

    public static void activeProfile(String profile) {
        logger.info("active profile:[{}] to root context:[{}]", profile, context);
        if (context == null || StringUtils.isBlank(profile)) {
            return;
        }

        context.getEnvironment().addActiveProfile(profile);
        refresh();
    }

}
