package byz.easy.jscript.extend;

import java.util.Map;

import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.SimpleJscriptFunction;

/**
 * @author 
 * @since 2020年8月12日
 */
public class SimpleJscriptFunctionExtend extends SimpleJscriptFunction implements JscriptFunctionExtend {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String version;
	protected int versionStatus;
	protected int versionType;
	protected int orderType;
	protected long orderNumber;

	@Override
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}

	@Override
	public int getVersionType() {
		return versionType;
	}

	public void setVersionType(int versionType) {
		this.versionType = versionType;
	}

	@Override
	public long getOrder() {
		return getOrderNumber();
	}

	@Override
	public void setOrder(long order) {
		setOrderNumber(order);
	}

	@Override
	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	@Override
	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public void loadMap(Map<String, Object> map) throws JscriptException {
		super.loadMap(map);
		this.orderType = (int) map.get("orderType");
		this.orderNumber = (long) map.get("orderNumber");
		this.version = (String) map.get("version");
		this.versionType = (int) map.get("versionType");
		this.versionStatus = (int) map.get("versionStatus");
	}

}
