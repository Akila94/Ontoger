package com.ontoger.exceptions;

public class NoResultsReturnedException extends RuntimeException {

    public NoResultsReturnedException() {
        super();
    }

    public NoResultsReturnedException(String msg) {
        super(msg);
    }
}
