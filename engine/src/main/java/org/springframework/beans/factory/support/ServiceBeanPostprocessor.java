///*
// * Copyright © 2010 www.myctu.cn. All rights reserved.
// */
//
//package org.springframework.beans.factory.support;
//
//import com.myctu.platform.AppSettings;
//import com.myctu.platform.gateway.agent.exportera.ServiceExporterImpl;
//import com.myctu.platform.utils.BeanUtils;
//import com.telecom.ctu.platform.framework.engine.service.Exporter;
//import com.telecom.ctu.platform.framework.engine.service.Invoker;
//import com.telecom.ctu.platform.framework.engine.service.SmartServiceInovker;
//import org.apache.commons.lang3.Validate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanCreationException;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//
//import java.lang.reflect.Field;
//
///**
// * User: pippo
// * Date: 13-12-19-13:55
// */
//public class ServiceBeanPostprocessor implements BeanFactoryPostProcessor {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBeanPostprocessor.class);
//
//	@Override
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//		beanFactory.addBeanPostProcessor(new CTUBeanPostProcessor(beanFactory));
//	}
//
//	public static class CTUBeanPostProcessor implements BeanPostProcessor {
//
//		public CTUBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
//			this.beanFactory = (DefaultListableBeanFactory) beanFactory;
//		}
//
//		private DefaultListableBeanFactory beanFactory;
//
//		/*将exporter声明的bean注册到当前context及网关*/
//		@Override
//		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//			Exporter exporter = bean.getClass().getAnnotation(Exporter.class);
//			if (exporter != null) {
//				RootBeanDefinition beanDefinition = (RootBeanDefinition) BeanDefinitionBuilder.rootBeanDefinition(
//						ServiceExporterImpl.class)
//						.setScope(BeanDefinition.SCOPE_SINGLETON)
//						.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO)
//						.setInitMethodName("init")
//						.addPropertyValue("gateway_uri", AppSettings.getInstance().get("ctu.gateway.uri"))
//						.addPropertyValue("service_regist_host",
//								AppSettings.getInstance().get("ctu.gateway.service-regist.host"))
//						.addPropertyValue("service_regist_port", AppSettings.getInstance().getInt(
//								"ctu.gateway.service-regist.port"))
//						.addPropertyValue("regist",
//								AppSettings.getInstance().getBoolean("ctu.gateway.service-regist.auto_regist"))
//						.addPropertyValue("webRoot", AppSettings.getInstance().get("server.http.root"))
//						.addPropertyValue("provider", AppSettings.getInstance().getInt("ctu.app.id"))
//						.addPropertyValue("serviceName", exporter.name())
//						.addPropertyValue("version", exporter.version())
//						.addPropertyValue("serviceInterface", exporter.serviceInterface())
//						.addPropertyReference("service", beanName)
//						.addDependsOn(beanName)
//						.getBeanDefinition();
//
//				beanFactory.createBean(String.format("exporter.%s", beanName), beanDefinition, null);
//				LOGGER.info("regist exporter service:[{}] for bean",
//						String.format("exporter.%s", beanName),
//						bean);
//			}
//
//			return bean;
//		}
//
//		/*为invoker声明的属性注入一个代理对象*/
//		/*smartinvoker会优先在当前context查找是否有可用的bean,如果没有那么通过网关调用*/
//		@Override
//		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//			Field[] fields = BeanUtils.getDeclaredFields(bean.getClass());
//
//			for (Field field : fields) {
//				createInvoker(bean, field);
//			}
//			return bean;
//		}
//
//		private void createInvoker(Object bean, Field field) {
//			Invoker invoker = field.getAnnotation(Invoker.class);
//			if (invoker == null) {
//				return;
//			}
//
//			Validate.isTrue(field.getType() == SmartServiceInovker.class);
//
//			field.setAccessible(true);
//			try {
//				field.set(bean, new SmartServiceInovker(invoker));
//			} catch (IllegalAccessException e) {
//				throw new BeanCreationException(
//						String.format("create invoker:[provider=%s, service=%s, versoin=%s, returnType=%s] due to error",
//								invoker.provider(), invoker.service(), invoker.version(), invoker.returnType()), e
//				);
//			}
//		}
//	}
//
//}
