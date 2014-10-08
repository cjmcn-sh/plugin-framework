/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.model;

import com.sirius.spring.BeanLocator;
import com.sirius.plugin.framework.jpa.domain.repository.BaseRepository;

import javax.persistence.MappedSuperclass;

/**
 * User: pippo
 * Date: 13-12-6-12:50
 */
@MappedSuperclass
public class CTUEntity extends BasePersistable {

    private static final long serialVersionUID = 545946784731302653L;

    public CTUEntity() {

    }

    public CTUEntity(String id) {
        super(id);
    }

    public <R extends BaseRepository<E>, E extends CTUEntity> R getRepository() {
        String clazz = getClass().getSimpleName();
        return BeanLocator.getBean(
				String.format("%s%sRepository", clazz.substring(0, 1).toLowerCase(), clazz.substring(1)));
    }

    @SuppressWarnings("unchecked")
    public <E extends CTUEntity> E save() {
        return (E) getRepository().save(this);

    }

    public void delete() {
        getRepository().delete(getId());
    }

}
