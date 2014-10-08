/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.sirius.utils.servlet3;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * User: pippo
 * Date: 13-11-21-10:34
 */
public interface WebApplicationInitializer {

    void onStartup(ServletContext servletContext) throws ServletException;

}
