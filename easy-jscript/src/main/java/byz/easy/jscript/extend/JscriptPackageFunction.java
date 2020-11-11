package byz.easy.jscript.extend;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptFunction;

/**
 * {@link JscriptFunction} 增加包属性
 * 
 * @author
 * @since 2020年7月24日
 */
public interface JscriptPackageFunction extends JscriptFunction {

	String[] getPackage();

	/**
	 * 包路径
	 * 
	 * @param _package ["a", "b", "b"] -> "a_b_c"
	 */
	void setPackage(String[] _package);

	/**
	 * 函数真实名
	 * 
	 * @return {@link #getPath()} + "_" + {@link #getName()}
	 */
	default String getRealName() {
		return getPath() + "_" + getName();
	}

	/**
	 * 拼接路径
	 * 
	 * @return package = ["a", "b", "b"] -> "a_b_c"</br>
	 *         package = [] -> ""
	 */
	default String getPath() {
		return Arrays.stream(getPackage()).collect(Collectors.joining("_"));
	}

	/**
	 * 默认格式 function [{@link #getRealName()}] ([argNames]) {[codeBlock]}</br>
	 * 例：function a_b_c_test (a, b) {return a + b;}
	 */
	@Override
	default String getRunBody() {
		return new StringBuilder("function ").append(getRealName()).append(" (").append(getArgName()).append(") {").append(getCodeBlock()).append('}').toString();
	}

	@Override
	default Map<String, Object> toMap() {
		Map<String, Object> map = JscriptFunction.super.toMap();
		map.put("package", Arrays.asList(getPackage()));
		return map;
	}

	@Override
	default void loadMap(Map<String, Object> map) throws JscriptException {
		JscriptFunction.super.loadMap(map);

		Object temp = map.get("package");
		String[] _package;
		if (temp instanceof List) {
			List<?> _temp = (List<?>) temp;
			_package = _temp.toArray(new String[_temp.size()]);
		} else if (temp instanceof String[]) {
			_package = (String[]) temp;
		} else {
			throw new JscriptException("package应为List<String>或String[]类型");
		}
		String name = "";
		try {
			for (int i = 0; i < _package.length; i++) {
				name = _package[i];
				JavaUtil.checkPackageName(name);
			}
		} catch (IOException e) {
			throw new JscriptException("包名不符合命名规范package -> " + Arrays.toString(_package) + " -> " + name, e);
		}
		setPackage(_package);
	}

}
