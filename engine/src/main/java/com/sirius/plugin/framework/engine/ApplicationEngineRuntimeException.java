/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine;

/**
 * User: pippo
 * Date: 13-12-19-13:47
 */
public class ApplicationEngineRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 775188822495117453L;

    public ApplicationEngineRuntimeException() {
    }

    public ApplicationEngineRuntimeException(String message) {
        super(message);
    }

    public ApplicationEngineRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationEngineRuntimeException(Throwable cause) {
        super(cause);
    }

    public ApplicationEngineRuntimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
