package org.example.nazar.exception;

public class DuplicateHashIdException extends RuntimeException {
    final private static String DEFAULT_STRING = "Duplicate Hash";


    public DuplicateHashIdException(String message) {
        super(message);
    }

    public DuplicateHashIdException() {
        super();
    }
}
