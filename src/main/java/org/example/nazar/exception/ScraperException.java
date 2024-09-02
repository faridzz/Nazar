package org.example.nazar.exception;

public class ScraperException extends RuntimeException {
    public ScraperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScraperException(String message) {
        super(message);
    }
}
