package byz.easy.jscript.extend;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptEngine;
import byz.easy.jscript.core.JscriptFunction;
import byz.easy.jscript.core.StandardJscriptEngineWapper;
import byz.easy.jscript.core.StandardJscriptFunction;
import byz.easy.jscript.nashorn.NashornEngine;

/**
 * 
 * @author
 * @since 2020年8月28日
 */
public class SimpleJEWPackage implements JscriptEnginePackage {

	protected String[] basePackage;
	protected String pathConcat;
	protected Map<String, Jscript> mapping;

	public SimpleJEWPackage(JscriptEngine engine, String[] basePackage, String pathConcat) throws ScriptException {
		super(engine);
		basePackage = new String[0];
		pathConcat = "_";
		packageMapping = new HashMap<>();
		this.basePackage = null == basePackage ? new String[0] : basePackage;
		if (null != pathConcat && !"".equals(pathConcat) && concats.contains(pathConcat))
			this.pathConcat = pathConcat;
		packageMapping = new HashMap<>();
	}

	public SimpleJEWPackage(String[] basePackage, String pathConcat) throws ScriptException {
		this.basePackage = null == basePackage ? new String[0] : basePackage;
		if (null != pathConcat && !"".equals(pathConcat) && concats.contains(pathConcat))
			this.pathConcat = pathConcat;
		packageMapping = new HashMap<>();
	}

	@Override
	public String getPathConcat() {
		return pathConcat;
	}

	@Override
	public String[] getBasePackage() {
		return basePackage;
	}

	@Override
	public Map<String, JscriptFunction> getPackageMapping() {
		return packageMapping;
	}

}