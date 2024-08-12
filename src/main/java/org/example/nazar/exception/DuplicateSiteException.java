package org.example.nazar.exception;

public class DuplicateSiteException extends RuntimeException {
    private static final String MESSAGE = "Duplicate site found" ;
    public DuplicateSiteException (String message){
        super(message);
    }
    public DuplicateSiteException (){
        super(MESSAGE);
    }

}
