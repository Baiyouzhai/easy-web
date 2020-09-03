package byz.easy.jscript.core.nashorn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import byz.easy.jscript.core.JscriptRuntimeException;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptFunction;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * 检入/检出 ScriptObjectMirror 类型数据
 * 
 * @author
 * @since 2019年12月19日
 */
public class NashornEngineExtend extends NashornEngine {

	static {
		typeMapping.put("nashornExtend", NashornEngineExtend.class);
	}

	protected Map<Integer, Jscript> jscripts; // Jscript.codeBlock.hashCode() - Jscript
	protected Map<String, JscriptFunction> functions;

	public NashornEngineExtend() {
		this(new HashMap<String, Object>());
	}

	/**
	 * @param initVariables 初始化全局变量
	 * @throws ScriptException
	 */
	public NashornEngineExtend(Map<String, Object> initVariables) {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		compilable = (Compilable) engine;
		jscripts = new HashMap<>();
		functions = new HashMap<>();
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
	public Object put(Jscript jscript) throws ScriptException {
		int runHash = jscript.getRunBody().hashCode();
		Jscript old = jscripts.put(runHash, jscript);
		if (jscript instanceof JscriptFunction) {
			JscriptFunction temp = (JscriptFunction) jscript;
			functions.put(temp.getName(), temp);
		}
		compilable.compile(jscript.getRunBody()).eval();
		return old;
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
	public Object run(String name, Object... args) throws ScriptException, NoSuchMethodException {
		Object jscript = get(name);
		if (jscript instanceof JscriptFunction)
			return ((Invocable) engine).invokeFunction(name, args);
		throw new NoSuchMethodException("没有可运行的脚本 name -> " + name);
	}

}