package byz.easy.jscript.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

import byz.easy.common.JavaUtil;

/**
 * JS引擎执行代码
 * 
 * @author
 * @since
 */
public interface Jscript extends Serializable {

	/**
	 * 脚本类型
	 * 
	 * @return
	 */
	default String getType() {
		return "js";
	}

	/**
	 * 获取脚本(函数)整体, 既脚本(函数)的正式格式
	 */
	String getRunBody();

	/**
	 * 格式最后调整
	 * 
	 * @param callBack
	 * @return
	 */
	default String getRunBody(Function<String, String> callBack) {
		return callBack.apply(getRunBody());
	}

	/**
	 * 复制成一个全新对象，不过运行环境将会丢失，需要重新绑定
	 * 
	 * @return
	 * @throws JscriptException
	 */
	default Jscript copy() throws JscriptException {
		try {
			JavaUtil.serializePrepare(this);
			return JavaUtil.ioClone(this);
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new JscriptException("Jscript复制错误 " + e.getMessage(), e);
		}
	}

	/**
	 * 序列成Map
	 * 
	 * @return
	 */
	Map<String, Object> toMap();

	/**
	 * 通过Map装载属性
	 * 
	 * @param map
	 * @return
	 * @throws JscriptException 
	 */
	void loadMap(Map<String, Object> map) throws JscriptException;
	
	/**
	 * 重载equals, 默认ignore=false
	 * 
	 * @param jscript
	 * @return
	 * @see #equals(Jscript, boolean)
	 */
	default boolean equals(Jscript jscript) {
		return equals(jscript, false);
	}

	/**
	 * 重载equals
	 * 
	 * @param jscript
	 * @param ignore 忽略空对象和内容 {@link #getRunBody()} 是否相同(null | "" 忽略)
	 * @return
	 */
	default boolean equals(Jscript jscript, boolean ignore) {
		if (this == jscript)
			return true;
		String thisRunBody = getRunBody();
		String targetRunBody = null;
		if (null != jscript)
			targetRunBody = jscript.getRunBody();
		if (ignore) {
			targetRunBody = targetRunBody == null ? "" : jscript.getRunBody();
			return (null == thisRunBody ? "" : thisRunBody).equals(null == targetRunBody ? "" : targetRunBody);
		}
		if (null != thisRunBody)
			return thisRunBody.equals(targetRunBody);
		if (null != targetRunBody)
			return targetRunBody.equals(thisRunBody);
		return true;
	}

}
