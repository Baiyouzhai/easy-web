package byz.easy.jscript.core;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.core.itf.JscriptEnginePackage;
import byz.easy.jscript.core.itf.JscriptFunction;
import byz.easy.jscript.core.nashorn.NashornEngine;

/**
 * 
 * @author
 * @since 2020年8月28日
 */
public class SimpleJscriptEnginePackage extends SimpleJscriptEngineWapper implements JscriptEnginePackage {

	protected String[] basePackage;
	protected String pathConcat;
	protected Map<String, JscriptFunction> packageMapping;

	public SimpleJscriptEnginePackage() {
		basePackage = new String[0];
		pathConcat = "_";
		packageMapping = new HashMap<>();
	}

	public SimpleJscriptEnginePackage(String engineName, String[] basePackage, String pathConcat) throws ScriptException {
		this(Utils.buildJscriptEngine(engineName), basePackage, pathConcat);
	}

	public SimpleJscriptEnginePackage(JscriptEngine engine, String[] basePackage, String pathConcat) throws ScriptException {
		super(engine);
		this.basePackage = null == basePackage ? new String[0] : basePackage;
		if (null != pathConcat && !"".equals(pathConcat))
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
	public JscriptFunction putFunction(String[] _package, JscriptFunction function) throws ScriptException {
		JscriptFunction copy = function.copy();
		String realName = getPath(_package) + "_" + copy.getName();
		copy.setName(realName);
		super.putFunction(copy);
		return packageMapping.put(realName, copy);
	}

	@Override
	public JscriptFunction getFunction(String[] _package, String name) {
		String realName = getPath(_package) + "_" + name;
		return packageMapping.get(realName);
	}

	@Override
	public JscriptFunction removeFunction(String[] _package, String name) {
		String realName = getPath(_package) + "_" + name;
		super.removeFunction(realName);
		return packageMapping.remove(realName);
	}
	
	public static void main(String[] args) {
		try {
			SimpleJscriptEnginePackage jscriptEnginePackage = new SimpleJscriptEnginePackage();
			jscriptEnginePackage.bindEngine(new NashornEngine());
			jscriptEnginePackage.putFunction(new SimpleJscriptFunction("a_b_b_test", "print('Hello World!'); return 0;"));
			jscriptEnginePackage.putFunction(new String[] {"a", "b", "b"}, new SimpleJscriptFunction("test", "print('Hello World!'); return 1;"));
			System.out.println(jscriptEnginePackage.run(new String[] {"a", "b", "b"}, "test"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}