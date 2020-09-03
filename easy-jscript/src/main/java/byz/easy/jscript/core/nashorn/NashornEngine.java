package byz.easy.jscript.core.nashorn;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import byz.easy.jscript.core.JscriptRuntimeException;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngine;

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
		typeMapping.put("nashorn", NashornEngine.class);
	}

	protected ScriptEngine engine;
	protected Compilable compilable;

	public NashornEngine() {
		this(new HashMap<String, Object>());
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
	public Object getScriptEngine() {
		return engine;
	}

	@Override
	@Deprecated
	public Object execute(String script) throws ScriptException {
		return compilable.compile(script).eval();
	}

	@Override
	@Deprecated
	public Object execute(Reader reader) throws ScriptException {
		return compilable.compile(reader).eval();
	}

	@Override
	public Object put(String name, Object value) {
		Object old = engine.get(name);
		engine.put(name, value);
		return old;
	}

	@Override
	public Object put(Jscript jscript) throws ScriptException {
		return compilable.compile(jscript.getRunBody()).eval();
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
	public Object run(String name, Object... args) throws ScriptException, NoSuchMethodException {
		return ((Invocable) engine).invokeFunction(name, args);
	}

	/**
	 * 只含有 {@link #NashornEngine(Map)} initVariables 中的值
	 */
	@Override
	public NashornEngine getTempEngine() throws ScriptException {
		NashornEngine tempEngine = new NashornEngine();
		tempEngine.engine.setBindings(engine.getBindings(ScriptContext.GLOBAL_SCOPE), ScriptContext.GLOBAL_SCOPE);
		return tempEngine;
	}

}