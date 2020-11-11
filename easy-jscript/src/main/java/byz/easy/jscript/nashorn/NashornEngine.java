package byz.easy.jscript.nashorn;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptEngine;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptRuntimeException;

/**
 * {@link JscriptEngine} 的简单实现
 * 
 * @author
 * @since 2019年12月19日
 */
public class NashornEngine implements JscriptEngine {

	static {
		// System.setProperty("Dnashorn.args", "--language=es6");
		System.setProperty("nashorn.args", "--language=es6");
	}

	protected ScriptEngine engine;
	protected Compilable compilable;

	public NashornEngine() {
		this(new ConcurrentHashMap<String, Object>());
	}

	/**
	 * @param initVariables 初始化全局变量
	 * @throws ScriptException
	 */
	public NashornEngine(Map<String, Object> initVariables) {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		compilable = (Compilable) engine;
		engine.getBindings(ScriptContext.GLOBAL_SCOPE).putAll(initVariables);
	}

	@Override
	public String getJscriptEngineName() {
		return "nashorn";
	}

	@Override
	public Object getScriptEngine() {
		return engine;
	}

	@Override
	@Deprecated
	public Object execute(String script) throws JscriptException {
		try {
			return compilable.compile(script).eval();
		} catch (ScriptException e) {
			throw new JscriptException(e.getMessage(), e);
		}
	}

	@Override
	public Object put(String name, Object value) {
		Object old = engine.get(name);
		engine.put(name, value);
		return old;
	}

	@Override
	public Object put(Jscript jscript) throws JscriptException {
		try {
			return compilable.compile(jscript.getRunBody()).eval();
		} catch (ScriptException e) {
			throw new JscriptException(e.getMessage(), e);
		}
	}

	@Override
	public Set<String> getNames() {
		return engine.getBindings(ScriptContext.ENGINE_SCOPE).keySet();
	}

	@Override
	public Object get(String name) {
		return engine.get(name);
	}

	@Override
	public Object remove(String name) {
		Object value = engine.get(name);
		if (null != value) {
			try {
				compilable.compile(name + " = undefined;").eval();
			} catch (ScriptException e) {
				throw new JscriptRuntimeException("编译错误 -> " + name + " = undefined;", e);
			}
		}
		return value;
	}

	@Override
	public Object run(String name, Object... args) throws JscriptException {
		try {
			return ((Invocable) engine).invokeFunction(name, args);
		} catch (ScriptException e) {
			throw new JscriptException("脚本运行错误：\n\t" + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new JscriptException("没有可运行的脚本 name -> " + name, e);
		}
	}

	@Override
	public NashornEngine getTempEngine(boolean hasInitData) throws JscriptException {
		NashornEngine tempEngine = new NashornEngine();
		if (hasInitData) {
			Bindings initVariables = engine.getBindings(ScriptContext.GLOBAL_SCOPE);
			tempEngine.engine.setBindings(initVariables, ScriptContext.GLOBAL_SCOPE);
		}
		return tempEngine;
	}

}