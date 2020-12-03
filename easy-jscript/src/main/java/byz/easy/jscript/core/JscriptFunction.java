package byz.easy.jscript.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对应js当中的函数块</br>
 * 包含 function <b>name</b> ([<b>argNames</b>]) {<b>codeBlock</b>}</br>
 * <b>name</b> String 函数名</br>
 * <b>argNames</b> String[] 参数</br>
 * <b>codeBlock</b> String 代码块
 * 
 * @author
 * @since 2020年7月24日
 */
public interface JscriptFunction extends JscriptInit {

	/**
	 * 获取函数名
	 * 
	 * @return
	 */
	String getName();

	void setName(String name) throws JscriptException;

	/**
	 * 获取参数名列表
	 * 
	 * @return
	 */
	String[] getArgNames();

	void setArgNames(String[] argNames) throws JscriptException;

	/**
	 * 参数拼接
	 * 
	 * @return argNames = ["a", "b", "b"] -> "a, b, c"</br>
	 *         argNames = [] -> ""
	 */
	default String getArgName() {
		return Arrays.stream(getArgNames()).collect(Collectors.joining(", "));
	}
	
	@Override
	default String getType() {
		return "function";
	}

	/**
	 * @return 默认格式 function <b>name</b> ({@link #getArgName()})
	 *         {<b>codeBlock</b>}</br>
	 *         例：function test (a, b) {return a + b;}
	 */
	@Override
	default String getRunBody() {
		return new StringBuilder("function ").append(getName()).append(" (").append(getArgName()).append(") {").append(getCodeBlock()).append('}').toString();
	}

	@Override
	default JscriptFunction copy() throws JscriptException {
		return (JscriptFunction) JscriptInit.super.copy();
	}

	@Override
	default Map<String, Object> toMap() {
		Map<String, Object> map = JscriptInit.super.toMap();
//		map.put("type", getType());
		map.put("name", getName());
		map.put("argNames", Arrays.asList(getArgNames()));
		return map;
	}

	@Override
	default void loadMap(Map<String, Object> map) throws JscriptException {
		JscriptInit.super.loadMap(map);

		Object _name = map.get("name");
		if (!(_name instanceof String))
			throw new JscriptException("name应为String类型");
		setName((String) _name);

		Object _argNames = map.get("argNames");
		String[] argNames;
		if (_argNames instanceof List) {
			List<?> temp = (List<?>) _argNames;
			argNames = temp.toArray(new String[temp.size()]);
		} else if (_argNames instanceof String[]) {
			argNames = (String[]) _argNames;
		} else {
			throw new JscriptException("argNames应为List<String>或String[]类型");
		}
		setArgNames(argNames);
	}

	/**
	 * 重写 {@link JscriptInit#equals(Jscript, boolean)} 部分规则
	 * @param jscript
	 * @param ignore 忽略函数名, 参数个数, 参数名 是否相同
	 */
	@Override
	default boolean equals(Jscript jscript, boolean ignore) {
		if (this == jscript)
			return true;
		if (jscript instanceof JscriptFunction) {
			String thisName = getName();
			String targetName = ((JscriptFunction) jscript).getName();
		}
		String thisRunBody = getRunBody();
		if (null == thisRunBody)
			thisRunBody = "";
		if (ignore && "".equals(thisRunBody) && null == jscript)
			return true;
		String targetRunBody = null == jscript ? "" : jscript.getRunBody();
		return JscriptInit.super.equals(jscript, ignore);
	}

}
