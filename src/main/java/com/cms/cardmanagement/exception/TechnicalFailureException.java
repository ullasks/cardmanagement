package com.cms.cardmanagement.exception;

public class TechnicalFailureException extends RuntimeException {

	private static final long serialVersionUID = -890968488647110267L;

	public TechnicalFailureException(String message) {
		super(message);
	}
	public TechnicalFailureException(String message,Throwable e) {
		super(message,e);
	}

}
