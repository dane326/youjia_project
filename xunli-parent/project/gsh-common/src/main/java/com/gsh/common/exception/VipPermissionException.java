package com.gsh.common.exception;

/**
 * VIP异常异常
 * 
 * @author gsh
 */
public class VipPermissionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	protected final String message;

	public VipPermissionException(String message) {
		this.message = message;
	}

	public VipPermissionException(String message, Throwable e) {
		super(message, e);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
