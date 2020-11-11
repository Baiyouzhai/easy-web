package byz.easy.jscript.springboot;

import java.util.Map;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import byz.easy.common.Result;
import byz.easy.jscript.JscriptUtil;
import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptEngine;
import byz.easy.jscript.core.JscriptFunction;

/**
 * @author
 * @since 2019年12月22日
 */
@RestController
public class JscriptEngineController {

	@Autowired
	private JscriptEngine engine;

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

}