package byz.easy.jscript.springboot;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author
 * @since 2020年8月23日
 */
public class JscriptEditSourceFilter implements Filter {

	private JscriptApiProperties properties;

	private Set<String> types;

	public JscriptEditSourceFilter(JscriptApiProperties properties) {
		this.properties = properties;
		types = new HashSet<>();
		types.add(".js");
		types.add(".css");
		types.add(".ico");
	}

	protected boolean useEditReferer(HttpServletRequest request) {
		String referer = request.getHeader("referer");
		if (null != referer) {
			String url = request.getRequestURL().toString();
			String editPath = url.substring(0, url.indexOf("/", 8)) + properties.getEditUrl();
			if (!url.startsWith(editPath) && referer.startsWith(editPath)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		if ("GET".equals(req.getMethod())) {
			String uri = req.getRequestURI();
			// /edit /edit/ /edit/index
			if (uri.equals(properties.getEditUrl()) || uri.equals(properties.getEditUrl() + "/") || uri.equals(properties.getEditUrl() + "/index")) {
				rep.sendRedirect(properties.getEditUrl() + "/index.html");
			} else {
				// webpack获取的内容
				if (useEditReferer(req)) {
					rep.sendRedirect(properties.getEditUrl() + uri);
				} else {
					// /other**
					chain.doFilter(request, response);
				}
			}
		} else {
			chain.doFilter(request, response);
		}
	}

}
