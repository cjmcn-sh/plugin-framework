/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

/**
 * project : ctu-framework
 * user created : pippo
 * date created : 2013-7-8 - 下午4:35:18
 */
package com.sirius.plugin.framework.jpa.domain;

import com.sirius.plugin.framework.jpa.domain.model.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author pippo
 * @since 2013-7-8
 */
public class HibernatePersistEventListener extends HibernateSaveOrUpdateEventListener implements PersistEventListener {

	private static final long serialVersionUID = -7906294844225450282L;

	private static final Logger logger = LoggerFactory.getLogger(HibernatePersistEventListener.class);

	@Override
	public void onPersist(PersistEvent event) throws HibernateException {
		logger.debug("begin save or update entity:[{}]", event.getObject());
		Object entity = event.getObject();

		if (!(entity instanceof BaseEntity)) {
			return;
		}

		if (((BaseEntity) entity).isNew()) {
			createStamp(entity);
		}

		updateStamp(entity);
	}

	@Override
	public void onPersist(PersistEvent event, @SuppressWarnings("rawtypes") Map createdAlready)
			throws HibernateException {
		this.onPersist(event);
	}

}
