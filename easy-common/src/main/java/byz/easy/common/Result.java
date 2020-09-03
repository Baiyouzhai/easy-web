package byz.easy.common;

/**
 * @author 
 * @since 2020年6月29日
 */
public class Result<T> {
	
	protected int code;
	protected String message;
	protected T data;

	public Result() {
	}

	public Result(T data) {
		this(200, "success", data);
	}

	public Result(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
