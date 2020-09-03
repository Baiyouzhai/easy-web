package org.byz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 
 * @since 2020年6月29日
 */
@RestController
public class TestController {
	
	@RequestMapping("/test")
	public String test(String path) {
		return "test";
	}

}
