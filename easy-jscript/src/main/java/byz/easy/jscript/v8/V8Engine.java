package byz.easy.jscript.v8;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

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
public class V8Engine implements JscriptEngine {

	protected V8 engine;
	protected Map<String, Object> initVariables;

	public V8Engine() {
		this(null);
	}

	public V8Engine(Map<String, Object> initVariables) {
		engine = V8.createV8Runtime(); //TODO 需要优化线程
		this.initVariables = initVariables;
		if (null != initVariables)
			initVariables.forEach((key, value) -> {
				put(key, value);
			});
	}

	@Override
	public String getJscriptEngineName() {
		return "v8";
	}

	@Override
	public Object getScriptEngine() {
		return engine;
	}

	@Override
	public Set<String> getNames() {
		return Arrays.stream(engine.getKeys()).collect(Collectors.toSet());
	}

	@Override
	public Object put(String name, Object value) {
		try {
			if (null == value) {
				return engine.addNull(name);
			} else if (value instanceof Boolean) {
				return engine.add(name, (Boolean) value);
			} else if (value instanceof Integer) {
				return engine.add(name, (Integer) value);
			} else if (value instanceof Number) {
				return engine.add(name, ((Number) value).doubleValue());
			} else if (value instanceof String) {
				return engine.add(name, (String) value);
			} else if (value instanceof Map) {
				return engine.add(name, V8Util.getV8Object(engine, new V8Object(engine), ((Map<String, Object>) value), false));
			} else if (value instanceof List) {
				return engine.add(name, V8Util.getV8Object(engine, new V8Array(engine), ((List<Object>) value), false));
			} else if (value.getClass().isArray()) {
				return engine.add(name, V8Util.getV8Object(engine, new V8Array(engine), (Object[]) value, false));
			} else if (value instanceof Jscript) { // 对象
				return put((Jscript) value);
			} else { // 对象
				return engine.add(name, V8Util.getV8Object(engine, new V8Object(engine), value, false));
			}
		} catch (JscriptException e) {
			throw new JscriptRuntimeException("转换错误 " + e.getMessage(), e);
		}
	}

	@Override
	public Object put(Jscript jscript) throws JscriptException {
		return engine.executeScript(jscript.getRunBody());
	}

	@Override
	public Object get(String name) {
		return V8Util.converV8Object(engine.get(name));
	}

	@Override
	public Object remove(String name) {
		Object old = engine.get(name);
		if (null != old)
			engine.executeScript(name + " = undefined;");
		return old;
	}

	@Override
	@Deprecated
	public Object execute(String script) throws JscriptException {
		return engine.executeScript(script);
	}

	@Override
	public Object run(String name, Object... args) throws JscriptException {
		if (null != args) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof Map) {
					args[i] = V8Util.getV8Object(engine, new V8Object(engine), args[i], false);
				} else if (args[i] instanceof List) {
					V8Util.getV8Object(engine, new V8Array(engine), (List<? extends Object>) args[i], false);
				} else if (args[i].getClass().isArray()) {
					V8Util.getV8Object(engine, new V8Array(engine), (Object[]) args[i], false);
				}
			}
		}
		return V8Util.converV8Object(engine.executeJSFunction(name, args));
	}

	@Override
	public V8Engine getTempEngine(boolean hasInitData) throws JscriptException {
		return hasInitData ? new V8Engine(initVariables) : new V8Engine();
	}

}