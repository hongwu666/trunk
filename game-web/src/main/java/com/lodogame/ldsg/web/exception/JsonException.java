package com.lodogame.ldsg.web.exception;

/**
 * JSON错误.
 * 
 * @author CJ
 */
public class JsonException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonException(String message) {
		super(message);
	}

	public JsonException(Throwable cause) {
		super(cause);
	}

}
