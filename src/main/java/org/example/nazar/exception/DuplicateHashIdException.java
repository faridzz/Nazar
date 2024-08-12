package org.example.nazar.exception;

public class DuplicateHashIdException extends RuntimeException {
    final private static String DEFAULT_STRING = "Duplicate Hash";

    DuplicateHashIdException() {
        super(DEFAULT_STRING);
    }

    public DuplicateHashIdException(String message) {
        super(message);
    }
}
