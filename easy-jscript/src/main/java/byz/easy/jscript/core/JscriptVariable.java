package byz.easy.jscript.core;

/**
 * JS引擎执行代码
 * 
 * @author
 * @since
 */
public interface JscriptVariable extends Jscript {

	String getName();

	void setName(String name) throws JscriptException;

	Object getValue();

	void setValue(Object value);

}
