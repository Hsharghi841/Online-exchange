package org.example.onlineexchange.Exceptions;

public class UserNameTakenException extends RuntimeException {
    public UserNameTakenException() {
        super("this username is taken");
    }
}
