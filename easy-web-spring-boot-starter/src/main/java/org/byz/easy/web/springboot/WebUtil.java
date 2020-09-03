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
	
	public static void main(String[] args) {
		String a = "ssssss.sss";
		int index = a.lastIndexOf(".");
		System.out.println(a.substring(0, index));
		System.out.println(a.substring(index));
	}

}
