package org.example.onlineexchange.Exceptions;

public class FirstNameException extends RuntimeException {
    public FirstNameException(){
        super("The name should only consist of letters");
    }
}
