/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : spring-ext
 * user created : pippo
 * date created : 2012-3-22 - 下午3:44:45
 */
package com.sirius.plugin.mongo;

import org.springframework.beans.factory.FactoryBean;

import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * @since 2012-3-22
 * @author pippo
 */
public class CollectionFactoryBean implements FactoryBean<DBCollection> {

	private String name;

	private DB db;

	public void setName(String name) {
		this.name = name;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	@Override
	public DBCollection getObject() throws Exception {
		return this.db.getCollection(name);
	}

	@Override
	public Class<?> getObjectType() {
		return DBCollection.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
