package byz.easy.jscript.core;

/**
 * Jscript资源文件(包装)</br>
 * 注意几个变量的说明:</br>
 * @see JscriptSource#status
 */
public class JscriptSource implements Comparable<JscriptSource> {

	protected String packAge; // 对应脚本所在的包
	protected String version; // 对应脚本的版本
	protected String name; // 对应资源名称
	protected Jscript jscript; // 对应脚本内容
	/**
	 * 
	 * 关联于脚本加载到引擎(Engine)中的顺序, 默认使用从小到大排序</br>
	 * 默认使用当前时间的毫秒数({@link System#currentTimeMillis()}), 若手动指定需要明确加载顺序</br>
	 * 如果加载的脚本有相同内容(相同变量的重复赋值, 相同函数的重复定义), 加载靠前的内容会被覆盖导致失效
	 */
	protected long order; // 对应脚本的加载时间(顺序)
	/**
	 * <table border="1">
	 * <tr><td>编码</td><td>状态</td><td>说明</td></tr>
	 * <tr><td>0</td><td>默认</td><td>不会加载到引擎当中(Engine), 可见, 可编辑</td></tr>
	 * <tr><td>1</td><td>加载</td><td>会被加载到引擎当中(加载后修改脚本无效)</td></tr>
	 * <tr><td>编码</td><td>状态</td><td>说明</td></tr>
	 * </table>
	 */
	protected int status; // 加载状态

	public JscriptSource(String name, Jscript jscript) {
		this(name, jscript, System.currentTimeMillis());
	}

	public JscriptSource(String name, Jscript jscript, long order) {
		this.name = name;
		this.jscript = jscript;
		this.order = order;
	}

	public JscriptSource(String packAge, String version, String name, Jscript jscript, long order, int status) {
		this.packAge = packAge;
		this.version = version;
		this.name = name;
		this.jscript = jscript;
		this.order = order;
		this.status = status;
	}

	public String getPackAge() {
		return packAge;
	}

	public void setPackAge(String packAge) {
		this.packAge = packAge;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Jscript getJscript() {
		return jscript;
	}

	public void setJscript(Jscript jscript) {
		this.jscript = jscript;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

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
	 * 重载 {@link #equals(Object)} 若使用原始方法, 将参数强转Object
	 * 
	 * @param jscriptSource
	 * @return
	 */
	public boolean equals(JscriptSource jscriptSource) {
		if (this == jscriptSource)
			return true;
		if (null == jscriptSource) 
			return false;
		if (!((null == packAge) ^ (null != jscriptSource.packAge)))
			return false;
		if (!((null == version) ^ (null != jscriptSource.version)))
			return false;
		if (status != jscriptSource.status)
			return false;
		if (!((null == name) ^ (null != jscriptSource.name))) // 一边为空一边不为空
			return false;
		if (!((null == jscript) ^ (null != jscriptSource.jscript)))
			return false;
		if (order != jscriptSource.order)
			return false;
		if (null != name && !name.equals(jscriptSource.name))
			return false;
		if (null != jscript && !jscript.equals(jscriptSource.jscript))
			return false;
		return true;
	}

	@Override
	public int compareTo(JscriptSource obj) {
		if (null == obj)
			return -1;
		if (order == obj.order)
			return 0;
		return order < obj.order ? -1 : 1;
	}

}
