/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.repository;

import javax.persistence.EntityManager;

/**
 * User: pippo
 * Date: 13-12-19-15:50
 */
public interface JPAProcedure<T> {

    T execute(EntityManager entityManager);

}
