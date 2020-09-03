package byz.easy.jscript.core;

import javax.script.ScriptException;

/**
 * @author 
 * @since 2020年5月26日
 */
public class JscriptException extends ScriptException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JscriptException(String message) {
		super(message);
	}

    public JscriptException(String message, Throwable cause) {
    	super(message);
    	initCause(cause);
    }

}
