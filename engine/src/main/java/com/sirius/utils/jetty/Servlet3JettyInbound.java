/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
/**
 * project : service-gateway
 * user created : pippo
 * date created : 2011-11-15 - 下午1:10:53
 */
package com.sirius.utils.jetty;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pippo
 * @since 2011-11-15
 */
public class Servlet3JettyInbound extends AbstractJettyInbound {

    private static Logger logger = LoggerFactory.getLogger(Servlet3JettyInbound.class);

    protected void createServer() throws Exception {
        super.createServer();
         /*添加servlet3.0支持*/
        Configuration.ClassList
                .setServerDefault(server)
                .add(0, AnnotationConfiguration.class.getName());
    }

    protected WebAppContext createWebAppContext() {
        WebAppContext context = super.createWebAppContext();

        Set<Resource> containerResources = new HashSet<Resource>();
        Set<Resource> webInfJarResources = new HashSet<Resource>();

        try {
            containerResources.add(Resource.newResource(Servlet3JettyInbound.class.getResource("/")));

            /*如果运行在嵌入式环境,依赖的lib会在webapp的classloader之前加载,那么不会作为当前webapp的资源被扫描*/
            /*所以此处主动扫描,并加入到当前webapp的WEB-INF/lib下*/
            Enumeration<URL> urls = ClassLoader.getSystemResources("META-INF/web-fragment.xml");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Resource resource = Resource.newResource(
                        url.getFile().replace("/META-INF/web-fragment.xml", "").replace("!", ""));

                if (resource.isDirectory()) {
                    containerResources.add(resource);
                } else {
                    webInfJarResources.add(resource);
                }
            }

            for (Resource resource : containerResources) {
                context.getMetaData().addContainerResource(resource);
                logger.debug("add resource:[{}] as container resource", resource);
            }

            for (Resource resource : webInfJarResources) {
                context.getMetaData().addWebInfJar(resource);
                logger.debug("add resource:[{}] as web info jar", resource);
            }

        } catch (IOException e) {
            logger.warn("assemble servlet3 resource due to error", e);
        }   finally {
            containerResources.clear();
            webInfJarResources.clear();
        }

        return context;
    }

}
