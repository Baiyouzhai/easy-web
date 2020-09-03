package byz.easy.jscript.core;

import byz.easy.jscript.core.itf.JscriptInit;

/**
 * @author
 * @since 2020年7月24日
 */
public class SimpleJscriptInit extends SimpleJscript implements JscriptInit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static {
		typeMapping.put("init", SimpleJscriptInit.class);
	}

	public SimpleJscriptInit() {
	}

	public SimpleJscriptInit(String codeBlock) {
		super(codeBlock);
	}

}
