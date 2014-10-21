package com.sirius.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pippo on 14-10-21.
 */
public class ClassScanner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanner.class);
	protected final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	protected final MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(
			resourcePatternResolver);
	protected final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
	protected final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

	public void addIncludeFilter(TypeFilter filter) {
		includeFilters.add(filter);
	}

	public void addExcludeFilter(TypeFilter filter) {
		excludeFilters.add(filter);
	}

	public void clearIncludeFilter() {
		includeFilters.clear();
	}

	public void clearExcludeFilter(TypeFilter filter) {
		excludeFilters.clear();
	}

	public void scan(String basePackage, Handler handler) {
		scan(basePackage, "**", handler);
	}

	public void scan(String basePackage, String resourcePattern, Handler handler) {

		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
				resolveBasePackage(basePackage) + "/" + resourcePattern;

		try {
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

			for (Resource resource : resources) {
				LOGGER.debug("Scanning {} for webapp", resource);
				if (!resource.isReadable()) {
					continue;
				}

				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				if (!isWebAppRegistry(metadataReader)) {
					continue;
				}

				handler.handle(metadataReader);
			}

		} catch (Exception e) {
			LOGGER.error("scan webapp due tu error", e);
		}

	}

	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(basePackage);
	}

	protected boolean isWebAppRegistry(MetadataReader metadataReader) throws IOException {

		for (TypeFilter tf : excludeFilters) {
			if (tf.match(metadataReader, metadataReaderFactory)) {
				return false;
			}
		}

		for (TypeFilter tf : includeFilters) {
			if (tf.match(metadataReader, metadataReaderFactory)) {
				return true;
			}
		}
		return false;
	}

	public interface Handler {

		void handle(MetadataReader metadataReader);

	}

}
