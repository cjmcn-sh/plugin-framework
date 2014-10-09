/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.repository;

import com.sirius.plugin.framework.jpa.domain.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

/**
 * User: pippo Date: 13-12-19-20:33
 */
public class CustomJPARepositoryFactoryBean<R extends JpaRepository<E, String>, E extends BaseEntity> extends
        JpaRepositoryFactoryBean<R, E, String> {

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new MyRepositoryFactory<E>(entityManager);
    }

    private static class MyRepositoryFactory<E extends BaseEntity> extends JpaRepositoryFactory {

        private EntityManager entityManager;

        public MyRepositoryFactory(EntityManager entityManager) {
            super(entityManager);

            this.entityManager = entityManager;
        }

        @SuppressWarnings("unchecked")
        protected Object getTargetRepository(RepositoryMetadata metadata) {
            return new CustomJPARepository<E>(
                    (JpaEntityInformation<E, ?>) getEntityInformation(metadata.getDomainType()),
                    entityManager);
        }

        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return CustomJPARepository.class;
        }
    }
}
