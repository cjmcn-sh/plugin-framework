/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.sirius.utils.servlet3;

import org.apache.commons.lang3.SystemUtils;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * User: pippo
 * Date: 13-11-21-09:34
 */
public final class Environment extends SystemUtils {

    private static final Map<String, Object> env = new HashMap<String, Object>(4);

    /**
     * ****************************************************************************
     */
    public static final String CTU_PROFILE = "ctu.runtime.profile";

    public static void setCTUProfile(String profile) {
        env.put(CTU_PROFILE, profile);
    }

    public static String getCTUProfile() {
        return (String) env.get(CTU_PROFILE);
    }

    /**
     * ****************************************************************************
     */
    public static final String SERVLET_CONTEXT = "ctu.runtime.servlet_context";

    public static void setServletContext(ServletContext servletContext) {
        if (servletContext != null) {
            env.put(SERVLET_CONTEXT, servletContext);
        }
    }

    public static ServletContext getServletContext() {
        return (ServletContext) env.get(SERVLET_CONTEXT);
    }

    public static boolean isServletContainer() {
        return env.containsKey(SERVLET_CONTEXT);
    }

}
