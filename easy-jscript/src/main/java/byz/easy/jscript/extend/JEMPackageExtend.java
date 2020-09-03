package byz.easy.jscript.extend;

import java.util.Map;

import javax.script.ScriptException;

import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngineWapper;

/**
 * 增加包来修饰 function
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JEMPackageExtend extends JscriptEngineWapper {

	/**
	 * 获取默认版本
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException 
	 */
	public Jscript get(String[] _package, String name) throws ScriptException;

	/**
	 * 获取指定版本
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @return
	 * @throws ScriptException 
	 */
	public default Jscript get(String[] _package, String name, String version) throws ScriptException {
		return get(_package, name, version, new String[0]);
	}

	/**
	 * 获取指定版本
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @param argNames
	 * @return
	 * @throws ScriptException 
	 */
	public Jscript get(String[] _package, String name, String version, String[] argNames) throws ScriptException;

	/**
	 * 获取默认版本
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException 
	 */
	public Jscript get(String _package, String name) throws ScriptException;

	/**
	 * 获取指定版本
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @return
	 * @throws ScriptException 
	 */
	public default Jscript get(String _package, String name, String version) throws ScriptException {
		return get(_package, name, version, new String[0]);
	}

	/**
	 * 获取指定版本
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @param argNames
	 * @return
	 * @throws ScriptException 
	 */
	public Jscript get(String _package, String name, String version, String[] argNames) throws ScriptException;

	/**
	 * 替换指定版本
	 * 
	 * @param _package
	 * @param json
	 * @param version
	 * @return
	 * @throws ScriptException
	 */
	public Object put(String[] _package, Map<String, Object> json, String version) throws ScriptException;

	/**
	 * 替换指定版本
	 * 
	 * @param jscript
	 * @param _package
	 * @param version
	 * @return
	 * @throws ScriptException
	 */
	public Object put(String[] _package, Jscript jscript, String version) throws ScriptException;

	/**
	 * 替换指定版本
	 * 
	 * @param _package
	 * @param json
	 * @param version
	 * @return
	 * @throws ScriptException
	 */
	public Object put(String _package, Map<String, Object> json, String version) throws ScriptException;

	/**
	 * 替换指定版本
	 * 
	 * @param jscript
	 * @param _package
	 * @param version
	 * @return
	 * @throws ScriptException
	 */
	public Object put(String _package, Jscript jscript, String version) throws ScriptException;

	/**
	 * 默认版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public Object run(String[] _package, String name, Object... args) throws NoSuchMethodException, ScriptException;

	/**
	 * 指定版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public default Object run(String[] _package, String name, String version) throws NoSuchMethodException, ScriptException {
		return run(_package, name, version, new String[0], new Object[0]);
	}

	/**
	 * 指定版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @param argNames
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public Object run(String[] _package, String name, String version, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException;

	/**
	 * 默认版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public Object run(String _package, String name, Object... args) throws NoSuchMethodException, ScriptException;

	/**
	 * 指定版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public default Object run(String _package, String name, String version) throws NoSuchMethodException, ScriptException {
		return run(_package, name, version, new String[0], new Object[0]);
	}

	/**
	 * 指定版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param version
	 * @param argNames
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public Object run(String _package, String name, String version, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException;

}
