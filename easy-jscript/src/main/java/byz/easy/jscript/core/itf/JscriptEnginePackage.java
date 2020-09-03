package byz.easy.jscript.core.itf;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.script.ScriptException;

/**
 * 增加包管理
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptEnginePackage extends JscriptEngineWapper {

	/**
	 * 包路径的拼接串
	 * 
	 * @return
	 */
	default String getPathConcat() {
		return "_";
	}

	/**
	 * 根目录
	 * 
	 * @return default []
	 */
	default String[] getBasePackage() {
		return new String[0];
	}

	/**
	 * 路径
	 * @return Arrays.stream({@link #getBasePackage()}).collect(Collectors.joining({@link #getPathConcat()}));
	 */
	default String getBasePath() {
		return Arrays.stream(getBasePackage()).collect(Collectors.joining(getPathConcat()));
	}

	/**
	 * 生成路径
	 * 
	 * @param _package
	 * @return
	 */
	default String getPath(String[] _package) {
		return Arrays.stream(_package).collect(Collectors.joining(getPathConcat()));
	}

	/**
	 * 存储函数
	 * 
	 * @param _package ["a", "b", "b"] -> "a.b.c"
	 * @param function
	 * @return
	 * @throws ScriptException 
	 */
	JscriptFunction putFunction(String[] _package, JscriptFunction function) throws ScriptException;

	/**
	 * 获取函数
	 * 
	 * @param _package ["a", "b", "b"] -> "a.b.c"
	 * @param name
	 * @return
	 */
	JscriptFunction getFunction(String[] _package, String name);

	/**
	 * 移除函数
	 * 
	 * @param _package ["a", "b", "b"] -> a.b.c
	 * @param name
	 * @return
	 */
	JscriptFunction removeFunction(String[] _package, String name);

	default Object run(String[] _package, String name, Object... args) throws ScriptException, NoSuchMethodException {
		JscriptFunction function = getFunction(_package, name);
		if (null != function) {
			String realName = getPath(_package) + "_" + name;
			return JscriptEngineWapper.super.run(realName, args);
		}
		throw new NoSuchMethodException("没有可运行的脚本: package -> " + getPath(_package) + " -> name -> " + name);
	}

	/**
	 * 添加到默认目录下
	 */
	@Override
	default JscriptFunction putFunction(JscriptFunction function) throws ScriptException {
		return putFunction(getBasePackage(), function);
	}

	/**
	 * 默认目录获取
	 */
	@Override
	default JscriptFunction getFunction(String name) {
		return getFunction(getBasePackage(), name);
	}

	/**
	 * 默认目录移除
	 */
	@Override
	default JscriptFunction removeFunction(String name) {
		return getFunction(getBasePackage(), name);
	}

	@Override
	default Object run(String name, Object... args) throws ScriptException, NoSuchMethodException {
		String realName = getPath(getBasePackage()) + "_" + name;
		return JscriptEngineWapper.super.run(realName, args);
	}

	

}
