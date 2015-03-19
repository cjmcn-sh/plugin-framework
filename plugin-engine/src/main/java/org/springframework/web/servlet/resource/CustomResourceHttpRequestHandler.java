package org.springframework.web.servlet.resource;

/**
 * Created by pippo on 15/3/19.
 */
public class CustomResourceHttpRequestHandler extends ResourceHttpRequestHandler {

	public CustomResourceHttpRequestHandler() {
		super();
		setSupportedMethods(METHOD_POST, METHOD_GET, METHOD_HEAD);
	}
}
