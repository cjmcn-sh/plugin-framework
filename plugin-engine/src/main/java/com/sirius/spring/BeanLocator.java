/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : openfire-xmppserver
 * user created : pippo
 * date created : 2010-11-5 - 上午09:37:52
 */
package com.sirius.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pippo
 * @since 2010-11-5
 */
public class BeanLocator {

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        String[] names = context.getBeanNamesForType(clazz);
        if (names == null || names.length == 0) {
            return null;
        }
        return context.getBean(names[0], clazz);
    }

    public static <T> List<T> getBeans(Class<T> clazz) {
        String[] names = context.getBeanNamesForType(clazz);
        if (names == null || names.length == 0) {
            return null;
        }
        List<T> beans = new ArrayList<T>();
        for (String name : names) {
            beans.add(context.getBean(name, clazz));
        }
        return beans;
    }

    public static <T> T getBean(Class<T> clazz, BeanSelector<T> selector) {
        String[] names = context.getBeanNamesForType(clazz);
        if (names == null || names.length == 0) {
            return null;
        }

        for (String beanName : names) {
            T bean = context.getBean(beanName, clazz);
            if (selector.select(bean, context)) {
                return bean;
            }
        }

        return null;
    }

    protected static ConfigurableApplicationContext context = null;

    public static void setApplicationContext(ConfigurableApplicationContext context) {
        BeanLocator.context = context;
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * context可能存在多个满足条件的bean,由用户实现选择逻辑
     *
     * @param <T>
     * @author pippo
     * @since 2010-8-16
     */
    public static interface BeanSelector<T> {

        boolean select(T bean, ApplicationContext context);

    }

}
