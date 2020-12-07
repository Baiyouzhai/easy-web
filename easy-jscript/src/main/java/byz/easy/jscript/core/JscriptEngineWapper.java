package byz.easy.jscript.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptException;

import byz.easy.common.LambdaUtil;

/**
 * 变量名(variables)与函数名(functions)别名(aliases)区分开使用, 相互互斥</br>
 * 可以给 {@link #Jscript} 下实现类取别名来调用
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptEngineWapper extends JscriptEngine {

	/**
	 * 绑定引擎
	 * 
	 * @param engine 引擎
	 * @return
	 * @throws JscriptException
	 */
	JscriptEngineWapper bindEngine(JscriptEngine engine) throws JscriptException;

	/**
	 * 获取绑定的引擎
	 * 
	 * @return
	 */
	JscriptEngine getJscriptEngine();

	default void setVariables(Map<String, Object> variables) {
		Map<String, Object> temp = getVariables();
		JscriptEngine engine = getJscriptEngine();
		temp.keySet().forEach(name -> engine.remove(name));
//		temp.clear();
		variables.forEach((key, value) -> {
			putVariable(key, value);
		});
	}

	Map<String, Object> getVariables();

	/**
	 * 设置全局变量, 应排除使用了的函数名和别名
	 * 
	 * @param name
	 * @param value
	 * @return
	 * @throws ScriptException
	 */
	default Object putVariable(String name, Object value) {
		removeFunction(name);
		removeAlias(name);
		getJscriptEngine().put(name, value);
		return getVariables().put(name, value);
	}

	/**
	 * 获取全局变量
	 * 
	 * @param name
	 * @return
	 */
	default Object getVariable(String name) {
		return getVariables().get(name);
	}

	/**
	 * 移除全局变量
	 * 
	 * @param name
	 * @return
	 */
	default Object removeVariable(String name) {
		getJscriptEngine().remove(name);
		return getVariables().remove(name);
	}

	default void setFunctions(Collection<JscriptFunction> functions) throws JscriptException {
		Map<String, JscriptFunction> temp = getFunctions();
		temp.keySet().forEach(name -> getJscriptEngine().remove(name));
		temp.clear();
		for (JscriptFunction function : functions)
			putFunction(function);
	}

	Map<String, JscriptFunction> getFunctions();

	/**
	 * 存储函数, JscriptFunction -> name 应排除在函数名和别名之外
	 * 
	 * @param function
	 * @return
	 * @throws JscriptException
	 */
	default JscriptFunction putFunction(JscriptFunction function) throws JscriptException {
		String name = function.getName();
		removeVariable(name);
		removeAlias(name);
		getJscriptEngine().put(function);
		return getFunctions().put(name, function);
	}

	/**
	 * 获取函数
	 * 
	 * @param name
	 * @return
	 */
	default JscriptFunction getFunction(String name) {
		return getFunctions().get(name);
	}

	/**
	 * 移除函数
	 * 
	 * @param name
	 * @return
	 */
	default JscriptFunction removeFunction(String name) {
		getJscriptEngine().remove(name);
		return getFunctions().remove(name);
	}

	/**
	 * 运行函数
	 * 
	 * @param name
	 * @param args
	 * @return
	 * @throws JscriptException
	 */
	default Object runFunction(String name, Object... args) throws JscriptException {
		JscriptFunction jscript = getFunction(name);
		if (null != jscript)
			return getJscriptEngine().run(name, args);
		throw new JscriptException("没有可运行的脚本 name -> " + name);
	}

	default void setAliases(Map<String, Jscript> aliases) throws JscriptException {
		Map<String, Jscript> temp = getAliases();
		temp.clear();
		aliases.forEach(LambdaUtil.apply((name, jscript) -> {
			putAlias(name, jscript);
		}));
	}

	/**
	 * 所有别名
	 * 
	 * @return
	 */
	Map<String, Jscript> getAliases();

	/**
	 * 给 Jscript 取别名方便调用, 应排除使用了的变量名和函数名
	 * 
	 * @param name
	 * @param jscript
	 * @return
	 * @throws JscriptException
	 */
	default Jscript putAlias(String name, Jscript jscript) throws JscriptException {
		removeVariable(name);
		removeFunction(name);
		getJscriptEngine().put(jscript);
		return getAliases().put(name, jscript);
	}

	/**
	 * 通过别名获取 Jscript
	 * 
	 * @param name
	 * @return
	 */
	default Jscript getAlias(String alias) {
		return getAliases().get(alias);
	}

	/**
	 * 获取 Jscript 的别名
	 * 
	 * @param jscript
	 * @return
	 */
	default List<String> getAlias(Jscript jscript) {
		List<String> names = new ArrayList<>();
		getAliases().forEach((name, _jscript) -> {
			if (_jscript == jscript)
				names.add(name);
		});
		return names;
	}

	/**
	 * 移除别名
	 * 
	 * @param name
	 * @return
	 */
	default Jscript removeAlias(String name) {
		return getAliases().remove(name);
	}

	/**
	 * 移除 Jscript 的所有别名
	 * 
	 * @param name
	 * @return
	 */
	default void removeAliases(Jscript jscript) {
		List<String> names = getAlias(jscript);
		for (String name : names)
			getAliases().remove(name);
	}

	/**
	 * 别名运行
	 * 
	 * @param name
	 * @return
	 * @throws JscriptException
	 */
	default Object runAlias(String name) throws JscriptException {
		Jscript jscript = getAlias(name);
		if (null != jscript)
			return getJscriptEngine().execute(jscript.getRunBody());
		throw new JscriptException("没有可运行的脚本 name -> " + name);
	}

	@Override
	@Deprecated
	default String getJscriptEngineName() {
		return getJscriptEngine().getJscriptEngineName();
	}

	@Override
	@Deprecated
	default Object getScriptEngine() {
		return getJscriptEngine().getScriptEngine();
	}

	@Override
	@Deprecated
	default Object execute(String script) throws JscriptException {
		return getJscriptEngine().execute(script);
	}

	@Override
	@Deprecated
	default Object put(String name, Object value) {
		return getJscriptEngine().put(name, value);
	}

	@Override
	@Deprecated
	default Object put(Jscript jscript) throws JscriptException {
		return getJscriptEngine().put(jscript);
	}

	@Override
	@Deprecated
	default Set<String> getNames() {
		return getJscriptEngine().getNames();
	}

	@Override
	@Deprecated
	default Object get(String name) {
		return getJscriptEngine().get(name);
	}

	@Override
	@Deprecated
	default Object remove(String name) {
		return getJscriptEngine().remove(name);
	}

	@Override
	@Deprecated
	default Object run(String name, Object... args) throws JscriptException {
		return getJscriptEngine().run(name, args);
	}

	@Override
	JscriptEngineWapper getTempEngine(boolean hasInitData) throws JscriptException;

}
