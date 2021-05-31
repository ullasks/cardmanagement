package com.cms.cardmanagement.exception;

public class InvalidCreditCardException extends RuntimeException {

	private static final long serialVersionUID = -890968488647110267L;

	public InvalidCreditCardException(String message) {
		super(message);
	}
	
	public InvalidCreditCardException(String message,Throwable e) {
		super(message,e);
	}

}
