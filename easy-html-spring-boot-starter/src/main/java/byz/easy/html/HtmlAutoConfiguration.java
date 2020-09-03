package byz.easy.html;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author
 * @since 2020年2月6日
 */
@Configuration
@EnableConfigurationProperties(HtmlProperties.class)
@Order(102)
public class HtmlAutoConfiguration implements CommandLineRunner {

	@Autowired
	private HtmlProperties htmlProperties;

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Html初始化……");
		long time = System.currentTimeMillis();
		System.out.println("Html注册管理接口……");
		requestMappingHandlerMapping.registerMapping(
				RequestMappingInfo.paths(htmlProperties.getManage(), htmlProperties.getManage() + "/", htmlProperties.getManage() + "/index").build(),
				"htmlController",
				ReflectionUtils.findMethod(HtmlController.class, "index", HttpServletRequest.class));
		requestMappingHandlerMapping.registerMapping(
				RequestMappingInfo.paths(htmlProperties.getManage() + "/template").build(),
				"htmlController",
				ReflectionUtils.findMethod(HtmlController.class, "template", HttpServletRequest.class));
		requestMappingHandlerMapping.registerMapping(
				RequestMappingInfo.paths(htmlProperties.getView() + "/view").build(),
				"htmlController",
				ReflectionUtils.findMethod(HtmlController.class, "view", HttpServletRequest.class));
		System.out.println("Html初始化完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
	}

}