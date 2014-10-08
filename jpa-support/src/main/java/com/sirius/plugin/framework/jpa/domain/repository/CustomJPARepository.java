/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.repository;

import com.sirius.plugin.framework.jpa.domain.model.CTUEntity;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * User: pippo
 * Date: 13-12-19-15:45
 */
public class CustomJPARepository<E extends CTUEntity> extends SimpleJpaRepository<E, String> implements BaseRepository<E> {

    public CustomJPARepository(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    protected EntityManager entityManager;

    @Override
    @Transactional
    public <T> T execute(String name, Class<T> returnType, NamedQueryExecutor<T> executor) {
        TypedQuery<T> query = entityManager.createQuery(name, returnType);
        return executor.execute(query);
    }

    @Override
    @Transactional
    public <T> T execute(JPAProcedure<T> procedure) {
        return procedure.execute(entityManager);
    }

}
