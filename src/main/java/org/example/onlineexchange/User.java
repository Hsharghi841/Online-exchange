package org.example.onlineexchange;

import org.example.onlineexchange.Exceptions.*;

import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;

    public static final ArrayList<User> allUsers = new ArrayList<User>();

    public User(String firstName, String lastName, String phoneNumber, String email, String username, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setUsername(username);
        setPassword(password);
        allUsers.add(this);
    }

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
        for (User user : allUsers){
            if(username.equals(user.getUsername()))throw new UserNameTakenException();
        }
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
}
