package byz.easy.jscript.extend;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.itf.JscriptFunction;

/**
 * @author 
 * @since 2020年8月12日
 */
public interface JscriptFunctionExtend extends JscriptFunction, JscriptInitExtend {

	/**
	 * 分类次序 function 200
	 * 
	 * @return
	 */
	@Override
	public default int getOrderType() {
		return 200;
	}

	/**
	 * 默认格式 function [name] ([argNames]) {[codeBlock]}</br>
	 * 例：function test (a, b) {return a + b;}
	 */
	@Override
	public default String getRunBody() {
		return new StringBuilder("function ").append(getName()).append("(").append(Arrays.stream(getArgNames()).collect(Collectors.joining(","))).append(") {").append(getCodeBlock()).append('}').toString();
	}

	@Override
	public default Map<String, Object> toMap() {
		Map<String, Object> map = JscriptInitExtend.super.toMap();
		map.put("name", getName());
		map.put("argNames", Arrays.asList(getArgNames()));
		return map;
	}

	@Override
	public default void loadMap(Map<String, Object> map) throws JscriptException {
		JscriptInitExtend.super.loadMap(map);

		Object _name = map.get("name");
		if (!(_name instanceof String))
			throw new JscriptException("name应为String类型");
		String name = (String) _name;
		try {
			JavaUtil.checkVariableName(name);
		} catch (Exception e) {
			throw new JscriptException("函数名不符合命名规范name -> " + name, e);
		}
		setName(name);

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
		try {
			for (int i = 0; i < argNames.length; i++) {
				name = argNames[i];
				JavaUtil.checkVariableName(name);
			}
		} catch (Exception e) {
			throw new JscriptException("函数的参数名不符合命名规范argNames -> " + Arrays.toString(argNames) + " -> " + name, e);
		}
		setArgNames(argNames);
	}

}
