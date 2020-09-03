package byz.easy.jscript.core.itf;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.core.JscriptException;

/**
 * 统一包和重写管理
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptEngineManage2 extends JscriptEnginePackage {

	static boolean checkPackagePath(String[] _package) throws ScriptException {
		try {
			for (String temp : _package)
				JavaUtil.checkFolderName(temp);
		} catch (IOException e) {
			throw new JscriptException("文件夹名错误 _package -> " + e.getMessage(), e);
		}
		return true;
	}

	JscriptEngineManage2 create() throws ScriptException;

	/**
	 * 替换指定包下内容
	 * 
	 * @param _package
	 * @param name
	 * @param argNames
	 * @param jscript
	 * @return
	 * @throws ScriptException
	 */
	Jscript put(String[] _package, String name, String[] argNames, Jscript jscript) throws ScriptException;

	/**
	 * 替换指定包下内容
	 * 
	 * @param _package 格式为 a_b_c
	 * @param name
	 * @param jscript
	 * @return
	 * @throws ScriptException
	 */
	Jscript put(String _package, String name, Jscript jscript) throws ScriptException;

	/**
	 * 获取对应内容
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException
	 */
	default Jscript get(String[] _package, String name) throws ScriptException {
		checkPackagePath(_package);
		return get(Arrays.stream(_package).collect(Collectors.joining("_")), name);
	}

	/**
	 * 获取对应内容
	 * 
	 * @param _package 格式为 a_b_c
	 * @param name
	 * @return
	 * @throws ScriptException
	 */
	Jscript get(String _package, String name);

	/**
	 * 移除对应内容
	 * 
	 * @param _package
	 * @param name
	 * @return
	 * @throws ScriptException
	 */
	Jscript remove(String[] _package, String name) throws ScriptException;

	/**
	 * 移除对应内容
	 * 
	 * @param _package 格式为 a_b_c
	 * @param name
	 * @return
	 * @throws ScriptException
	 */
	Jscript remove(String _package, String name) throws ScriptException;

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
	default Object run(String[] _package, String name) throws NoSuchMethodException, ScriptException {
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
	Object run(String[] _package, String name, Object... args) throws NoSuchMethodException, ScriptException;

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
	default Object run(String[] _package, String name, String[] argNames) throws NoSuchMethodException, ScriptException {
		return run(_package, name, argNames, new Object[0]);
	}

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
	Object run(String[] _package, String name, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException;

}
