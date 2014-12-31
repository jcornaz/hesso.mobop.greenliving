package com.hesso.greenliving.exception;

public class UnexpectedException extends RuntimeException {
    private static final long serialVersionUID = -4063592642910513173L;

    public UnexpectedException() {
	super();
    }

    public UnexpectedException( String detailMessage, Throwable throwable ) {
	super( detailMessage, throwable );
    }

    public UnexpectedException( String detailMessage ) {
	super( detailMessage );
    }

    public UnexpectedException( Throwable throwable ) {
	super( throwable );
    }
}
