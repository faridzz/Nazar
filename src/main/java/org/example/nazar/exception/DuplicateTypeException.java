package org.example.nazar.exception;

public class DuplicateTypeException extends RuntimeException {
    private final static String MASSAGE = "Duplicate type";

    public DuplicateTypeException(String message) {
        super(message);
    }

    public DuplicateTypeException() {
        super(MASSAGE);
    }
}
