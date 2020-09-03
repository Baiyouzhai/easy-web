package byz.easy.jscript.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @since 2019年12月19日
 */
@ConfigurationProperties(prefix = "jscript")
public class JscriptProperties {

	protected String engineName = "nashorn";
	protected String savePath = "jscript";
	protected boolean useManage = false;
	protected JscriptApiProperties api;

	public JscriptProperties() {
	}

	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public boolean getUseManage() {
		return useManage;
	}

	public void setUseManage(boolean useManage) {
		this.useManage = useManage;
	}

	public JscriptApiProperties getApi() {
		return api;
	}

	public void setApi(JscriptApiProperties api) {
		this.api = api;
	}

}