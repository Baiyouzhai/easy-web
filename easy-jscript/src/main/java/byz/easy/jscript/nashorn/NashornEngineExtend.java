package byz.easy.jscript.nashorn;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptFunction;
import byz.easy.jscript.core.JscriptRuntimeException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * 检入/检出 ScriptObjectMirror 类型数据
 * 
 * @author
 * @since 2019年12月19日
 */
public class NashornEngineExtend extends NashornEngine {

	protected Map<Integer, Jscript> jscripts; // Jscript.codeBlock.hashCode() - Jscript
	protected Map<String, JscriptFunction> functions;

	public NashornEngineExtend() {
		this(new ConcurrentHashMap<String, Object>());
	}

	/**
	 * @param initVariables 初始化全局变量
	 * @throws ScriptException
	 */
	public NashornEngineExtend(Map<String, Object> initVariables) {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		compilable = (Compilable) engine;
		jscripts = new ConcurrentHashMap<>();
		functions = new ConcurrentHashMap<>();
		Set<Entry<String, Object>> entrySet = initVariables.entrySet();
		for (Entry<String, Object> entry : entrySet)
			put(entry.getKey(), entry.getValue());
	}

	@Override
	public Object put(String name, Object value) {
		Object _value = value;
		try {
			if (_value instanceof ScriptObjectMirror)
				_value = NashornUtil.convertScriptObjectMirror(_value);
			if (_value instanceof JscriptFunction) {
				JscriptFunction temp = (JscriptFunction) _value;
				if (!temp.getName().equals(name)) {
					temp = temp.copy();
					temp.setName(name);
				}
				_value = put(temp);
			} else if (_value instanceof Jscript) {
				_value = put((Jscript) _value);
			} else {
				_value = engine.get(name);
				engine.put(name, value);
			}
			return _value;
		} catch (ScriptException e) {
			throw new JscriptRuntimeException("编译错误: " + e.getMessage(), e);
		}
	}

	@Override
	public Object put(Jscript jscript) throws JscriptException {
		int runHash = jscript.getRunBody().hashCode();
		Jscript old = jscripts.put(runHash, jscript);
		if (jscript instanceof JscriptFunction) {
			JscriptFunction temp = (JscriptFunction) jscript;
			functions.put(temp.getName(), temp);
		}
		try {
			compilable.compile(jscript.getRunBody()).eval();
			return old;
		} catch (ScriptException e) {
			throw new JscriptException(e.getMessage(), e);
		}
	}

	@Override
	public Object get(String name) {
		Object value = functions.get(name);
		if (null == value)
			value = engine.get(name);
		try {
			if (value instanceof ScriptObjectMirror)
				value = NashornUtil.convertScriptObjectMirror(value);
			if (value instanceof Jscript)
				value = put((Jscript) value);
			return value;
		} catch (ScriptException e) {
			throw new JscriptRuntimeException("编译错误：转换 ScriptObjectMirror 失败 -> " + value, e);
		}
	}

	@Override
	public Object remove(String name) {
		Object value = engine.get(name);
		if (value instanceof ScriptObjectMirror)
			value = functions.remove(name);
		try {
			compilable.compile(name + " = undefined;").eval();
		} catch (ScriptException e) {
			throw new JscriptRuntimeException("编译错误" + e.getMessage(), e);
		}
		return value;
	}

	@Override
	public Object run(String name, Object... args) throws JscriptException {
		Object jscript = get(name);
		if (jscript instanceof JscriptFunction)
			try {
				return ((Invocable) engine).invokeFunction(name, args);
			} catch (ScriptException e) {
				throw new JscriptException("脚本运行错误：\n\t" + e.getMessage(), e);
			} catch (NoSuchMethodException e) {
			}
		throw new JscriptException("没有可运行的脚本 name -> " + name);
	}

}