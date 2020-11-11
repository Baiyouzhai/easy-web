package byz.easy.jscript.springboot;

import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import byz.easy.jscript.JscriptUtil;
import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptEngine;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import byz.easy.common.Result;

/**
 * @author
 * @since 2019年12月22日
 */
@RestController
public class JscriptApiController {

	@Autowired
	private JscriptEngine engine;

	@Autowired(required=false)
	private JscriptManage manage;

	protected boolean checkVariable(Object _package) throws JscriptException {
		if (null == _package)
			throw new JscriptException("参数缺失：package");
		if (_package instanceof List)
			return true;
		throw new JscriptException("参数类型错误：package应为List<String>类型");
	}

	protected boolean checkVariable(Object _package, Object version, Object argNames) throws JscriptException {
		if (null == version)
			throw new JscriptException("参数缺失：version");
		if (null == argNames)
			throw new JscriptException("参数缺失：argNames");
		if (version instanceof String) {
			if (argNames instanceof List)
				return checkVariable(_package);
			throw new JscriptException("参数类型错误：argNames应为List<String>类型");
		}
		throw new JscriptException("参数类型错误：version应为String类型");
	}

	/**
	 * 调用指定name的脚本
	 * 
	 * @param name
	 * @param data
	 * @return
	 */
	public Result<?> engineUse(@PathVariable String name, @RequestBody Map<String, Object> data) {
		try {
			Object jscript = engine.get(name);
			if (jscript instanceof JscriptFunction)
				return new Result<>(engine.run(name, JscriptManage.toArgs((JscriptFunction) jscript, data)));
			return new Result<>(engine.run(name, data));
		} catch (NoSuchMethodException e) {
			return new Result<>(500, "运行错误：没有此接口", e.getMessage());
		} catch (ScriptException e) {
			return new Result<>(500, "运行错误：API内部运行存在错误", e.getMessage());
		}
	}

	public Result<?> manageUse(@PathVariable String name, @RequestBody Map<String, Object> data) {
		try {
			Object _package = data.remove("package");
			Object version = data.remove("version");
			checkVariable(_package);
			String[] temp = ((List<String>) _package).stream().toArray(String[]::new);
			if (null == version) {
				Jscript jscript = manage.get(temp, name);
				return new Result<>(manage.run(temp, name, JscriptManage.toArgs(jscript, data)));
			}
			String[] argNames = data.keySet().toArray(new String[data.size()]);
			Jscript jscript = manage.get(temp, name, (String) version, argNames);
			return new Result<>(manage.run(temp, name, (String) version, argNames, JscriptManage.toArgs(jscript, data)));
		} catch (JscriptException e) {
			return new Result<>(400, "参数存在错误", e.getMessage());
		} catch (NoSuchMethodException e) {
			return new Result<>(500, "运行错误：没有此接口", e.getMessage());
		} catch (ScriptException e) {
			return new Result<>(500, "运行错误：API内部运行存在错误", e.getMessage());
		}
	}

	public Result<?> engineAdd(@RequestBody Map<String, Object> data) {
		try {
			if (!data.containsKey("order"))
				data.put("order", System.currentTimeMillis());
			Jscript jscript = JscriptUtil.buildJscript(data);
			return new Result<>(200, "添加成功", engine.put(jscript));
		} catch (ScriptException e) {
			return new Result<>(400, "添加失败", e.getMessage());
		}
	}

	public Result<?> manageAdd(@RequestBody Map<String, Object> data) {
		try {
			Object result = null;
			Object _package = data.get("package");
			Object version = data.get("version");
			Object argNames = data.get("argNames");
			checkVariable(_package, version, argNames);
			String[] p = ((List<String>) _package).stream().toArray(String[]::new);
			String[] a = ((List<String>) argNames).stream().toArray(String[]::new);
			manage.get(p, (String) data.get("name"), (String) version, a);
			if (!data.containsKey("order"))
				data.put("order", System.currentTimeMillis());
			result = manage.put(p, data, (String) version);
			return new Result<>(200, "添加成功", result);
		} catch (ScriptException e) {
			return new Result<>(400, "添加失败", e.getMessage());
		}
	}

}