/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine.module;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * User: pippo
 * Date: 13-12-4-16:41
 */
public abstract class SiriusBaseController {

    @ModelAttribute
    public void setPluginName(ModelMap context) {
        context.addAttribute("pluginName", getPluginName());
    }

    @ModelAttribute
    public void setPluginPath(ModelMap context) {
        context.addAttribute("pluginPath", getPluginPath());
    }

    @ModelAttribute
    public void setHomePath(ModelMap context) {
        context.addAttribute("homePath", getHomePath());
    }

    protected abstract String getPluginName();

    protected abstract String getPluginPath();

    protected abstract String getHomePath();

}
