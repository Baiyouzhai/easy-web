package byz.easy.html;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @since 2020年2月6日
 */
@ConfigurationProperties(prefix = "html")
public class HtmlProperties {

	private String manage = "/manage";
	private String view = "/view";

	public HtmlProperties() {
	}

	public HtmlProperties(String manage, String view) {
		this.manage = manage;
		this.view = view;
	}

	public String getManage() {
		return manage;
	}

	public void setManage(String manage) {
		this.manage = manage;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}