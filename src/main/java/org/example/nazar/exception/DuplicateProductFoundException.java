package org.example.nazar.exception;

public class DuplicateProductFoundException extends RuntimeException {
    private static final String MESSAGE = "Duplicate product found";

    public DuplicateProductFoundException() {
        super(MESSAGE);
    }

    public DuplicateProductFoundException(String message) {
        super(message);
    }
}
