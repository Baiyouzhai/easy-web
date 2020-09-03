package org.byz.easy.web.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @since 2019年12月19日
 */
@ConfigurationProperties(prefix = "jscript.api.cors")
public class JscriptApiCorsProperties {

	protected String[] origins = new String[] {"*"};
	protected String[] methods = new String[] {"GET", "POST", "OPTIONS", "HEAD", "PUT", "PATCH", "DELETE"};
	protected String[] headers = new String[] {"*"};
	protected boolean credentials = true;
	protected String[] exposedHeaders = new String[0];
	protected long maxAge = 86400;

	public String[] getOrigins() {
		return origins;
	}
	public void setOrigins(String[] origins) {
		this.origins = origins;
	}
	public String[] getMethods() {
		return methods;
	}
	public void setMethods(String[] methods) {
		this.methods = methods;
	}
	public String[] getHeaders() {
		return headers;
	}
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	public boolean getCredentials() {
		return credentials;
	}
	public void setCredentials(boolean credentials) {
		this.credentials = credentials;
	}
	public String[] getExposedHeaders() {
		return exposedHeaders;
	}
	public void setExposedHeaders(String[] exposedHeaders) {
		this.exposedHeaders = exposedHeaders;
	}
	public long getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(long maxAge) {
		this.maxAge = maxAge;
	}

}