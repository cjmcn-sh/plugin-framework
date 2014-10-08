/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.jpa.domain.repository;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.StatelessSessionBuilder;
import org.hibernate.TypeHelper;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.springframework.util.Assert;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

/**
 * User: pippo
 * Date: 14-2-13-21:35
 */
public class DelegateHibernatJPASessionFactory implements SessionFactory {

    private static final long serialVersionUID = -3921479917266040648L;

    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public SessionFactory getSessionFactory() {
        Assert.isInstanceOf(HibernateEntityManagerFactory.class, entityManagerFactory);
        return ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();
    }

    //**************************************************************************************//

    @Override
    public Session openSession() throws HibernateException {
        return ((HibernateEntityManager) entityManagerFactory.createEntityManager()).getSession();
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Session getCurrentSession() throws HibernateException {
        if (entityManager == null) {
            return openSession();
        }
        return ((HibernateEntityManager) entityManager).getSession();
    }

    //**************************************************************************************//

    @Override
    public SessionFactoryOptions getSessionFactoryOptions() {
        return getSessionFactory().getSessionFactoryOptions();
    }

    @Override
    public SessionBuilder withOptions() {
        return getSessionFactory().withOptions();
    }

    @Override
    public StatelessSessionBuilder withStatelessOptions() {
        return getSessionFactory().withStatelessOptions();
    }

    @Override
    public StatelessSession openStatelessSession() {
        return getSessionFactory().openStatelessSession();
    }

    @Override
    public StatelessSession openStatelessSession(Connection connection) {
        return getSessionFactory().openStatelessSession(connection);
    }

    public ClassMetadata getClassMetadata(@SuppressWarnings("rawtypes") Class entityClass) {
        return getSessionFactory().getClassMetadata(entityClass);
    }

    @Override
    public ClassMetadata getClassMetadata(String entityName) {
        return getSessionFactory().getClassMetadata(entityName);
    }

    @Override
    public CollectionMetadata getCollectionMetadata(String roleName) {
        return getSessionFactory().getCollectionMetadata(roleName);
    }

    @Override
    public Map<String, ClassMetadata> getAllClassMetadata() {
        return getSessionFactory().getAllClassMetadata();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getAllCollectionMetadata() {
        return getSessionFactory().getAllCollectionMetadata();
    }

    @Override
    public Statistics getStatistics() {
        return getSessionFactory().getStatistics();
    }

    @Override
    public void close() throws HibernateException {
        getSessionFactory().close();
    }

    @Override
    public boolean isClosed() {
        return getSessionFactory().isClosed();
    }

    @Override
    public Cache getCache() {
        return getSessionFactory().getCache();
    }

    @Override
    @Deprecated
    public void evict(@SuppressWarnings("rawtypes") Class persistentClass) throws HibernateException {
        getSessionFactory().evict(persistentClass);
    }

    @Override
    @Deprecated
    public void evict(@SuppressWarnings("rawtypes") Class persistentClass, Serializable id) throws HibernateException {
        getSessionFactory().evict(persistentClass, id);
    }

    @Override
    @Deprecated
    public void evictEntity(String entityName) throws HibernateException {
        getSessionFactory().evictEntity(entityName);
    }

    @Override
    @Deprecated
    public void evictEntity(String entityName, Serializable id) throws HibernateException {
        getSessionFactory().evictEntity(entityName, id);
    }

    @Override
    @Deprecated
    public void evictCollection(String roleName) throws HibernateException {
        getSessionFactory().evictCollection(roleName);
    }

    @Override
    @Deprecated
    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        getSessionFactory().evictCollection(roleName, id);
    }

    @Override
    @Deprecated
    public void evictQueries(String cacheRegion) throws HibernateException {
        getSessionFactory().evictQueries(cacheRegion);
    }

    @Override
    @Deprecated
    public void evictQueries() throws HibernateException {
        getSessionFactory().evictQueries();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Set getDefinedFilterNames() {
        return getSessionFactory().getDefinedFilterNames();
    }

    @Override
    public FilterDefinition getFilterDefinition(String filterName) throws HibernateException {
        return getSessionFactory().getFilterDefinition(filterName);
    }

    @Override
    public boolean containsFetchProfileDefinition(String name) {
        return getSessionFactory().containsFetchProfileDefinition(name);
    }

    @Override
    public TypeHelper getTypeHelper() {
        return getSessionFactory().getTypeHelper();
    }

    @Override
    public Reference getReference() throws NamingException {
        return getSessionFactory().getReference();
    }

}
