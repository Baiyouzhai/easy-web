package byz.easy.jscript.core;

import byz.easy.jscript.core.itf.JscriptOrder;

/**
 * @author
 * @since 2020年7月24日
 */
public class SimpleJscriptOrder extends SimpleJscript implements JscriptOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected long order;

	public SimpleJscriptOrder() {
	}

	public SimpleJscriptOrder(String codeBlock) {
		this(codeBlock, System.currentTimeMillis());
	}

	public SimpleJscriptOrder(String codeBlock, long order) {
		super(codeBlock);
		this.order = order;
	}

	@Override
	public void setOrder(long order) {
		this.order = order;
	}

	@Override
	public long getOrder() {
		return order;
	}

}
