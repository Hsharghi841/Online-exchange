package org.example.onlineexchange.Exceptions;

public class passwordLengthException extends RuntimeException {
    public passwordLengthException() {
        super("The password should be more than 8 character");
    }
}
