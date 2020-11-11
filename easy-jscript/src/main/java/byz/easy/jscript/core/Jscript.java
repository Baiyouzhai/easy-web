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
	 * 获取函数整体既函数的正式格式
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

}
