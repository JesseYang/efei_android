package com.efei.lib.android.exception;

public class EfeiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EfeiException() {
		super();
	}

	public EfeiException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public EfeiException(String detailMessage) {
		super(detailMessage);
	}

	public EfeiException(Throwable throwable) {
		super(throwable);
	}

}
