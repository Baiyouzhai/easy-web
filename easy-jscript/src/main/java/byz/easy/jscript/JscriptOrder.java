package byz.easy.jscript;

import byz.easy.common.VirtualOrder;
import byz.easy.jscript.core.itf.Jscript;

/**
 * @author
 * @since 2020年8月1日
 */
public class JscriptOrder implements VirtualOrder {

	protected int type;
	protected long order;
	protected Jscript jscript;

	public JscriptOrder(int type, long order) {
		this(type, order, null);
	}

	public JscriptOrder(int type, long order, Jscript jscript) {
		this.type = type;
		this.order = order;
		this.jscript = jscript;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public Jscript get() {
		return jscript;
	}

	public void set(Jscript jscript) {
		this.jscript = jscript;
	}

	@Override
	public int getOrderType() {
		return type;
	}

	@Override
	public long getOrderNumber() {
		return order;
	}

}
