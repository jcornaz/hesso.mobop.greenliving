package com.hesso.greenliving.exception;

public class NotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 4463486112752198627L;

    public NotImplementedException() {
	super();
    }

    public NotImplementedException( String detailMessage, Throwable throwable ) {
	super( detailMessage, throwable );
    }

    public NotImplementedException( String detailMessage ) {
	super( detailMessage );
    }

    public NotImplementedException( Throwable throwable ) {
	super( throwable );
    }
}
