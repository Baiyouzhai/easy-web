package byz.easy.jscript.springboot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import byz.easy.jscript.JscriptFile;
import byz.easy.jscript.JscriptVersion;
import byz.easy.jscript.SimpleJscriptManage;
import byz.easy.jscript.core.JscriptRuntimeException;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.core.itf.JscriptFunction;

import byz.easy.common.JavaUtil;

/**
 * @author
 * @since 2020年8月6日
 */
public class JscriptManage extends SimpleJscriptManage {

	protected JscriptProperties properties;
	protected boolean help;
	protected Map<String, List<String[]>> argNamesMapping;

	public JscriptManage(JscriptProperties properties) throws ScriptException {
		super(properties.getEngineName(), properties.getSavePath());
		this.properties = properties;
	}

	public JscriptManage(JscriptEngine engine, JscriptProperties properties) throws ScriptException {
		super(engine, properties.getSavePath());
		this.properties = properties;
	}

	@Override
	public JscriptManage create() throws JscriptRuntimeException, ScriptException {
		help = properties.getApi().getApiHelp();
		argNamesMapping = new HashMap<>();
		super.create();
		return this;
	}

	@Override
	public Object put(Jscript jscript, JscriptFile jscriptFile, JscriptVersion version) throws ScriptException {
		try {
			return super.put(jscript, jscriptFile, version);
		} finally {
			if (help) {
				String path = jscriptFile.getFolder().getPath(".").replace(folder.getName() + ".", "");
				if (jscript instanceof JscriptFunction) {
					JscriptFunction temp = (JscriptFunction) jscript;
					path = path + "#" + temp.getName() + "@" + version.getName() + "@" + JavaUtil.arrayTotalHash(temp.getArgNames());
					if (!argNamesMapping.containsKey(path))
						argNamesMapping.put(path,  new ArrayList<>());
					argNamesMapping.get(path).add(temp.getArgNames());
				} else {
					
				}
			}
		}
	}

	@Override
	public Object run(String[] _package, String name, String version, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException {
		try {
			return super.run(_package, name, version, argNames, args);
		} catch (NoSuchMethodException | ScriptException e) {
			if (help) {
				String path = getPackagePath(_package, ".") + "#" + name + "@" + version + "@" + JavaUtil.arrayTotalHash(argNames);
				List<String[]> argNamesList = argNamesMapping.get(path);
				if (null != argNamesList) {
					StringBuilder builder = new StringBuilder();
					for (String[] _argNames : argNamesList)
						builder.append(", " + Arrays.toString(_argNames));
					throw new NoSuchMethodException("未找到可运行的脚本, 你要用的脚本的需要调整参数顺序 -> " + builder.substring(2));
				}
			}
			throw e;
		}
	}

}
