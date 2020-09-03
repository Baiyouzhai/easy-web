package byz.easy.jscript;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import byz.easy.jscript.core.JscriptRuntimeException;
import byz.easy.jscript.core.SimpleJscriptInit;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.core.itf.JscriptFunction;
import byz.easy.jscript.core.itf.JscriptInit;

/**
 * 
 * @author
 * @since 2020年7月23日
 */
public class SimpleJscriptManage extends AbstractJscriptManage {

	public static Object[] toRunnerArgs(String[] _package, String name, String version, String[] argNames, Object... args) {
		Object[] params = new Object[5];
		params[0] = Arrays.stream(_package).collect(Collectors.joining("."));
		params[1] = name;
		params[2] = version;
		params[3] = Arrays.stream(argNames).collect(Collectors.joining(","));
		params[4] = args;
		return params;
	}

	public SimpleJscriptManage(String engineName, String savePath) throws ScriptException {
		super(JscriptUtil.buildJscriptEngine(engineName), savePath);
	}

	public SimpleJscriptManage(JscriptEngine engine, String savePath) throws ScriptException {
		super(engine, savePath);
	}

	@Override
	public List<JscriptInit> getInits() throws JscriptRuntimeException {
		try {
			List<JscriptInit> inits = new ArrayList<>();
			Class<?> c = SimpleJscriptManage.class;
			String codeBlock = JscriptUtil.read(c.getResourceAsStream("JavaImport.js"));
			inits.add(new SimpleJscriptInit(codeBlock));
			codeBlock = JscriptUtil.read(c.getResourceAsStream("OverloadContainer.js"));
			inits.add(new SimpleJscriptInit(codeBlock));
			codeBlock = JscriptUtil.read(c.getResourceAsStream("FunctionContainerExtend.js")).concat("('.')");
			inits.add(new SimpleJscriptInit(codeBlock));
			codeBlock = JscriptUtil.read(c.getResourceAsStream("RunnerExtend.js"));
			inits.add(new SimpleJscriptInit(codeBlock));
			return inits;
		} catch (IOException e) {
			throw new JscriptRuntimeException("初始化失败，内部文件读取错误", e);
		}
	}

	@Override
	public SimpleJscriptManage create() throws JscriptRuntimeException, ScriptException {
		super.create();
		return this;
	}

	@Override
	public Jscript get(String[] _package, String name) throws ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, ".") + "#"+ name;
		JscriptFunction function = jscriptFunctionMapping.get(path);
		if (null != function) {
			return function;
		} else {
			return jscriptMapping.get(path);
		}
	}

	@Override
	public Jscript get(String[] _package, String name, String version, String[] argNames) throws ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, ".") + "#"+ name + "@" + version;
		JscriptFunction function = jscriptFunctionMapping.get(path + "@" + Arrays.toString(argNames));
		return null == function ? jscriptMapping.get(path) : function;
	}

	public Object put(Jscript jscript, JscriptFile jscriptFile, JscriptVersion version) throws ScriptException {
		fileMapping.put(jscript, jscriptFile);
		versionMapping.put(jscript, version);
		int status = version.getStatus();
		switch (status) {
			case 1 :
				String path = jscriptFile.getFolder().getPath(".").replace(folder.getName() + ".", "");
				jscriptMapping.put(path + "#" + jscriptFile.getName() + "@" + version.getName(), jscript);
				if (0 == version.getType())
					jscriptMapping.put(path + "#" + jscriptFile.getName(), jscript);
				if (jscript instanceof JscriptFunction) {
					JscriptFunction temp = (JscriptFunction) jscript;
					JscriptFunction old = jscriptFunctionMapping.put(path + "#" + temp.getName() + "@" + version.getName() + "@" + Arrays.toString(temp.getArgNames()), temp);
					if (0 == version.getType())
						jscriptFunctionMapping.put(path + "#" + temp.getName(), temp);
					engine.execute("FunctionContainer.put('" + path + "', '" + temp.getName() + "', '" + version.getName() + "', '" + Arrays.stream(temp.getArgNames()).collect(Collectors.joining(",")).toString() + "', \""
							+ jscript.getCodeBlock() + "\")");
					return old;
				}
				return engine.execute(jscript.getRunBody());
			default :
				return null;
		}
	}

	@Override
	public Object run(String[] _package, String name, Object... args) throws NoSuchMethodException, ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, ".") + "#" + name;
		JscriptFunction function = jscriptFunctionMapping.get(path);
		if (null != function) {
			Object[] asArgs = toRunnerArgs(_package, name, versionMapping.get(function).getName(), function.getArgNames(), null == args ? new Object[0] : args);
			return engine.run("Runner", asArgs);
		} else {
			Jscript jscript = jscriptMapping.get(path);
			if (null != jscript)
				return engine.execute(jscript.getRunBody());
			throw new NoSuchMethodException("没有可运行的脚本 -> " + path);
		}
	}

	@Override
	public Object run(String[] _package, String name, String version, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, ".") + "#" + name + "@" + version;
		JscriptFunction function = jscriptFunctionMapping.get(path + "@" + Arrays.toString(argNames));
		if (null != function) {
			Object[] asArgs = toRunnerArgs(_package, name, version, function.getArgNames(), null == args ? new Object[0] : args);
			return engine.run("Runner", asArgs);
		} else {
			Jscript jscript = jscriptMapping.get(path);
			if (null != jscript) {
				return engine.execute(jscript.getRunBody());
			}
			throw new NoSuchMethodException("没有可运行的脚本 -> " + path + "@" + Arrays.toString(argNames));
		}
	}

}
