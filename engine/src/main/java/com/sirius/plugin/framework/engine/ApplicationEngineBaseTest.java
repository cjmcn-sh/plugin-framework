/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.plugin.framework.engine;

import com.sirius.spring.ApplicationContextLauncher;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: pippo
 * Date: 13-12-18-15:37
 */
@ContextConfiguration(locations = {
        "classpath*:META-INF/ctu.application.main.context.xml",
		"classpath*:META-INF/application.test.context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationEngineBaseTest extends ApplicationContextLauncher {


}
