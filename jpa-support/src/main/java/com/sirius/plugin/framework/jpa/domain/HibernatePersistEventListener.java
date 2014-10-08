/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

/**
 * project : ctu-framework
 * user created : pippo
 * date created : 2013-7-8 - 下午4:35:18
 */
package com.sirius.plugin.framework.jpa.domain;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2013-7-8
 * @author pippo
 */
public class HibernatePersistEventListener extends HibernateSaveOrUpdateEventListener implements PersistEventListener {

	private static final long serialVersionUID = -7906294844225450282L;

	private static final Logger logger = LoggerFactory.getLogger(HibernatePersistEventListener.class);

	@Override
	public void onPersist(PersistEvent event) throws HibernateException {
		logger.debug("begin save or update entity:[{}]", event.getObject());
		Object entity = event.getObject();
		createStamp(entity);
		updateStamp(entity);
	}

	@Override
	public void onPersist(PersistEvent event, @SuppressWarnings("rawtypes") Map createdAlready)
			throws HibernateException {
		this.onPersist(event);
	}

}
