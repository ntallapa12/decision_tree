/**
 * 
 */
package com.forum.ml.dtree.common;

/**
 * @author ntallapa
 *
 */
public class DTException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DTException() {
	}

	/**
	 * @param message
	 */
	public DTException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DTException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DTException(String message, Throwable cause) {
		super(message, cause);
	}

}
