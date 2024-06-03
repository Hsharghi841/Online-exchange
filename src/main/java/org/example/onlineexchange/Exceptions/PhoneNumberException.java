package org.example.onlineexchange.Exceptions;

public class PhoneNumberException extends RuntimeException {
    public PhoneNumberException() {
        super("The phone number is incorrect");
    }
}
