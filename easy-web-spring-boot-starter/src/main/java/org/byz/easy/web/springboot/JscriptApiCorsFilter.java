package org.byz.easy.web.springboot;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import byz.easy.jscript.springboot.JscriptProperties;

/**
 * @author
 * @since 2020年8月23日
 */
public class JscriptApiCorsFilter implements Filter {

	private Map<String, Map<String, String>> mapping;

	public static Map<String, String> toMap(JscriptApiCorsProperties properties) {
		Map<String, String> temp = new HashMap<String, String>();
		temp.put("origin", Arrays.stream(properties.getOrigins()).collect(Collectors.joining(", ")));
		temp.put("methods", Arrays.stream(properties.getMethods()).collect(Collectors.joining(", ")));
		temp.put("headers", Arrays.stream(properties.getHeaders()).collect(Collectors.joining(", ")));
		temp.put("credentials", properties.getCredentials() + "");
		temp.put("maxAge", properties.getMaxAge() + "");
		return temp;
	}

	public static Map<String, Map<String, String>> buildMapping(JscriptProperties properties) {
		Map<String, Map<String, String>> mapping = new HashMap<>();
//		if (null != properties.getApi()) {
//			JscriptApiProperties api = properties.getApi();
//			Map<List<String>, JscriptApiCorsProperties> map = api.getUseCors();
//			map.forEach((key, value) -> {
//				Map<String, String> temp = toMap(value);
//				key.forEach(path -> {
//					mapping.put(api.getUseUrl() + path, temp);
//				});
//			});
//			JscriptApiCorsProperties cors = api.getManageCors();
//			if (null != cors) {
//				Map<String, String> temp = toMap(cors);
//				Arrays.asList(api.getAddUrl(), api.getModifyUrl(), api.getUpdateUrl(), api.getDeleteUrl(), api.getSaveUrl(), api.getDownloadUrl()).forEach(item -> {
//					mapping.put(item, temp);
//				});
//			}
//		}
		return mapping;
	}

	public JscriptApiCorsFilter(JscriptProperties properties) {
		mapping = buildMapping(properties);
		System.out.println(mapping.toString());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		Map<String, String> map = mapping.get(req.getRequestURI());
		if (null != map) {
			HttpServletResponse rep = (HttpServletResponse) response;
			rep.setHeader("Access-Control-Allow-Origin", map.get("origin"));
			rep.setHeader("Access-Control-Allow-Methods", map.get("methods"));
			rep.setHeader("Access-Control-Allow-Headers", map.get("headers"));
			rep.setHeader("Access-Control-Allow-Credentials", map.get("credentials"));
			rep.setHeader("Access-Control-Max-Age", map.get("maxAge"));
		}
		chain.doFilter(request, response);
	}

}
