package byz.easy.jscript.nashorn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.SimpleJscriptFunction;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * @author
 * @since 2020年8月7日
 */
public class NashornUtil {

	public static String[] getArgNames(String function) {
		List<String> list = JavaUtil.findPairBracket(function.toCharArray(), 0, 0, '(', ')');
		String argNames = list.get(0);
		if ("()".equals(argNames)) {
			return new String[0];
		} else {
			String[] split = argNames.substring(1, argNames.length() - 1).split(",");
			return Arrays.stream(split).map(item -> item.trim()).toArray(String[]::new);
		}
	}

	public static String getCodeBlock(String function) {
		List<String> list = JavaUtil.findPairBracket(function.toCharArray(), 0, 0, '{', '}');
		String codeBlock = list.get(0);
		return codeBlock.substring(1, codeBlock.length() - 1);
	}

	public static Object convertScriptObjectMirror(Object obj) throws JscriptException {
		if (obj instanceof ScriptObjectMirror) {
			String type = ((ScriptObjectMirror) obj).getClassName();
			if ("Function".equals(type)) {
				String functionBody = obj.toString();
				String[] argNames = getArgNames(functionBody);
				String codeBlock = getCodeBlock(functionBody);
				return new SimpleJscriptFunction((String) ((Bindings) obj).get("name"), argNames, codeBlock);
			} else if ("Object".equals(type)) {
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.putAll((Bindings) obj);
				return temp;
			}
		}
		return obj;
	}

}
