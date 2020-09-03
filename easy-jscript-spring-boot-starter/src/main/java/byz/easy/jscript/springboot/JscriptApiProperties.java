package byz.easy.jscript.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author
 * @since 2019年12月19日
 */
@ConfigurationProperties(prefix = "jscript.api")
public class JscriptApiProperties {

	protected String apiControllerName = "jscriptApiController";
	protected boolean apiHelp = false;
	protected String useUrl = "/use";
	protected String addUrl = "/manage/add";
	protected String modifyUrl = "/manage/modify";
	protected String updateUrl = "/manage/refresh";
	protected String deleteUrl = "/manage/delete";
	protected String saveUrl = "/manage/save";
	protected String downloadUrl = "/download";
	protected boolean openEdit = false;
	protected String editControllerName = "jscriptEditController";
	protected String editResourcePath = "/";
	protected String editUrl = "/edit";

	public String getApiControllerName() {
		return apiControllerName;
	}

	public void setApiControllerName(String apiControllerName) {
		this.apiControllerName = apiControllerName;
	}

	public boolean getApiHelp() {
		return apiHelp;
	}

	public void setApiHelp(boolean apiHelp) {
		this.apiHelp = apiHelp;
	}

	public String getUseUrl() {
		return useUrl;
	}

	public void setUseUrl(String useUrl) {
		this.useUrl = useUrl;
	}

	public String getAddUrl() {
		return addUrl;
	}

	public void setAddUrl(String addUrl) {
		this.addUrl = addUrl;
	}

	public String getModifyUrl() {
		return modifyUrl;
	}

	public void setModifyUrl(String modifyUrl) {
		this.modifyUrl = modifyUrl;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public boolean getOpenEdit() {
		return openEdit;
	}

	public void setOpenEdit(boolean openEdit) {
		this.openEdit = openEdit;
	}

	public String getEditControllerName() {
		return editControllerName;
	}

	public void setEditControllerName(String editControllerName) {
		this.editControllerName = editControllerName;
	}

	public String getEditResourcePath() {
		return editResourcePath;
	}

	public void setEditResourcePath(String editResourcePath) {
		this.editResourcePath = editResourcePath;
	}

	public String getEditUrl() {
		return editUrl;
	}

	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}

}