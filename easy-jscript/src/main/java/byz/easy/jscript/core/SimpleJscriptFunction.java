package byz.easy.jscript.core;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import byz.easy.common.JavaUtil;

/**
 * 根据JS运行函数的一些组成部分抽象化一些属性的简单实现</br>
 * name 函数名</br>
 * argNames 传参传递名</br>
 * codeBlock 运行的代码块
 * 
 * @author
 * @since 2019年12月20日
 */
public class SimpleJscriptFunction extends SimpleJscriptInit implements JscriptFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String name;
	protected String[] argNames;

	public SimpleJscriptFunction() {
	}

	public SimpleJscriptFunction(String name, String codeBlock) throws JscriptException {
		this(name, new String[0], codeBlock);
	}

	public SimpleJscriptFunction(String name, String[] argNames, String codeBlock) throws JscriptException {
		super(StringUtils.isEmpty(codeBlock) ? "return 0;" : codeBlock);
		setName(name);
		setArgNames(argNames);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) throws JscriptException {
		try {
			JavaUtil.checkVariableName(name);
			this.name = name;
		} catch (Exception e) {
			throw new JscriptException("函数名不符合命名规范: name -> " + name, e);
		}
	}

	@Override
	public String[] getArgNames() {
		return argNames;
	}

	public void setArgNames(String[] argNames) throws JscriptException {
		int index = 0;
		try {
			for (; index < argNames.length; index++)
				JavaUtil.checkVariableName(argNames[index]);
			this.argNames = argNames;
		} catch (Exception e) {
			throw new JscriptException("函数的参数名不符合命名规范: argNames -> " + Arrays.toString(argNames) + " -> index: " + index, e);
		}
	}

}
