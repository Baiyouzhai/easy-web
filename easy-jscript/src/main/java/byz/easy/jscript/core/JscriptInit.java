package byz.easy.jscript.core;

import java.util.HashMap;
import java.util.Map;

/**
 * JS引擎中作为初始化使用的代码块
 * 
 * @author
 * @since 2019年12月20日
 */
public interface JscriptInit extends Jscript {

	/**
	 * 获取代码块
	 * 
	 * @return
	 */
	String getCodeBlock();

	void setCodeBlock(String codeBlock);

	@Override
	default String getRunBody() {
		return getCodeBlock();
	}

	@Override
	default JscriptInit copy() throws JscriptException {
		return (JscriptInit) Jscript.super.copy();
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