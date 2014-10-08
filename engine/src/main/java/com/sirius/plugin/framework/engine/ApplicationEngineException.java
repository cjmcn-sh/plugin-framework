package com.sirius.plugin.framework.engine;

/**
 * User: pippo
 * Date: 13-12-12-13:49
 */
public class ApplicationEngineException extends Exception {

    private static final long serialVersionUID = 5987630405146588107L;

    public ApplicationEngineException() {
    }

    public ApplicationEngineException(String message) {
        super(message);
    }

    public ApplicationEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationEngineException(Throwable cause) {
        super(cause);
    }

    public ApplicationEngineException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
