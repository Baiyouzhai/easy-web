package byz.easy.jscript.core;

import byz.easy.common.JavaUtil;

/**
 * @author
 * @since 2020年7月24日
 */
public class SimpleJscriptRun extends SimpleJscriptInit implements JscriptRun {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String returnTo;

	public SimpleJscriptRun() {
	}

	public SimpleJscriptRun(String codeBlock) throws JscriptException {
		super(codeBlock);
	}

	public SimpleJscriptRun(String codeBlock, String returnTo) throws JscriptException {
		super(codeBlock);
		setReturnTo(returnTo);
	}

	@Override
	public String getReturnTo() {
		return returnTo;
	}

	@Override
	public void setReturnTo(String returnTo) throws JscriptException {
		try {
			if (null == returnTo || "".equals(returnTo))
				this.returnTo = "";
			else if (JavaUtil.checkVariableName(returnTo))
				this.returnTo = returnTo;
		} catch (Exception e) {
			throw new JscriptException("返回的变量名称不符合规范: name -> " + returnTo);
		}
	}

}
