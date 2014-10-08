/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : spring-ext
 * user created : pippo
 * date created : 2012-3-5 - 下午12:23:21
 */
package com.sirius.plugin.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pippo
 * @since 2012-3-5
 */
public class ClusterMongoFactoryBean implements FactoryBean<Mongo> {

    private static Logger logger = LoggerFactory.getLogger(ClusterMongoFactoryBean.class);

    private Mongo mongo;

    @Override
    public Mongo getObject() throws Exception {
        if (this.mongo == null) {
            init();
        }

        return mongo;
    }

    @Override
    public Class<?> getObjectType() {
        return Mongo.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void init() throws UnknownHostException {
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        for (String address : this.addressList.split(";")) {
            String[] a = address.split(":");
            String host = a[0];
            int port = Integer.parseInt(a[1]);
            ServerAddress serverAddress = new ServerAddress(host, port);
            addrs.add(serverAddress);
        }

        MongoClientOptions options = MongoClientOptions.builder()
                .autoConnectRetry(this.autoConnectRetry)
                .connectTimeout(this.connectTimeout)
                .socketTimeout(this.socketTimeout)
                .socketKeepAlive(this.socketKeepAlive)
                .connectionsPerHost(this.connectionsPerHost)
                .threadsAllowedToBlockForConnectionMultiplier(this.threadsAllowedToBlockForConnectionMultiplier)
                .maxWaitTime(this.threadMaxWaitTime)
                .writeConcern(WriteConcern.SAFE)
                .readPreference(ReadPreference.secondaryPreferred())
                .build();

        logger.info("try to create mongo instance with options:[{}]", options.toString());

        this.mongo = addrs.size() > 1 ? new MongoClient(addrs, options) : new MongoClient(addrs.get(0), options);
        this.mongo.setWriteConcern(WriteConcern.JOURNALED);
        this.mongo.setReadPreference(ReadPreference.secondaryPreferred());

        logger.info("mongo instance:[{}] created", this.mongo.toString());
    }

    private String addressList;

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

    private boolean autoConnectRetry = true;

    private int connectTimeout = 1000;

    private int socketTimeout = 0;

    private boolean socketKeepAlive = true;

    private int connectionsPerHost = 20;

    private int threadsAllowedToBlockForConnectionMultiplier = Runtime.getRuntime().availableProcessors() * 4;

    private int threadMaxWaitTime = 120000;

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public void setAutoConnectRetry(boolean autoConnectRetry) {
        this.autoConnectRetry = autoConnectRetry;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadMaxWaitTime(int threadMaxWaitTime) {
        this.threadMaxWaitTime = threadMaxWaitTime;
    }

}
