package org.byz.easy.web.springboot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author 
 * @since 2020年6月29日
 */
@Component
public class WebInterfaceManage {

	@Autowired
	protected RequestMappingHandlerMapping requestMappingHandlerMapping;
	protected Map<String, WebInterface> mapping;
	
	public WebInterfaceManage(){
		mapping = new HashMap<String, WebInterface>();
	}
	
	public void register(String path) {
		System.out.println(path);
//		requestMappingHandlerMapping.registerMapping();
	}
	
	public void unregister(String name) {
//		requestMappingHandlerMapping.unregisterMapping(mapping);
	}
	
}
