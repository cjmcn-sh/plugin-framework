package com.sirius.utils.jetty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pippo on 14-10-21.
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebAppRegistry {

	WebApp[] value();

	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface WebApp {

		String dir();

		String contextPath();

		String[] welcomeFiles() default "index.html";
	}
}
