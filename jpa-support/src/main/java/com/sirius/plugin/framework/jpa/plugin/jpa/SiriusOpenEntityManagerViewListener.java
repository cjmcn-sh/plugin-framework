package com.sirius.plugin.framework.jpa.plugin.jpa;

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
public class SiriusOpenEntityManagerViewListener extends WebPluginInitListener {

    private static final Logger logger = LoggerFactory.getLogger(SiriusOpenEntityManagerViewListener.class);

    private static final String orm_type = AppSettings.getInstance().get("orm.framework.type");

    @Override
    protected void onPluginInit(ServletContext context, Plugin plugin) {
        if (!"jpa".equalsIgnoreCase(orm_type)) {
            return;
        }

        FilterRegistration openEntityManagerInViewFilter = context.getFilterRegistration("OpenEntityManagerInViewFilter");
        if (openEntityManagerInViewFilter == null) {
            openEntityManagerInViewFilter = context.addFilter("OpenEntityManagerInViewFilter", new SiriusOpenEntityManagerInViewFilter());
        }

        logger.info("regist OpenEntityManagerInViewFilter for dispatcher:[{}]", plugin.getDispatcher().getName());
        openEntityManagerInViewFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class),
                                                                true,
                                                                plugin.getDispatcher().getName());
    }
}
