package com.hesso.greenliving.exception;

public class NotSupportedOperationException extends RuntimeException {

    private static final long serialVersionUID = -4929893512177942437L;

    public NotSupportedOperationException() {
	super();
    }

    public NotSupportedOperationException( String detailMessage, Throwable throwable ) {
	super( detailMessage, throwable );
    }

    public NotSupportedOperationException( String detailMessage ) {
	super( detailMessage );
    }

    public NotSupportedOperationException( Throwable throwable ) {
	super( throwable );
    }
}
