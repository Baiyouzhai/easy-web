package byz.easy.jscript.core.itf;

import java.util.List;

import javax.script.ScriptException;

/**
 * 增加重写管理
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptEngineOverride extends JscriptEngineWapper {

	/**
	 * 增加重载
	 * 
	 * @param function
	 * @return
	 */
	JscriptFunction putOverride(JscriptFunction function);

	/**
	 * 获取指定重载
	 * 
	 * @param name
	 * @param argNames
	 * @return
	 */
	JscriptFunction getOverride(String name, String[] argNames);

	/**
	 * 获取重载
	 * 
	 * @param name
	 * @return
	 */
	List<JscriptFunction> getOverrides(String name);

	/**
	 * 移除指定重载
	 * 
	 * @param function
	 * @return
	 */
	JscriptFunction removeOverride(JscriptFunction function);

	/**
	 * 移除所有重载
	 * 
	 * @param name
	 * @return
	 */
	JscriptFunction removeOverrides(String name);

	/**
	 * 移除指定重载
	 * 
	 * @param name
	 * @param argNames
	 * @return
	 */
	JscriptFunction removeOverride(String name, String[] argNames);

	/**
	 * {@link #putOverride(JscriptFunction)}
	 */
	@Override
	@Deprecated
	default JscriptFunction putFunction(JscriptFunction function) throws ScriptException {
		return putOverride(function);
	}

	/**
	 * {@link #getOverride(String, String[])} -> getOverride(name, new String[0]);
	 */
	@Override
	@Deprecated
	default JscriptFunction getFunction(String name) {
		return getOverride(name, new String[0]);
	}

	/**
	 * {@link #removeOverride(String)} -> removeOverride(name);
	 */
	@Override
	@Deprecated
	default JscriptFunction removeFunction(String name) {
		return removeOverride(name, new String[0]);
	}

}
