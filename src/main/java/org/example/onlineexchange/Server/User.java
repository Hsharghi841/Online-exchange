package org.example.onlineexchange.Server;

import org.example.onlineexchange.Coins.Coin;
import org.example.onlineexchange.Coins.Orders;
import org.example.onlineexchange.Exceptions.*;
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
    private Coin coins[] = new Coin[5], temp;
    private ArrayList<Orders> orders=new ArrayList<>();

    private static final Database db = Database.getDataBase();

    public Wallet wallet = new Wallet();

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
        try {
            ResultSet r = db.getStatement().executeQuery(STR."SELECT username FROM users WHERE username = '\{username}'");
            if(r.next())throw new UserNameTakenException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return STR."User [id=\{id}, firstName='\{firstName}\{'\''}, lastName='\{lastName}\{'\''}, phoneNumber='\{phoneNumber}\{'\''}, email='\{email}\{'\''}, username='\{username}\{'\''}, password='\{password}\{'\''}\{']'}";
    }

    public static void addUserToDatabase(User user) throws SQLException {
        db.getStatement().execute(
                "INSERT INTO users (firstName, lastName, phoneNumber, email, username, password)" +
                STR."VALUES ('\{user.firstName}', '\{user.lastName}', '\{user.phoneNumber}', '\{user.email}', '\{user.username}', '\{user.password}')");
        ResultSet r = db.getStatement().executeQuery(STR."SELECT id FROM users WHERE username = '\{user.username}'");
        r.next();
        user.id = r.getInt("id");
        db.getStatement().execute(STR."INSERT INTO wallet (user_id, usd, eur, yen, gbp)\nVALUES (\{user.id}, \{user.wallet.getUsd()} ,\{user.wallet.getEur()} ,\{user.wallet.getYen()} ,\{user.wallet.getGbp()});");
    }

    public static User getUserFromDatabase(String username) throws SQLException {
        ResultSet r = db.getStatement().executeQuery(STR."SELECT * FROM users WHERE username = '\{username}'");
        if(!r.next())throw new UserNameNotFoundException();
        User result = new User();
        result.id = r.getInt("id");
        result.firstName = r.getString("firstName");
        result.lastName = r.getString("lastName");
        result.phoneNumber = r.getString("phoneNumber");
        result.email = r.getString("email");
        result.username = r.getString("username");
        result.password = r.getString("password");

        r = db.getStatement().executeQuery(STR."SELECT * FROM wallet WHERE user_id = \{result.id}");
        r.next();
        result.wallet.setUsd(r.getDouble("usd"));
        result.wallet.setEur(r.getDouble("eur"));
        result.wallet.setYen(r.getDouble("yen"));
        result.wallet.setGbp(r.getDouble("gbp"));

        return result;
    }

    public static User getUserFromDatabaseByEmail(String email) throws SQLException {
        ResultSet r = db.getStatement().executeQuery(STR."SELECT * FROM users WHERE email = '\{email}'");
        if(!r.next())throw new EmailNotFoundException();
        User result = new User();
        result.id = r.getInt("id");
        result.firstName = r.getString("firstName");
        result.lastName = r.getString("lastName");
        result.phoneNumber = r.getString("phoneNumber");
        result.email = r.getString("email");
        result.username = r.getString("username");
        result.password = r.getString("password");
        return result;
    }

}
