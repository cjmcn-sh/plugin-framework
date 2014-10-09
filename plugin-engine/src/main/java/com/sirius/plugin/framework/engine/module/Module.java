package com.sirius.plugin.framework.engine.module;

/**
 * User: pippo
 * Date: 13-12-12-13:22
 */
public interface Module {

    Type getType();

    String getName();

    void init();

    enum Type {

        component,

        plugin,

    }
}
