package byz.easy.jscript.core;

/**
 * @author 
 * @since 2020年5月26日
 */
public class JscriptRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JscriptRuntimeException(Throwable cause) {
		super(cause);
	}

	public JscriptRuntimeException(String message) {
		super(message);
	}

	public JscriptRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
