/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

/**
 * project : ctu-framework
 * user created : pippo
 * date created : 2013-7-8 - 下午5:11:34
 */
package com.sirius.plugin.framework.jpa.domain;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2013-7-8
 * @author pippo
 */
public class HibernateCustomIntegrator implements Integrator {

	private static final Logger logger = LoggerFactory.getLogger(HibernateCustomIntegrator.class);

	@Override
	public void integrate(Configuration configuration,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		final EventListenerRegistry eventRegistry = serviceRegistry.getService(EventListenerRegistry.class);

		logger.info("Registering event listeners");
		eventRegistry.prependListeners(EventType.SAVE_UPDATE, new HibernateSaveOrUpdateEventListener());
		eventRegistry.prependListeners(EventType.SAVE, new HibernateSaveOrUpdateEventListener());
		eventRegistry.prependListeners(EventType.UPDATE, new HibernateSaveOrUpdateEventListener());
		eventRegistry.prependListeners(EventType.PERSIST, new HibernatePersistEventListener());
	}

	@Override
	public void integrate(MetadataImplementor metadata,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		// TODO Auto-generated method stub

	}

}
