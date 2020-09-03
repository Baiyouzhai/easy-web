package org.byz.easy.web.springboot;


import java.util.HashMap;
import java.util.Map;

import byz.easy.jscript.core.itf.JscriptEngine;

/**
 * @author 
 * @since 2020年6月1日
 */
public class JscriptManage {

	protected JscriptEngine jscriptEngine;

	public JscriptManage(JscriptEngine jscriptEngine) {
		this.jscriptEngine = jscriptEngine;
	}

	public JscriptEngine getJscriptEngine() {
		return jscriptEngine;
	}

	public void setJscriptEngine(JscriptEngine jscriptEngine) {
		this.jscriptEngine = jscriptEngine;
	}

}
