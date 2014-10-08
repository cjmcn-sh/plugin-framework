package com.sirius.plugin.framework.engine.module;

import com.sirius.plugin.framework.AppSettings;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Created by pippo on 14-9-24.
 */
public class CharEncodingFilterConfig {

	private static final Logger logger = LoggerFactory.getLogger(CharEncodingFilterConfig.class);

	public CharEncodingFilterConfig(ServletContext ctx) {
		/* spring encoding filter */
		/*只有显示声明为false时才会关闭*/
		String enableEncodingFilter = AppSettings.getInstance().get("server.http.encoding.enable");
		String enableEncodingCharset = AppSettings.getInstance().get("server.http.encoding.charset");
		if (!"false".equalsIgnoreCase(enableEncodingFilter)) {
			encodingFilter = ctx.addFilter("encoding", new CharacterEncodingFilter());
			encodingFilter.setInitParameter("encoding",
					StringUtils.isBlank(enableEncodingCharset) ? CharEncoding.UTF_8 : enableEncodingCharset);
			encodingFilter.setInitParameter("forceEncoding", "true");
		}
	}

	protected FilterRegistration encodingFilter;

	public void addMapping(ServletRegistration servletRegistration) {
		for (String pattern : servletRegistration.getMappings()) {
			addMapping(pattern);
		}
	}

	public void addMapping(String... urlPattern) {
		if (encodingFilter == null) {
			return;
		}

		logger.info("enable encoding filter filter for url pattern:[{}]", Arrays.toString(urlPattern));
		encodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, urlPattern);
	}

}
