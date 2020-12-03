package byz.easy.jscript.core;

import java.util.Map;

/**
 * JS引擎中直接使用内容的匿名内容, 有返回
 * 
 * @author
 * @since 2019年12月20日
 */
public interface JscriptRun extends JscriptInit {

	/**
	 * 赋值给
	 * 
	 * @return
	 */
	default String getReturnTo() {
		return "";
	}

	/**
	 * 设置返回值赋给的变量名
	 * 
	 * @param returnTo
	 * @throws JscriptException
	 */
	void setReturnTo(String returnTo) throws JscriptException;

	@Override
	default String getType() {
		return "run";
	}

	/**
	 * @return 默认格式</br>
	 *         returnTo = null -> (function () {{@link #getCodeBlock()}})()</br>
	 *         returnTo != null -> <b>returnTo</b> = function () {{@link #getCodeBlock()}}()
	 */
	@Override
	default String getRunBody() {
		String name = getReturnTo();
		if ("".equals(name)) {
			return new StringBuilder("(function () {").append(getCodeBlock()).append("})()").toString();
		} else {
			return new StringBuilder(getReturnTo()).append(" = ").append(" function () {").append(getCodeBlock()).append("}()").toString();
		}
	}

	@Override
	default JscriptRun copy() throws JscriptException {
		return (JscriptRun) JscriptInit.super.copy();
	}

	@Override
	default Map<String, Object> toMap() {
		Map<String, Object> map = JscriptInit.super.toMap();
//		map.put("type", getType());
		map.put("returnTo", getReturnTo());
		return map;
	}

	@Override
	default void loadMap(Map<String, Object> map) throws JscriptException {
		JscriptInit.super.loadMap(map);

		Object returnTo = map.get("returnTo");
		if (null == returnTo)
			returnTo = "";
		if (!(returnTo instanceof String))
			throw new JscriptException("returnTo应为String类型");
		setReturnTo((String) returnTo);
	}

	/**
	 * 重写 {@link JscriptInit#equals(Jscript, boolean)} 部分规则</br>
	 * 新增:</br>
	 * 1.同类优先比对 {@link #getCodeBlock()} 是否相同(应该能快一点)
	 * 
	 * @param jscript
	 * @param ignore 
	 * @return
	 */
	@Override
	default boolean equals(Jscript jscript, boolean ignore) {
		return JscriptInit.super.equals(jscript, ignore);
	}

}