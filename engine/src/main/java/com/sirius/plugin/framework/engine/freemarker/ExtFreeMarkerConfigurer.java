package com.sirius.plugin.framework.engine.freemarker;

import com.sirius.utils.servlet3.Environment;
import freemarker.ext.jsp.TaglibFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * Created by pippo on 14-3-23.
 */
public class ExtFreeMarkerConfigurer extends FreeMarkerConfigurer {

    private TaglibFactory taglibFactory;


    @Override
    public TaglibFactory getTaglibFactory() {
        if (taglibFactory == null) {
            taglibFactory = new TaglibFactory(Environment.getServletContext());
        }

        return taglibFactory;
    }
}
