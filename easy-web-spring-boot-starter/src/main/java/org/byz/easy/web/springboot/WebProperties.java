package org.byz.easy.web.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 
 * @since 2020年7月17日
 */
@ConfigurationProperties(prefix = "web")
public class WebProperties {

	private String manageUrl;

}
