package org.byz.easy.web.springboot;


import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import byz.easy.jscript.core.JscriptException;
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
public class JscriptController {

	private JscriptManage manage;

	/**
	 * 调用指定name的脚本
	 * 
	 * @param name
	 * @param data
	 * @return
	 */
	public Result<?> useScript(@PathVariable String name, @RequestBody Map<String, Object> data) {
		try {
			return new Result<>(null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result<>(500, "error", "函数运行错误");
		}
	}

	/**
	 * 装载指定脚本
	 * 
	 * @param data
	 * @return
	 */
	public Result<?> reloadScript(@PathVariable String name, @PathVariable String version, @RequestBody Map<String, Object> data) {
		return new Result<>(data);
	}

	/**
	 * 装载全部脚本
	 * 
	 * @return
	 */
	public Result<?> reloadAllScript() {
		return new Result<>("装载完成");
	}

	public Result<?> addScript(@RequestBody Map<String, Object> data) {
		try {
			return new Result<>(200, "添加成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result<>(400, "添加失败", e);
		}
	}

}