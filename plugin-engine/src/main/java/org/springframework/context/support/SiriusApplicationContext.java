/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package org.springframework.context.support;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User: pippo
 * Date: 13-12-13-15:16
 */
public class SiriusApplicationContext extends AbstractXmlApplicationContext {

	public SiriusApplicationContext(ApplicationContext parent) {
		super(parent);
		registerShutdownHook();
	}

	public SiriusApplicationContext(String... locations) throws BeansException {
		setConfigLocations(locations);
		registerShutdownHook();
	}

	private Set<ConfigurableApplicationContext> children = new LinkedHashSet<>();

	public SiriusApplicationContext addChildApplication(ConfigurableApplicationContext applicationContext) {
		children.add(applicationContext);
		applicationContext.setParent(this);
		return this;
	}

	public SiriusApplicationContext appendConfigLocations(String location) {
		String[] configLocations = getConfigLocations();
		setConfigLocations(ArrayUtils.add(configLocations, location));
		return this;
	}

	private Set<Resource> configLocations = new LinkedHashSet<>();

	public SiriusApplicationContext appendConfigLocations(Resource location) {
		configLocations.add(location);
		return this;
	}

	@Override
	protected Resource[] getConfigResources() {
		return configLocations.toArray(new Resource[0]);
	}
}
