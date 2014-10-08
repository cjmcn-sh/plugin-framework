/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : spring-ext
 * user created : pippo
 * date created : 2012-3-22 - 下午3:44:45
 */
package com.sirius.plugin.mongo;

import org.springframework.beans.factory.FactoryBean;

import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * @since 2012-3-22
 * @author pippo
 */
public class DatabaseFactoryBean implements FactoryBean<DB> {

	private String name;

	private Mongo mongo;

	public void setName(String name) {
		this.name = name;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	@Override
	public DB getObject() throws Exception {
		return this.mongo.getDB(name);
	}

	@Override
	public Class<?> getObjectType() {
		return DB.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
