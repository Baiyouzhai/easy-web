package byz.easy.jscript.extend;

import java.util.List;

import javax.script.ScriptException;

import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptFunction;

/**
 * 增加包来修饰 function
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JEMExtend {
	
	public JEMExtend create() throws ScriptException;

	/**
	 * 替换指定版本
	 * 
	 * @param _package
	 * @param function
	 * @return
	 * @throws ScriptException
	 */
	public Object put(String _package, JscriptFunction function) throws ScriptException;

	/**
	 * 替换指定版本
	 * 
	 * @param _package
	 * @param function
	 * @return
	 * @throws ScriptException
	 */
	public Object put(String[] _package, JscriptFunction function) throws ScriptException;

	/**
	 * 获取对应函数
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException 
	 */
	public default Jscript getFunction(String _package, String name) throws ScriptException {
		return getFunction(_package, name, new String[0]);
	}

	/**
	 * 获取对应函数
	 * 
	 * @param _package
	 * @param name
	 * @param argNames
	 * @return
	 * @throws ScriptException 
	 */
	public Jscript getFunction(String _package, String name, String[] argNames) throws ScriptException;

	/**
	 * 获取对应函数
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException 
	 */
	public default Jscript getFunction(String[] _package, String name) throws ScriptException {
		return getFunction(_package, name, new String[0]);
	}

	/**
	 * 获取对应函数
	 * 
	 * @param _package
	 * @param name
	 * @param argNames
	 * @return
	 * @throws ScriptException 
	 */
	public Jscript getFunction(String[] _package, String name, String[] argNames) throws ScriptException;

	/**
	 * 获取全部函数
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException
	 */
	public List<Jscript> getFunctions(String _package, String name) throws ScriptException;

	/**
	 * 获取全部函数
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException
	 */
	public List<Jscript> getFunctions(String[] _package, String name) throws ScriptException;

	/**
	 * 指定版本运行
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public default Object run(String _package, String name) throws NoSuchMethodException, ScriptException {
		return run(_package, name, new String[0], new Object[0]);
	}

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
	public default Object run(String[] _package, String name) throws NoSuchMethodException, ScriptException {
		return run(_package, name, new String[0], new Object[0]);
	}

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
	 * 指定运行
	 * 
	 * @param _package
	 * @param name
	 * @param argNames
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public default Object run(String[] _package, String name, String[] argNames) throws NoSuchMethodException, ScriptException {
		return run(_package, name, argNames, new Object[0]);
	}

	/**
	 * 指定版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param argNames
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public default Object run(String _package, String name, String[] argNames) throws NoSuchMethodException, ScriptException {
		return run(_package, name, argNames, new Object[0]);
	}

	/**
	 * 指定版本运行
	 * 
	 * @param _package
	 * @param name
	 * @param argNames
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public Object run(String _package, String name, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException;

	/**
	 * 指定运行
	 * 
	 * @param _package
	 * @param name
	 * @param argNames
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public Object run(String[] _package, String name, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException;

}
