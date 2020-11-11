package byz.easy.html;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author
 * @since 2020年2月6日
 */
@EnableConfigurationProperties(HtmlProperties.class)
@Order(200)
public class HtmlAutoConfiguration implements CommandLineRunner {

	@Autowired
	private HtmlProperties htmlProperties;

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	private Object registerBean(Class<?> c, String name) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(c);
		beanDefinitionBuilder.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) handlerMapping.getApplicationContext()).getBeanFactory();
		defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getRawBeanDefinition());
		return defaultListableBeanFactory.getBean(name);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Html初始化……");
		long time = System.currentTimeMillis();
		System.out.println("Html注册管理接口……");
		String controllerName = htmlProperties.getHtmlControllerName();
		handlerMapping.registerMapping(
				RequestMappingInfo.paths(htmlProperties.getTurnOn()).build(),
				controllerName,
				ReflectionUtils.findMethod(HtmlController.class, "index", HttpServletRequest.class));
		handlerMapping.registerMapping(
				RequestMappingInfo.paths(htmlProperties.getShutDown()).build(),
				controllerName,
				ReflectionUtils.findMethod(HtmlController.class, "view", HttpServletRequest.class));
		System.out.println("Html初始化完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
	}

}