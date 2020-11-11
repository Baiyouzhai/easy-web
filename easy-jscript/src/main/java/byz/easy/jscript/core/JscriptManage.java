package byz.easy.jscript.core;

import java.util.List;

/**
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JscriptManage {

	JscriptSourceManage getJscriptSourceManage();

	JscriptEngine getJscriptEngine();

	default JscriptManage init() throws JscriptException {
		JscriptSourceManage sourceManage = getJscriptSourceManage();
		JscriptEngine engine = getJscriptEngine();
		sourceManage.load();
		List<Jscript> load = sourceManage.getOrderList();
		for (Jscript jscript : load)
			engine.put(jscript);
		return this;
	}

}
