package byz.easy.jscript.core.v8;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

import byz.easy.jscript.core.JscriptException;

/**
 * @author 
 * @since 2020年6月30日
 */
public class V8Util {

	public static V8Object getV8Object(V8 parent, V8Object result, Map<String, Object> map) throws JscriptException {
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			Object value = entry.getValue();
			if (null == value)
				result.addNull(entry.getKey());
			else if (value instanceof Boolean)
				result.add(entry.getKey(), (Boolean) value);
			else if (value instanceof Integer)
				result.add(entry.getKey(), (Integer) value);
			else if (value instanceof Number)
				result.add(entry.getKey(), ((Number) value).doubleValue());
			else if (value instanceof String)
				result.add(entry.getKey(), (String) value);
			else if (value instanceof Map)
				result.add(entry.getKey(), getV8Object(parent, new V8Object(parent), ((Map<String, Object>) value)));
			else if (value instanceof List)
				result.add(entry.getKey(), getV8Object(parent, new V8Array(parent), ((List<Object>) value)));
			else if (value.getClass().isArray())
				result.add(entry.getKey(), getV8Object(parent, new V8Array(parent), (Object[]) value));
			else // 对象
				result.add(entry.getKey(), getV8Object(parent, new V8Object(parent), value));
		}
		return result;
	}

	public static V8Object getV8Object(V8 parent, V8Array array, List<Object> list) throws JscriptException {
		for (Object value : list) {
			if (value instanceof Boolean)
				array.push((Boolean) value);
			else if (value instanceof Integer)
				array.push((Integer) value);
			else if (value instanceof Number)
				array.push(((Number) value).doubleValue());
			else if (value instanceof String)
				array.push((String) value);
			else if (value instanceof Map)
				array.push(getV8Object(parent, new V8Object(parent), ((Map<String, Object>) value)));
			else if (value instanceof List)
				array.push(getV8Object(parent, new V8Array(parent), ((List<Object>) value)));
			else if (value.getClass().isArray())
				array.push(getV8Object(parent, new V8Array(parent), (Object[]) value));
			else // 对象
				array.push(getV8Object(parent, new V8Object(parent), value));
		}
		return array;
	}

	public static V8Object getV8Object(V8 parent, V8Array array, Object[] value) throws JscriptException {
		return getV8Object(parent, array, Arrays.asList(value));
	}

	public static V8Object getV8Object(V8 parent, V8Object json, Object value) throws JscriptException {
		Field[] fields = value.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAccessible())
				field.setAccessible(true);
			try {
				Object _value = field.get(value);
				if (null == _value)
					json.addNull(field.getName());
				else if (_value instanceof Boolean)
					json.add(field.getName(), (Boolean) _value);
				else if (_value instanceof Integer)
					json.add(field.getName(), (Integer) _value);
				else if (_value instanceof Number)
					json.add(field.getName(), ((Number) _value).doubleValue());
				else if (_value instanceof String)
					json.add(field.getName(), (String) _value);
				else if (_value instanceof Map)
					json.add(field.getName(), getV8Object(parent, new V8Object(parent), ((Map<String, Object>) _value)));
				else if (_value instanceof List)
					json.add(field.getName(), getV8Object(parent, new V8Array(parent), ((List<Object>) _value)));
				else if (_value.getClass().isArray())
					json.add(field.getName(), getV8Object(parent, new V8Array(parent), (Object[]) _value));
				else // 对象
					json.add(field.getName(), getV8Object(parent, new V8Object(parent), _value));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				JscriptException _e = new JscriptException("JS内在对象构建失败：" + field.getName() + " -> " + value.toString());
				e.setStackTrace(e.getStackTrace());
				throw _e;
			}
		}
		Method[] methods = value.getClass().getDeclaredMethods();
		for (Method method : methods)
			json.registerJavaMethod(value, method.getName(), method.getName(), method.getParameterTypes());
		return json;
	}

	public static Object converV8Object(Object executeResult) {
		if (executeResult instanceof V8Array) {
			V8Array temp = (V8Array) executeResult;
			List<Object> result = new ArrayList<Object>();
			for (int i = 0; i < temp.length(); i++)
				result.add(converV8Object(temp.get(i)));
			return result;
		} else if (executeResult instanceof V8Object) {
			if (V8Object.class.getDeclaredClasses()[0] == executeResult.getClass()) // V8Object$Undefined
				return null;
			V8Object temp = (V8Object) executeResult;
			Map<String, Object> result = new HashMap<String, Object>();
			String[] keys = temp.getKeys();
			for (String key : keys)
				result.put(key, converV8Object(temp.get(key)));
			return result;
		} else {
			return executeResult;
		}
	}

}
