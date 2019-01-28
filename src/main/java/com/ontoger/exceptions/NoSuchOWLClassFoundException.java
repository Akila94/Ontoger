package com.ontoger.exceptions;

public class NoSuchOWLClassFoundException extends RuntimeException {
    public NoSuchOWLClassFoundException() {
        super();
    }
    public NoSuchOWLClassFoundException(String msg) {
        super(msg);
    }
}
