package byz.easy.jscript.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import byz.easy.common.LambdaUtil;
import byz.easy.jscript.nashorn.NashornUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * 
 * @author
 * @since 2020年8月28日
 */
public class StandardJscriptEngineWapper implements JscriptEngineWapper {

	protected JscriptEngine engine;
	protected Map<String, Object> variables;
	protected Map<String, JscriptFunction> functions;
	protected Map<String, Jscript> aliases;

	public StandardJscriptEngineWapper(JscriptEngine engine) throws JscriptException {
		variables = new ConcurrentHashMap<>();
		functions = new ConcurrentHashMap<>();
		aliases = new ConcurrentHashMap<>();
		bindEngine(engine);
	}

	@Override
	public JscriptEngineWapper bindEngine(JscriptEngine engine) throws JscriptException {
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
	public void setFunctions(Collection<JscriptFunction> functions) throws JscriptException {
		functions.forEach(LambdaUtil.apply(function -> {
			putFunction(function);
		}));
	}

	@Override
	public Map<String, JscriptFunction> getFunctions() {
		return functions;
	}

	@Override
	public JscriptFunction putFunction(JscriptFunction function) throws JscriptException {
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
		try {
			engine.put(jscript);
		} catch (JscriptException e) {
			throw new JscriptRuntimeException(e);
		}
		return aliases.put(name, jscript);
	}

	@Override
	public Jscript getAlias(String alias) {
		return aliases.get(alias);
	}

	@Override
	public List<String> getAlias(Jscript jscript) {
		List<String> names = new ArrayList<>();
		getAliases().forEach((name, _jscript) -> {
			if (_jscript == jscript)
				names.add(name);
		});
		return names;
	}

	@Override
	public Jscript removeAlias(String name) {
		return aliases.remove(name);
	}

	@Override
	public void removeAliases(Jscript jscript) {
		List<String> names = getAlias(jscript);
		for (String name : names)
			aliases.remove(name);
	}

	@Override
	public JscriptEngineWapper getTempEngine(boolean hasInitData) throws JscriptException {
		JscriptEngine _engine = engine.getTempEngine(hasInitData);
		JscriptEngineWapper wapper = new StandardJscriptEngineWapper(_engine);
		wapper.setAliases(aliases);
		wapper.setFunctions(functions.values());
		wapper.setVariables(variables);
		return wapper;
	}

}