package org.example.onlineexchange.Exceptions;

public class LastNameException extends RuntimeException {
    public LastNameException() {
        super("The last name should only consist of letters");
    }
}
