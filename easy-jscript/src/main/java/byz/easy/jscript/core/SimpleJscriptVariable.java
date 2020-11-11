package byz.easy.jscript.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import byz.easy.common.JavaUtil;

/**
 * JS引擎执行代码
 * 
 * @author
 * @since
 */
public class SimpleJscriptVariable implements JscriptVariable {

	/**
	 */
	private static final long serialVersionUID = 1L;

	protected String name;
	protected Object value;

	public SimpleJscriptVariable() {
	}

	public SimpleJscriptVariable(String name, Object value) throws JscriptException {
		setName(name);
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws JscriptException {
		try {
			JavaUtil.checkVariableName(name);
		} catch (Exception e) {
			throw new JscriptException("变量名不符合规范: name -> " + name, e);
		}
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String getRunBody() {
		try {
			return new StringBuilder("var ").append(getName()).append(" = ").append(JscriptFileUtil.getJsonUtil().toString(value)).append(';').toString();
		} catch (IOException e) {
			throw new JscriptRuntimeException(e);
		}
	}

	@Override
	public SimpleJscriptVariable copy() throws JscriptException {
		return (SimpleJscriptVariable) JscriptVariable.super.copy();
	}

	public Map<String, Object> toMap() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("value", JscriptFileUtil.getJsonUtil().toString(value));
			return map;
		} catch (IOException e) {
			throw new JscriptRuntimeException(e);
		}
	}

	@Override
	public void loadMap(Map<String, Object> map) throws JscriptException {
		Object _name = map.get("name");
		if (!(_name instanceof String))
			throw new JscriptException("name应为String类型");
		setName((String) _name);
		this.value = map.get("value");
	}

}
