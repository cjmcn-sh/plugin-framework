/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.repository;

import com.sirius.plugin.framework.jpa.domain.model.CTUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * User: pippo
 * Date: 13-12-18-12:36
 */
@NoRepositoryBean
public interface BaseRepository<E extends CTUEntity> extends JpaRepository<E, String>, JpaSpecificationExecutor<E> {

    <T> T execute(JPAProcedure<T> executor);

    <T> T execute(String name, Class<T> returnType, NamedQueryExecutor<T> executor);
}
