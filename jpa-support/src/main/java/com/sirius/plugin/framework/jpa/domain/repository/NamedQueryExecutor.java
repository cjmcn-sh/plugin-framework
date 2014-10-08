/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.repository;

import javax.persistence.TypedQuery;

/**
 * User: pippo
 * Date: 13-12-19-15:50
 */
public interface NamedQueryExecutor<T> {

    T execute(TypedQuery<T> query);

}
