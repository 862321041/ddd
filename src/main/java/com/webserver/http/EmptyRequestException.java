package com.webserver.http;
/**
 * 空请求异常
 * 当HttpRequest在解析请求时发现此次请求是一个空请求时会抛出该异常。
 * @author lenovo
 *
 */
public class EmptyRequestException extends Exception{
	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;

	public EmptyRequestException() {
		super();
	
	}

	public EmptyRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	

}
