package byz.easy.jscript.core;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.core.itf.JscriptRun;

/**
 * @author
 * @since 2020年7月24日
 */
public class SimpleJscriptRun extends SimpleJscriptInit implements JscriptRun {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		typeMapping.put("run", SimpleJscriptRun.class);
	}

	protected String returnTo;

	public SimpleJscriptRun() {
	}

	public SimpleJscriptRun(String codeBlock) throws JscriptException {
		super(codeBlock);
	}

	public SimpleJscriptRun(String codeBlock, String returnTo) throws JscriptException {
		super(StringUtils.isEmpty(codeBlock) ? "return 0;" : codeBlock);
		setReturnTo(returnTo);
	}

	@Override
	public String getReturnTo() {
		return returnTo;
	}

	@Override
	public void setReturnTo(String returnTo) throws JscriptException {
		if (null != returnTo && !"".equals(returnTo)) {
			if (!Pattern.matches(JavaUtil.variableNameRegx, returnTo))
				throw new JscriptException("运行返回名称不符合规范: name -> " + returnTo);
			this.returnTo = returnTo;
		}
	}

}
