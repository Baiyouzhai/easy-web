package byz.easy.jscript.springboot;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptException;

import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.core.itf.JscriptEngineManage;
import byz.easy.jscript.core.nashorn.NashornEngine;
import byz.easy.jscript.core.v8.V8Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author
 * @since 2019年12月23日
 */
@EnableConfigurationProperties(JscriptProperties.class)
public class JscriptAutoConfiguration implements WebMvcConfigurer, CommandLineRunner {

	@Autowired
	private JscriptProperties jscriptProperties;

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	@ConditionalOnMissingBean
	@Bean
	public JscriptEngine getJscriptEngine() throws ClassNotFoundException, ScriptException, IOException {
		System.out.println("Jscript引擎创建……");
		long time = System.currentTimeMillis();
		JscriptEngine jscriptEngine = null;
		String engineName = jscriptProperties.getEngineName();
		if ("v8".equalsIgnoreCase(engineName)) {
			jscriptEngine = new V8Engine();
		} else if ("nashorn".equalsIgnoreCase(engineName)) {
			jscriptEngine = new NashornEngine();
		}
		System.out.println("Jscript引擎创建完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
		return jscriptEngine;
	}

	@ConditionalOnProperty(prefix = "jscript", name = "use-manage", havingValue = "true")
	@ConditionalOnMissingBean
	@Bean(initMethod = "create")
	public JscriptEngineManage getJscriptManage(JscriptEngine engine) throws ScriptException {
		System.out.println("Jscript管理器创建……");
		long time = System.currentTimeMillis();
		File file = new File(jscriptProperties.getSavePath());
		if (!file.exists())
			file.mkdir();
		JscriptManage manage = null == engine ? new JscriptManage(jscriptProperties) : new JscriptManage(engine, jscriptProperties);
		System.out.println("Jscript管理器创建完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
		return manage;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (null != jscriptProperties && null != jscriptProperties.getApi() && jscriptProperties.getApi().getOpenEdit()) {
			System.out.println("注册JscriptEdit资源……");
			long _time = System.currentTimeMillis();
			registry.addResourceHandler(jscriptProperties.getApi().getEditUrl() + "/**").addResourceLocations(jscriptProperties.getApi().getEditResourcePath())
					.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
			System.out.println("注册JscriptEdit资源完成，用時" + (System.currentTimeMillis() - _time) / 1000.0 + "s");
		}
	}

	@ConditionalOnProperty(prefix = "jscript.api", name = "open-edit", havingValue = "true")
	@Bean
	public FilterRegistrationBean<?> registerJscriptEditSourceFilter() {
		System.out.println("注册JscriptEdit资源过滤器……");
		long time = System.currentTimeMillis();
		FilterRegistrationBean<JscriptEditSourceFilter> registration = new FilterRegistrationBean<>();
		registration.setName("jscriptEditSourceFilter");
		registration.setFilter(new JscriptEditSourceFilter(jscriptProperties.getApi()));
		registration.addUrlPatterns("/*");
		System.out.println("注册JscriptEdit资源过滤器完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
		return registration;
	}

	private Object registerBean(Class<?> c, String name) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(c);
		beanDefinitionBuilder.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) handlerMapping.getApplicationContext()).getBeanFactory();
		defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getRawBeanDefinition());
		return defaultListableBeanFactory.getBean(name);
	}

	public void run(String... args) throws Exception {
		if (null != jscriptProperties.getApi()) {
			JscriptApiProperties apiProperties = jscriptProperties.getApi();
			System.out.println("注册JscriptAPI接口……");
			long time = System.currentTimeMillis();
			String controllerName = apiProperties.getApiControllerName();
			Object handler = registerBean(JscriptApiController.class, controllerName);
			if (jscriptProperties.getUseManage()) {
				handlerMapping.registerMapping(RequestMappingInfo.paths(apiProperties.getUseUrl() + "/{name}").build(), handler, ReflectionUtils.findMethod(JscriptApiController.class, "manageUse", String.class, Map.class));
				handlerMapping.registerMapping(RequestMappingInfo.paths(apiProperties.getAddUrl()).build(), handler, ReflectionUtils.findMethod(JscriptApiController.class, "manageAdd", Map.class));
			} else {
				handlerMapping.registerMapping(RequestMappingInfo.paths(apiProperties.getUseUrl() + "/{name}").build(), handler, ReflectionUtils.findMethod(JscriptApiController.class, "engineUse", String.class, Map.class));
				handlerMapping.registerMapping(RequestMappingInfo.paths(apiProperties.getAddUrl()).build(), handler, ReflectionUtils.findMethod(JscriptApiController.class, "engineAdd", Map.class));
			}
			if (apiProperties.getOpenEdit()) {
				controllerName = apiProperties.getEditControllerName();
				handler = registerBean(JscriptEditController.class, controllerName);
				handlerMapping.registerMapping(RequestMappingInfo.paths(apiProperties.getEditUrl() + "/getProperties").build(), handler, ReflectionUtils.findMethod(JscriptEditController.class, "getProperties"));
			}
			System.out.println("注册JscriptAPI接口完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
		}
	}

}