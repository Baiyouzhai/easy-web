package byz.easy.html;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

/**
 * @author 
 * @time 2019年11月3日
 */
@Controller
public class HtmlController {
	
	public String index(HttpServletRequest request) {
		return "index";
	}
	
	public String template(HttpServletRequest request) {
		return "template";
	}
	
	public String view(HttpServletRequest request) {
		return "view";
	}
	
}