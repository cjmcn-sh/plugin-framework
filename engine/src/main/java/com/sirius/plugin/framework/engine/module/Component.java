/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine.module;

import org.springframework.context.support.SiriusApplicationContext;

/**
 * User: pippo
 * Date: 13-12-13-13:42
 */
public abstract class Component implements Module {

	public Component(SiriusApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private SiriusApplicationContext applicationContext;

	@Override
	public Type getType() {
		return Type.component;
	}

	@Override
	public void init() {
		this.applicationContext.appendConfigLocations(getName());
	}

}
