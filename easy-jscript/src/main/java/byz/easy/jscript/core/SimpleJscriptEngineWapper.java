package byz.easy.jscript.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import byz.easy.common.LambdaExceptionUtil;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.core.itf.JscriptEngineWapper;
import byz.easy.jscript.core.itf.JscriptFunction;
import byz.easy.jscript.core.nashorn.NashornUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * 
 * @author
 * @since 2020年8月28日
 */
public class SimpleJscriptEngineWapper implements JscriptEngineWapper {

	protected JscriptEngine engine;
	protected Map<String, Object> variables;
	protected Map<String, JscriptFunction> functions;
	protected Map<String, Jscript> aliases;

	public SimpleJscriptEngineWapper() {
		variables = new HashMap<>();
		functions = new HashMap<>();
		aliases = new HashMap<>();
	}

	public SimpleJscriptEngineWapper(String engineName) throws ScriptException {
		this(Utils.buildJscriptEngine(engineName));
	}

	public SimpleJscriptEngineWapper(JscriptEngine engine) throws ScriptException {
		this();
		bindEngine(engine);
	}

	@Override
	public JscriptEngineWapper bindEngine(JscriptEngine engine) throws ScriptException {
		this.engine = engine;
		Set<String> names = engine.getNames();
		if (!names.isEmpty()) {
			for (String name : names) {
				Object object = engine.get(name);
				if (object instanceof ScriptObjectMirror)
					object = NashornUtil.convertScriptObjectMirror(object);
				if (object instanceof JscriptFunction)
					functions.put(name, (JscriptFunction) object);
				else if (object instanceof Jscript)
					aliases.put(name, (Jscript) object);
				else
					variables.put(name, object);
			}
		}
		return this;
	}

	@Override
	public JscriptEngine getJscriptEngine() {
		return engine;
	}

	@Override
	public void setVariables(Map<String, Object> variables) {
		variables.forEach((key, value) -> {
			putVariable(key, value);
		});
	}

	@Override
	public Map<String, Object> getVariables() {
		return variables;
	}

	@Override
	public Object putVariable(String name, Object value) {
		if (value instanceof Jscript)
			throw new JscriptRuntimeException("putVariable(String, Object) 禁用 Jscript");
		if (functions.containsKey(name))
			throw new JscriptRuntimeException("已有函数占用此名称: name -> " + name);
		if (aliases.containsKey(name))
			throw new JscriptRuntimeException("已有别名占用此名称: name -> " + name);
		engine.put(name, value);
		return variables.put(name, value);
	}

	@Override
	public Object getVariable(String name) {
		return variables.get(name);
	}

	@Override
	public Object removeVariable(String name) {
		engine.remove(name);
		return variables.remove(name);
	}

	@Override
	public void setFunctions(Map<String, JscriptFunction> functions) throws ScriptException {
		functions.forEach(LambdaExceptionUtil.applyConsumer((name, function) -> {
			putFunction(function);
		}));
	}

	@Override
	public Map<String, JscriptFunction> getFunctions() {
		return functions;
	}

	@Override
	public JscriptFunction putFunction(JscriptFunction function) throws ScriptException {
		String name = function.getName();
		if (variables.containsKey(name))
			throw new JscriptRuntimeException("已有变量占用此名称: name -> " + name);
		if (aliases.containsKey(name))
			throw new JscriptRuntimeException("已有别名占用此名称: name -> " + name);
		engine.put(function);
		return functions.put(name, function);
	}

	@Override
	public JscriptFunction getFunction(String name) {
		return functions.get(name);
	}

	@Override
	public JscriptFunction removeFunction(String name) {
		engine.remove(name);
		return functions.remove(name);
	}

	@Override
	public void setAliases(Map<String, Jscript> aliases) {
		aliases.forEach((key, value) -> {
			putAlias(key, value);
		});
	}

	@Override
	public Map<String, Jscript> getAliases() {
		return aliases;
	}

	@Override
	public Jscript putAlias(String name, Jscript jscript) {
		if (jscript instanceof JscriptFunction)
			throw new JscriptRuntimeException("putAlias(String, Jscript) 禁用 JscriptFunction");
		if (variables.containsKey(name))
			throw new JscriptRuntimeException("已有变量占用此名称: name -> " + name);
		if (functions.containsKey(name))
			throw new JscriptRuntimeException("已有函数占用此名称: name -> " + name);
		return aliases.put(name, jscript);
	}

	@Override
	public Jscript getJscript(String alias) {
		return aliases.get(alias);
	}

	@Override
	public Set<String> getAlias(Jscript jscript) {
		return aliases.entrySet().stream().filter(entry -> entry.getValue() == jscript).map(Map.Entry::getKey).collect(Collectors.toSet());
	}

	@Override
	public Jscript removeAlias(String name) {
		return aliases.remove(name);
	}

	@Override
	public void removeAliases(Jscript jscript) {
		this.aliases = getAliases().entrySet().stream().filter(entry -> entry.getValue() != jscript).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public SimpleJscriptEngineWapper getTempEngine() throws ScriptException {
		JscriptEngine engine = getJscriptEngine().getTempEngine();
		SimpleJscriptEngineWapper wapper = new SimpleJscriptEngineWapper(engine);
		wapper.setVariables(getVariables());
		wapper.setFunctions(getFunctions());
		wapper.setAliases(getAliases());
		return wapper;
	}

}