package byz.easy.jscript.core;

import byz.easy.jscript.core.itf.Jscript;

/**
 * @author
 * @since 2020年7月24日
 */
public class SimpleJscript implements Jscript {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static {
		typeMapping.put("js", SimpleJscript.class);
		typeMapping.put(SimpleJscript.class.getSimpleName(), SimpleJscript.class);
		typeMapping.put(SimpleJscript.class.getName(), SimpleJscript.class);
	}

	protected String codeBlock;

	public SimpleJscript() {
	}

	public SimpleJscript(String codeBlock) {
		this.codeBlock = codeBlock;
	}

	@Override
	public String getCodeBlock() {
		return codeBlock;
	}

	@Override
	public void setCodeBlock(String codeBlock) {
		this.codeBlock = codeBlock;
	}

}
