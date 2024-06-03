package org.example.onlineexchange.Exceptions;

public class EmailException extends RuntimeException {
    public EmailException() {
        super("Email is incorrect");
    }
}
