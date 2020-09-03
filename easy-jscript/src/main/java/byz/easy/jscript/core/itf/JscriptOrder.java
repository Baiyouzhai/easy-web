package byz.easy.jscript.core.itf;

import java.util.Map;

import byz.easy.jscript.core.JscriptException;

/**
 * JS引擎执行代码
 * 
 * @author
 * @since
 */
public interface JscriptOrder extends Jscript {

	/**
	 * 设置加载顺序
	 * 
	 * @param order
	 */
	void setOrder(long order);

	/**
	 * 加载顺序
	 * 
	 * @return
	 */
	long getOrder();

	/**
	 * 序列成Map
	 * 
	 * @return
	 */
	default Map<String, Object> toMap() {
		Map<String, Object> map = Jscript.super.toMap();
		map.put("order", getOrder());
		return map;
	}

	/**
	 * 通过Map装载属性
	 * 
	 * @param map
	 * @return
	 */
	default void loadMap(Map<String, Object> map) throws JscriptException {
		Object order = map.get("order");
		if (!(order instanceof Long))
			throw new JscriptException("order应为long类型");
		setOrder((long) order);
	}

}
