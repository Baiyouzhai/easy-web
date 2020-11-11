package org.byz.easy.web.springboot;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * @author 
 * @since 2020年6月29日
 */
public class WebUtil {

	public RequestMappingInfo buildRequestMappingInfo(String... paths) {
		return RequestMappingInfo.paths(paths).build();
	}

}
