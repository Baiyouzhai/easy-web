package byz.easy.jscript.core.v8;

import java.io.Reader;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptException;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8ObjectUtils;

import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptRuntimeException;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.core.v8.V8Util;

/**
 * {@link JscriptEngine} 的简单实现
 * 
 * @author
 * @since 2019年12月19日
 */
public class V8Engine implements JscriptEngine {

	protected V8 engine;
	protected Map<String, Jscript> functions;
	
	static {
		typeMapping.put("v8", V8Engine.class);
	}

	public V8Engine() throws JscriptException {
		this(null);
	}

	public V8Engine(Map<String, Object> engineParams) throws JscriptException {
		engine = V8.createV8Runtime(); //TODO 等待完成
		functions = new ConcurrentHashMap<String, Jscript>();
		if (null != engineParams) {
			Set<Entry<String, Object>> entrySet = engineParams.entrySet();
			for (Entry<String, Object> entry : entrySet)
				put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Object getScriptEngine() {
		return engine;
	}

	@Override
	public Set<String> getNames() {
		return null;
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
				return engine.add(name, V8Util.getV8Object(engine, new V8Object(engine), ((Map<String, Object>) value)));
			} else if (value instanceof List) {
				return engine.add(name, V8Util.getV8Object(engine, new V8Array(engine), ((List<Object>) value)));
			} else if (value.getClass().isArray()) {
				return engine.add(name, V8Util.getV8Object(engine, new V8Array(engine), (Object[]) value));
			} else { // 对象
				return engine.add(name, V8Util.getV8Object(engine, new V8Object(engine), value));
			}
		} catch (JscriptException e) {
			JscriptRuntimeException _e = new JscriptRuntimeException("转换错误 " + e.getMessage());
			e.setStackTrace(e.getStackTrace());
			throw _e;
		}
	}

	@Override
	public Object put(Jscript jscript) throws ScriptException {
		return null; //TODO 等待完成
	}

	@Override
	public Object get(String name) {
		return engine.get(name);
	}
	@Override
	public Object remove(String name) {
		return null; //TODO 等待完成
	}

	@Override
	@Deprecated
	public Object execute(String script) throws JscriptException {
		return null; //TODO 等待完成
	}

	@Override
	@Deprecated
	public Object execute(Reader reader) throws JscriptException {
		return null; //TODO 等待完成
	}

	@Override
	public Object run(String name, Object... args) throws JscriptException {
		if (null != args) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof Map) {
					args[i] = V8ObjectUtils.toV8Object(engine, (Map<String, ? extends Object>) args[i]);
				} else if (args[i] instanceof Array) {
					args[i] = V8ObjectUtils.toV8Array(engine, (List<? extends Object>) args[i]);
				}
			}
		}
		return V8Util.converV8Object(engine.executeJSFunction(name, args));
	}

	@Override
	public V8Engine getTempEngine() throws JscriptException {
		return new V8Engine(null); //TODO 等待完成
	}

}