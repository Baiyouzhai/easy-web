package byz.easy.jscript;

import byz.easy.common.VirtualVersion;
import byz.easy.jscript.core.itf.Jscript;

/**
 * @author 
 * @since 2020年8月1日
 */
public class JscriptVersion implements VirtualVersion {

	protected String name;
	/**
	 * <table border="1">
	 * <tr><td>属性</td><td>值</td><td>说明</td></tr>
	 * <tr><td>status</td><td>0</td><td>停用，不加载</td></tr>
	 * <tr><td>status</td><td>1</td><td>启用，加载</td></tr>
	 * </table>
	 */
	protected int status;
	/**
	 * 前置条件 status=1
	 * <table border="1">
	 * <tr><td>属性</td><td>值</td><td>说明</td></tr>
	 * <tr><td>type</td><td>0</td><td>默认，版本默认项</td></tr>
	 * <tr><td>type</td><td>1</td><td>可用，版本可选项</td></tr>
	 * </table>
	 */
	protected int type;
	protected Jscript jscript;

	/**
	 * @param status {@link #status}
	 * @param type {@link #type}
	 */
	public JscriptVersion(String name, int status, int type) {
		this(name, type, status, null);
	}

	/**
	 * @param status {@link #status}
	 * @param type {@link #type}
	 */
	public JscriptVersion(String name, int status, int type, Jscript jscript) {
		this.name = name;
		this.status = status;
		this.type = type;
		this.jscript = jscript;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see #status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status {@link #status}
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @see #type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type {@link #type}
	 */
	public void setType(int type) {
		this.type = type;
	}

	public Jscript get() {
		return jscript;
	}

	public void set(Jscript jscript) {
		this.jscript = jscript;
	}

	@Override
	public String getVersion() {
		return name;
	}

	@Override
	public int getVersionType() {
		return type;
	}

	@Override
	public int getVersionStatus() {
		return status;
	}

}
