package com.sirius.plugin.framework.jpa.plugin.hibernate;

import com.sirius.plugin.framework.engine.AppSettings;
import com.sirius.plugin.framework.engine.module.Plugin;
import com.sirius.plugin.framework.engine.module.WebPluginInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;

/**
 * Created by pippo on 14-3-21.
 */
@WebListener
public class SiriusOpenSessionInViewListener extends WebPluginInitListener {

    private static final Logger logger = LoggerFactory.getLogger(SiriusOpenSessionInViewListener.class);

    private static final String orm_type = AppSettings.getInstance().get("orm.framework.type");

    @Override
    protected void onPluginInit(ServletContext context, Plugin plugin) {
        if (!"hibernate4".equalsIgnoreCase(orm_type)) {
            return;
        }

        FilterRegistration openSessionInViewFilter = context.getFilterRegistration("OpenSessionInViewFilter");
        if (openSessionInViewFilter == null) {
            openSessionInViewFilter = context.addFilter("OpenSessionInViewFilter", new SiriusOpenSessionInViewFilter());
        }

        logger.info("regist openSessionInViewFilter for dispatcher:[{}]", plugin.getDispatcher().getName());
        openSessionInViewFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), true, plugin.getDispatcher().getName());
    }

}
