package byz.easy.html;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @since 2020年2月6日
 */
@ConfigurationProperties(prefix = "html")
public class HtmlProperties {

	protected String htmlControllerName = "htmlController";
	protected String turnOn = "/manage/turnOn";
	protected String shutDown = "/manage/shutDown";

	public String getHtmlControllerName() {
		return htmlControllerName;
	}
	public void setHtmlControllerName(String htmlControllerName) {
		this.htmlControllerName = htmlControllerName;
	}
	public String getTurnOn() {
		return turnOn;
	}
	public void setTurnOn(String turnOn) {
		this.turnOn = turnOn;
	}
	public String getShutDown() {
		return shutDown;
	}
	public void setShutDown(String shutDown) {
		this.shutDown = shutDown;
	}

}