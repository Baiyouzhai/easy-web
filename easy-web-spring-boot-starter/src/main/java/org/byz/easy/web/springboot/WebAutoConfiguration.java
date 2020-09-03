package org.byz.easy.web.springboot;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import byz.easy.database.DatabaseManage;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.springboot.JscriptProperties;

/**
 * @author
 * @since 2019年12月23日
 */
@Configuration
@EnableConfigurationProperties(JscriptProperties.class)
@Order(Integer.MAX_VALUE - 100)
public class WebAutoConfiguration implements CommandLineRunner {

	@Autowired
	private JscriptProperties jscriptProperties;

	@Autowired(required = false)
	private DatabaseManage databaseManage;

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Bean
	public JscriptEngine getJscriptEngine() throws JscriptException {
		System.out.println("Jscript创建引擎……");
		long time = System.currentTimeMillis();
		JscriptEngine jscriptEngine = null;
		Map<String, Object> engineParams = new HashMap<String, Object>();
		System.out.println("Jscript创建引擎完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
		return jscriptEngine;
	}

	@Bean
	public JscriptManage getJscriptManage(JscriptEngine jscriptEngine) {
		System.out.println("Jscript创建管理器……");
		long time = System.currentTimeMillis();
		JscriptManage jscriptManage = new JscriptManage(jscriptEngine);
		System.out.println("Jscript创建管理器完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
		return jscriptManage;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Jscript组件加载……");
		long time = System.currentTimeMillis();
//		if (jscriptProperties.getWeb().getOpen()) { // 注册接口
//			System.out.println("Jscript注册管理接口……");
//			requestMappingHandlerMapping.registerMapping(
//					RequestMappingInfo.paths(jscriptProperties.getWeb().getRequestUrl() + "/{name}").build(),
//					"jscriptController",
//					ReflectionUtils.findMethod(JscriptController.class, "useScript", String.class, Map.class));
//			requestMappingHandlerMapping.registerMapping(
//					RequestMappingInfo.paths(jscriptProperties.getWeb().getManageUrl() + "/addScript").build(),
//					"jscriptController",
//					ReflectionUtils.findMethod(JscriptController.class, "addScript", Map.class));
//		} else {
//		}
//		if (jscriptProperties.getDatabase().getOpen()) {
//			System.out.println("Jscript加载数据库存储接口数据……");
//		} else {
//			System.out.println("Jscript加载本地存储的接口文件……");
//		}
		System.out.println("Jscript组件加载完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
	}

}