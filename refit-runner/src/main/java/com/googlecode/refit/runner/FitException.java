package com.googlecode.refit.runner;

public class FitException extends RuntimeException {
    
    private static final long serialVersionUID = 2339653105974921247L;

    public FitException() {
    }    
    
    public FitException(String msg) {
        super(msg);
    }
    
    
    public FitException(Throwable exc) {
        super(exc);
    }
    
    public FitException(String msg, Throwable exc) {
        super(msg, exc);
    }
}
