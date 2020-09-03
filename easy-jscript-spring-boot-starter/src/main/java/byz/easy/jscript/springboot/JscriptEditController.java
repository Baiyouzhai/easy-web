package byz.easy.jscript.springboot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import byz.easy.common.Result;

/**
 * 
 * @author
 * @since 2019年12月22日
 */
@Controller
public class JscriptEditController {

	@Autowired
	private JscriptProperties properties;

	@ResponseBody
	public Result<?> getProperties() {
		return new Result<>(properties);
	}
	
}