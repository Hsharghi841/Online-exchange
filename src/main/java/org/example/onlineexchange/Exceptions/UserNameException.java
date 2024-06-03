package org.example.onlineexchange.Exceptions;

public class UserNameException extends RuntimeException {
    public UserNameException() {
        super("username should only consist of letters, numbers or '.'");
    }
}
