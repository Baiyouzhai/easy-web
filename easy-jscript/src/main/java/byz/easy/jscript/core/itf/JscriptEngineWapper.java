package byz.easy.jscript.core.itf;

import java.io.Reader;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptRuntimeException;

/**
 * 封装引擎对象</br>
 * variable function aliases 保存创建的初始状态, 构建临时引擎({@link #getTempEngine()})</br>
 * 变量名与函数名别名区分开使用, 相互互斥</br>
 * 可以给 {@link #Jscript} 下实现类取别名来调用, 但是应当要排除 {@link #JscriptFunction} 这个分支
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
	 * @throws ScriptException
	 */
	JscriptEngineWapper bindEngine(JscriptEngine engine) throws ScriptException;

	/**
	 * 获取绑定的引擎
	 * 
	 * @return
	 */
	JscriptEngine getJscriptEngine();

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
		if (value instanceof Jscript)
			throw new JscriptRuntimeException("putVariable(String, Object) 禁用 Jscript");
		if (getFunctions().containsKey(name))
			throw new JscriptRuntimeException("已有函数占用此名称: name -> " + name);
		if (getAliases().containsKey(name))
			throw new JscriptRuntimeException("已有别名占用此名称: name -> " + name);
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
	
	void setFunctions(Map<String, JscriptFunction> functions) throws ScriptException;

	Map<String, JscriptFunction> getFunctions();

	/**
	 * 存储函数, JscriptFunction -> name 应排除在函数名和别名之外
	 * 
	 * @param function
	 * @return
	 * @throws ScriptException
	 */
	default JscriptFunction putFunction(JscriptFunction function) throws ScriptException {
		String name = function.getName();
		if (getVariables().containsKey(name))
			throw new JscriptRuntimeException("已有变量占用此名称: name -> " + name);
		if (getAliases().containsKey(name))
			throw new JscriptRuntimeException("已有别名占用此名称: name -> " + name);
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

	void setAliases(Map<String, Jscript> aliases);

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
	 */
	default Jscript putAlias(String name, Jscript jscript) {
		if (jscript instanceof JscriptFunction)
			throw new JscriptRuntimeException("putAlias(String, Jscript) 禁用 JscriptFunction");
		if (getVariables().containsKey(name))
			throw new JscriptRuntimeException("已有变量占用此名称: name -> " + name);
		if (getFunctions().containsKey(name))
			throw new JscriptRuntimeException("已有函数占用此名称: name -> " + name);
		return getAliases().put(name, jscript);
	}

	/**
	 * 通过别名获取 Jscript
	 * 
	 * @param name
	 * @return
	 */
	default Jscript getJscript(String alias) {
		return getAliases().get(alias);
	}

	/**
	 * 获取 JscriptInit 的别名
	 * 
	 * @param jscript
	 * @return
	 */
	default Set<String> getAlias(Jscript jscript) {
		return getAliases().entrySet().stream().filter(entry -> entry.getValue() == jscript).map(Map.Entry::getKey).collect(Collectors.toSet());
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
	 * 移除 JscriptInit 的所有别名
	 * 
	 * @param name
	 * @return
	 */
	default void removeAliases(Jscript jscript) {
		setAliases(getAliases().entrySet().stream().filter(entry -> entry.getValue() != jscript).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}
	
	void setVariables(Map<String, Object> variables);

	/**
	 * 别名运行
	 * 
	 * @param name
	 * @return
	 * @throws ScriptException
	 * @throws NoSuchMethodException 
	 */
	default Object aliasRun(String name) throws ScriptException, NoSuchMethodException {
		Jscript jscript = getJscript(name);
		if (null != jscript)
			return getJscriptEngine().execute(jscript.getRunBody());
		throw new NoSuchMethodException("没有可运行的脚本 name -> " + name);
	}

	@Override
	default Object getScriptEngine() {
		return getJscriptEngine().getScriptEngine();
	}

	@Override
	@Deprecated
	default Object execute(String script) throws ScriptException {
		return getJscriptEngine().execute(script);
	}

	@Override
	@Deprecated
	default Object execute(Reader reader) throws ScriptException {
		return getJscriptEngine().execute(reader);
	}

	@Override
	default Object put(String name, Object value) {
		return getJscriptEngine().put(name, value);
	}

	@Override
	default Object put(Jscript jscript) throws ScriptException {
		return getJscriptEngine().put(jscript);
	}

	@Override
	default Set<String> getNames() {
		return getJscriptEngine().getNames();
	}

	@Override
	default Object get(String name) {
		return getJscriptEngine().get(name);
	}

	@Override
	default Object remove(String name) {
		return getJscriptEngine().remove(name);
	}

	@Override
	default Object run(String name, Object... args) throws ScriptException, NoSuchMethodException {
		JscriptFunction function = getFunction(name);
		if (null != function)
			return getJscriptEngine().run(name, args);
		throw new NoSuchMethodException("没有可运行的脚本 name -> " + name);
	}

	/**
	 * 还原创建时的数据</br>
	 * 注意: 子类不实现此方法注意要有无参构造
	 */
	@Override
	default JscriptEngineWapper getTempEngine() throws ScriptException {
		try {
			JscriptEngine engine = getJscriptEngine().getTempEngine();
			JscriptEngineWapper wapper = this.getClass().newInstance();
			wapper.bindEngine(engine);
			wapper.setVariables(getVariables());
			wapper.setFunctions(getFunctions());
			wapper.setAliases(getAliases());
			return wapper;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JscriptException("必须有无参构造方法", e);
		}
	}

}
