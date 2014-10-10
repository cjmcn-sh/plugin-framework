package com.sirius.spring;

import com.google.common.collect.ImmutableMap;
import com.sirius.plugin.framework.engine.Constants;
import com.sirius.utils.thread.ThreadContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pippo on 14-10-10.
 */
public class PluginControllerExceptionResolver implements HandlerExceptionResolver, Constants {

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginControllerExceptionResolver.class);

	private FreeMarkerView view;

	public void setView(FreeMarkerView view) {
		this.view = view;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			Exception ex) {

		LOGGER.warn("request:[{}] due to error:[{}]", request.getRequestURI(), ExceptionUtils.getStackTrace(ex));

		String user_id = ThreadContext.get(THREAD_AUDITOR_KEY);

		return new ModelAndView(view, ImmutableMap.<String, Object>builder()
				.put("uri", request.getRequestURI())
				.put("parameters", request.getParameterMap().toString())
				.put("user_id", user_id != null ? user_id : DEFAULT_AUDITOR)
				.put("exception", ex)
				.put("stack", ExceptionUtils.getStackTrace(ex))
				.build());
	}

}
