package org.byz.easy.web.springboot;

import java.lang.reflect.Method;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * @author 
 * @since 2020年6月29日
 */
public class WebInterface {

	protected RequestMappingInfo mapping;
	protected Object handler;
	protected Method method;

	public RequestMappingInfo getMapping() {
		return mapping;
	}
	public void setMapping(RequestMappingInfo mapping) {
		this.mapping = mapping;
	}
	public Object getHandler() {
		return handler;
	}
	public void setHandler(Object handler) {
		this.handler = handler;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}

}
