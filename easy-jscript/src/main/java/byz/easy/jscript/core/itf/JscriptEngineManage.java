package byz.easy.jscript.core.itf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.JscriptFile;
import byz.easy.jscript.JscriptFolder;
import byz.easy.jscript.JscriptOrder;
import byz.easy.jscript.JscriptVersion;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptRuntimeException;

/**
 * 统一包和重写管理
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptEngineManage {

	public static Map<Class<?>, Integer> orderTypeMapping = new HashMap<>();

	static boolean checkPackagePath(String[] _package) throws ScriptException {
		try {
			for (String temp : _package)
				JavaUtil.checkFolderName(temp);
		} catch (IOException e) {
			throw new JscriptException("文件夹名错误 _package -> " + e.getMessage(), e);
		}
		return true;
	}
	
	List<JscriptInit> getInits() throws JscriptRuntimeException;

	JscriptEngineManage create() throws ScriptException;

	JscriptEngine getEngine();

	JscriptFolder getFolder();

	Map<String, Jscript> getMapping();
	
	Jscript get(String[] _package, String name) throws ScriptException;

	Jscript get(String[] _package, String name, String version, String[] argNames) throws ScriptException;

	Object put(String[] _package, Map<String, Object> json, String version) throws ScriptException;

	Object put(String[] _package, Jscript jscript, String version) throws ScriptException;

	Object put(Jscript jscript, JscriptFile jscriptFile, JscriptVersion version) throws ScriptException;

	Object put(Jscript jscript, JscriptFile jscriptFile, JscriptVersion version, JscriptOrder order) throws ScriptException;

	Object run(String[] _package, String name, String version, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException;
	
	Object run(String[] _package, String name, Object... args) throws NoSuchMethodException, ScriptException;

}
