/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine;

/**
 * User: pippo
 * Date: 13-12-19-13:47
 */
public class PluginEngineRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 775188822495117453L;

    public PluginEngineRuntimeException() {
    }

    public PluginEngineRuntimeException(String message) {
        super(message);
    }

    public PluginEngineRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginEngineRuntimeException(Throwable cause) {
        super(cause);
    }

    public PluginEngineRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
