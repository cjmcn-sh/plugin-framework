package com.sirius.plugin.framework.engine;

/**
 * User: pippo
 * Date: 13-12-12-13:49
 */
public class PluginEngineException extends Exception {

    private static final long serialVersionUID = 5987630405146588107L;

    public PluginEngineException() {
    }

    public PluginEngineException(String message) {
        super(message);
    }

    public PluginEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginEngineException(Throwable cause) {
        super(cause);
    }

    public PluginEngineException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
