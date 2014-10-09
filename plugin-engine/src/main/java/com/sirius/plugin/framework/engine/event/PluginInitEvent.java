/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine.event;

import com.sirius.plugin.framework.engine.module.Plugin;
import org.springframework.context.ApplicationEvent;

/**
 * User: pippo
 * Date: 13-12-16-19:42
 */
public class PluginInitEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5718745480434731425L;

    public PluginInitEvent(Plugin source) {
        super(source);
    }
}
