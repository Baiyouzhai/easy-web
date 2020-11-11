package org.byz.easy.web.springboot;

import java.util.Date;

import byz.easy.common.VirtualVersion;
import byz.easy.jscript.core.SimpleJscriptFunction;

/**
 * @author 
 * @since 2020年7月17日
 */
public class JSWebFunction extends SimpleJscriptFunction implements VirtualVersion {

	protected int id;
	protected String versionName;
	protected int versionStatus;
	protected int type;
	protected long order;

	public JSWebFunction(String name, String[] paramNames, String codeBlock) throws Exception {
		super(name, paramNames, codeBlock);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getVersionType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public Date createTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date updateTime() {
		// TODO Auto-generated method stub
		return null;
	}

}
