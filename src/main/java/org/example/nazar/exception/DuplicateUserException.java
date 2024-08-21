package org.example.nazar.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String userName) {
        super(userName + " already exists");
    }
}
