package org.example.onlineexchange.Exceptions;

public class UserNameNotFoundException extends RuntimeException {
    public UserNameNotFoundException() {
        super("username not found");
    }
}
