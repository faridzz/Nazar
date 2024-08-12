package org.example.nazar.exception;

public class ProductTypeNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Product type not found";

    public ProductTypeNotFoundException() {
        super(MESSAGE);
    }

    public ProductTypeNotFoundException(String message) {
        super(message);
    }
}
