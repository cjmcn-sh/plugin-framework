/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

/**
 * project : ctu-framework
 * user created : pippo
 * date created : 2013-7-16 - 上午10:37:59
 */
package com.sirius.plugin.framework.jpa.domain;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * @since 2013-7-16
 * @author pippo
 */
public class HibernateImprovedNamingStrategy extends ImprovedNamingStrategy {

	private static final long serialVersionUID = -8834782834428093556L;

	private static final String TABLE_PATTERN = "%s_t";

	@Override
	public String classToTableName(String className) {
		return String.format(TABLE_PATTERN, super.classToTableName(className));
	}

	@Override
	public String collectionTableName(String ownerEntity,
			String ownerEntityTable,
			String associatedEntity,
			String associatedEntityTable,
			String propertyName) {
		return String.format(TABLE_PATTERN, super.collectionTableName(ownerEntity,
			ownerEntityTable,
			associatedEntity,
			associatedEntityTable,
			propertyName));
	}

	@Override
	public String logicalCollectionTableName(String tableName,
			String ownerEntityTable,
			String associatedEntityTable,
			String propertyName) {
		return String.format(TABLE_PATTERN,
			super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName));
	}
}
