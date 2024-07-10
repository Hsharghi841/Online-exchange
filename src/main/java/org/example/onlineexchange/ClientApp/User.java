package org.example.onlineexchange.ClientApp;

import org.example.onlineexchange.Coins.Coin;
import org.example.onlineexchange.Coins.Orders;
import org.example.onlineexchange.Exceptions.*;
import org.example.onlineexchange.Server.Database;
import org.example.onlineexchange.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    public ArrayList<Orders> orders=new ArrayList<>();
    public Wallet wallet=new Wallet();
    public static User user;

    public User(String firstName, String lastName, String phoneNumber, String email, String username, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setUsername(username);
        setPassword(password);
    }

    private User(){};

    public void setFirstName(String firstName) {
        if(!firstName.trim().matches("[a-z ]+"))throw new FirstNameException();
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if(!lastName.trim().matches("[a-z ]+"))throw new LastNameException();
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(!((phoneNumber.trim().matches("(09)[0-9]+") && phoneNumber.trim().length() == 11) ||
                (phoneNumber.trim().matches("(\\+98)[0-9]+") && phoneNumber.trim().length() == 13)))
            throw new PhoneNumberException();
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if(!email.trim().matches("[a-zA-z\\.0-9_-]+@[a-zA-Z0-9]+\\.[a-z]+"))throw new EmailException();
        this.email = email;
    }

    public void setUsername(String username) {
        if(!username.trim().matches("[a-zA-z0-9\\.]+"))throw new UserNameException();
        this.username = username;
    }

    public void setPassword(String password) {
        if(password.length() < 8)throw new passwordLengthException();
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return STR."User [id=\{id}, firstName='\{firstName}\{'\''}, lastName='\{lastName}\{'\''}, phoneNumber='\{phoneNumber}\{'\''}, email='\{email}\{'\''}, username='\{username}\{'\''}, password='\{password}\{'\''}\{']'}";
    }

}
