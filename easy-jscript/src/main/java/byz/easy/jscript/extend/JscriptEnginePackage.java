package byz.easy.jscript.extend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptEngine;
import byz.easy.jscript.core.JscriptEngineWapper;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptFunction;

/**
 * 包管理
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptEnginePackage extends JscriptEngineWapper {

	/**
	 * 默认包
	 * 
	 * @return
	 */
	default String[] getBasePackage() {
		return new String[0];
	}

	/**
	 * 包路径的拼接串
	 * 
	 * @return
	 */
	default String getPathConcat() {
		return "_";
	}

	/**
	 * 包路径的拼接串
	 * 
	 * @return
	 */
	default String getNameConcat() {
		return "$";
	}

	/**
	 * 忽略大小写
	 * 
	 * @return
	 */
	default boolean ignoreCase() {
		return true;
	}

	/**
	 * 路径映射
	 * 
	 * @param _package
	 * @return
	 */
	default String getPath(String... _package) {
		Stream<String> stream = Arrays.stream(null == _package ? new String[0] : _package);
		if (ignoreCase())
			stream = stream.map(item -> item.toLowerCase());
		return stream.collect(Collectors.joining(getPathConcat()));
	}

	/**
	 * 真实映射
	 * 
	 * @param name
	 * @param _package
	 * @return
	 */
	default String getRealName(String name, String... _package) {
		return getPath(_package) + getNameConcat() + name;
	}

	/**
	 * String realName - Jscript jscript
	 * 
	 * @return
	 */
	Map<String, Jscript> getMapping();

	/**
	 * 存储指定
	 * 
	 * @param name
	 * @param _package
	 * @param jscript
	 * @return
	 */
	default Jscript packagePut(String name, Jscript jscript, String... _package) {
		String realName = getRealName(name, _package);
		return getMapping().put(realName, jscript);
	}

	/**
	 * 获取指定
	 * 
	 * @param name
	 * @param _package
	 * @return
	 */
	default Jscript packageGet(String name, String... _package) {
		String realName = getRealName(name, _package);
		return getMapping().get(realName);
	}

	/**
	 * 移除指定
	 * 
	 * @param name
	 * @param _package
	 * @return
	 */
	default Jscript packageRemove(String name, String... _package) {
		String realName = getRealName(name, _package);
		return getMapping().remove(realName);
	}

	/**
	 * 移除整个包
	 * 
	 * @param _package
	 * @return
	 */
	default List<Jscript> packageRemove(String... _package) {
		String path = getPath(_package);
		List<String> names = new ArrayList<>();
		List<Jscript> jscripts = new ArrayList<>();
		getMapping().forEach((realName, jscript) -> {
			if (realName.contains(path)) {
				names.add(realName);
				jscripts.add(jscript);
			}
		});
		for (String realName : names)
			getMapping().remove(realName);
		return jscripts;
	}

	@Override
	default JscriptEngineWapper bindEngine(JscriptEngine engine) throws JscriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default JscriptEngine getJscriptEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Object get(String name) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.get(name);
	}

	@Override
	default Object putJscript(Jscript jscript) throws JscriptException {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.putJscript(jscript);
	}

	@Override
	default Map<String, Object> getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Object putVariable(String name, Object value) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.putVariable(name, value);
	}

	@Override
	default Object getVariable(String name) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.getVariable(name);
	}

	@Override
	default Object removeVariable(String name) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.removeVariable(name);
	}

	@Override
	default void setFunctions(Map<String, JscriptFunction> functions) throws JscriptException {
		// TODO Auto-generated method stub
		
	}

	@Override
	default Map<String, JscriptFunction> getFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default JscriptFunction putFunction(JscriptFunction function) throws JscriptException {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.putFunction(function);
	}

	@Override
	default JscriptFunction getFunction(String name) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.getFunction(name);
	}

	@Override
	default JscriptFunction removeFunction(String name) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.removeFunction(name);
	}

	@Override
	default void setAliases(Map<String, Jscript> aliases) {
		// TODO Auto-generated method stub
		
	}

	@Override
	default Map<String, Jscript> getAliases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Jscript putAlias(String name, Jscript jscript) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.putAlias(name, jscript);
	}

	@Override
	default Jscript getAlias(String alias) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.getAlias(alias);
	}

	@Override
	default Set<String> getAlias(Jscript jscript) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.getAlias(jscript);
	}

	@Override
	default Jscript removeAlias(String name) {
		// TODO Auto-generated method stub
		return JscriptEngineWapper.super.removeAlias(name);
	}

	@Override
	default void removeAliases(Jscript jscript) {
		JscriptEngineWapper.super.removeAliases(jscript);
	}

	@Override
	default Object runAlias(String name) throws JscriptException {
		return JscriptEngineWapper.super.runAlias(getRealName(name, getBasePackage()));
	}

	@Override
	default Object runFunction(String name, Object... args) throws JscriptException {
		return JscriptEngineWapper.super.runFunction(getRealName(name, getBasePackage()), args);
	}

}
