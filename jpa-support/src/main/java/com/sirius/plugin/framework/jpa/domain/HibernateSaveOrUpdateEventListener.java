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
import com.sirius.utils.thread.ThreadContext;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pippo
 * @since 2013-7-8
 */
public class HibernateSaveOrUpdateEventListener implements SaveOrUpdateEventListener {

	private static final long serialVersionUID = -8268719061764049975L;

	private static final Logger logger = LoggerFactory.getLogger(HibernateSaveOrUpdateEventListener.class);

	public static final String THREAD_AUDITOR_KEY = "sirius.auditor";

	public static final String DEFAULT_AUDITOR = "anonymous";

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		createStamp(event.getObject());
		updateStamp(event.getObject());

		logger.debug("begin save or update entity:[{}]", event.getObject());
	}

	protected void createStamp(Object entity) {

		if (!(entity instanceof BaseEntity)) {
			return;
		}

		((BaseEntity) entity).setTimeCreated(System.currentTimeMillis());

		String auditor = ThreadContext.get(THREAD_AUDITOR_KEY);
		((BaseEntity) entity).setUserCreated(auditor != null ? auditor : DEFAULT_AUDITOR);
	}

	protected void updateStamp(Object entity) {

		if (!(entity instanceof BaseEntity)) {
			return;
		}

		((BaseEntity) entity).setTimeModified(System.currentTimeMillis());

		String auditor = ThreadContext.get(THREAD_AUDITOR_KEY);
		((BaseEntity) entity).setUserModified(auditor != null ? auditor : DEFAULT_AUDITOR);
	}

}
