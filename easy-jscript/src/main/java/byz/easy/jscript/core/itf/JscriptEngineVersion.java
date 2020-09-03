package byz.easy.jscript.core.itf;

import java.util.List;

import javax.script.ScriptException;

/**
 * 增加包管理
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptEngineVersion extends JscriptEngineWapper {

	/**
	 * 增加版本
	 * 
	 * @param function
	 * @param version
	 * @return
	 */
	JscriptFunction putVersion(JscriptFunction function, String version);

	/**
	 * 获取对应版本
	 * 
	 * @param name
	 * @param version
	 * @return
	 */
	JscriptFunction getVersion(String name, String version);

	/**
	 * 获取多个版本
	 * 
	 * @param name
	 * @return
	 */
	List<JscriptFunction> getVersions(String name);

	/**
	 * 移除指定版本
	 * 
	 * @param function
	 * @return
	 */
	JscriptFunction removeVersion(JscriptFunction function);

	/**
	 * 移除所有版本
	 * 
	 * @param name
	 * @return
	 */
	JscriptFunction removeVersions(String name);

	/**
	 * 移除指定版本
	 * 
	 * @param name
	 * @param version
	 * @return
	 */
	JscriptFunction removeVersion(String name, String version);

	/**
	 * {@link #putVersion(JscriptFunction, String)} -> putVersion(function, "default");
	 */
	@Override
	@Deprecated
	default JscriptFunction putFunction(JscriptFunction function) throws ScriptException {
		return putVersion(function, "default");
	}

	/**
	 * {@link #getVersion(String, String)} -> getVersion(name, "default");
	 */
	@Override
	@Deprecated
	default JscriptFunction getFunction(String name) {
		return getVersion(name, "default");
	}

	/**
	 * {@link #removeVersion(String, String)} -> removeVersion(name, "default");
	 */
	@Override
	@Deprecated
	default JscriptFunction removeFunction(String name) {
		return removeVersion(name, "default");
	}

}
