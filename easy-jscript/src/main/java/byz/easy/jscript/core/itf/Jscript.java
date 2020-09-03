package byz.easy.jscript.core.itf;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.core.JscriptException;

/**
 * JS引擎执行代码
 * 
 * @author
 * @since
 */
public interface Jscript extends Serializable {

	Map<String, Class<?>> typeMapping = new HashMap<>();

	/**
	 * 获取代码块
	 * 
	 * @return
	 */
	String getCodeBlock();

	void setCodeBlock(String codeBlock);

	/**
	 * 获取函数整体既函数的正式格式
	 * 
	 * @return {@link #getCodeBlock()}
	 */
	default String getRunBody() {
		return getCodeBlock();
	}

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
	default <T extends Jscript> T copy() throws JscriptException {
		try {
			JavaUtil.serializePrepare(this);
			return (T) JavaUtil.ioClone(this);
		} catch (IllegalAccessException | ClassNotFoundException | IOException e) {
			throw new JscriptException("Jscript复制错误 " + e.getMessage(), e);
		}
	}

	/**
	 * 序列成Map
	 * 
	 * @return
	 */
	default Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codeBlock", getCodeBlock());
		return map;
	}

	/**
	 * 通过Map装载属性
	 * 
	 * @param map
	 * @return
	 */
	default void loadMap(Map<String, Object> map) throws JscriptException {
		Object codeBlock = map.get("codeBlock");
		if (!(codeBlock instanceof String))
			throw new JscriptException("codeBlock应为String类型");
		setCodeBlock((String) codeBlock);
	}

}
