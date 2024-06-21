package org.example.onlineexchange;


import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.sql.Connection;

public class Database {

    private static String defaultUsername = "root";
    private static String defaultPassword = "123456789";
    private static String defaultUrl = "jdbc:mysql://localhost:3306";

    private static Database db;

    private final String username;
    private final String password;
    private final String url;


    private final Connection connection;
    private final Statement statement;

    private Database(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            statement.execute("USE online_exchange");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Database getDataBase (String username, String password, String url) throws IOException {
        if(db == null){
            db = new Database(username, password, url);
            return db;
        }

        if(Objects.equals(db.username, username) && Objects.equals(db.password, password) && Objects.equals(db.url, url)){
            return db;
        }else {
            try {
                db.statement.close();
                db.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return db = new Database(username, password, url);
        }
    }

    public static Database getDataBase (){
        if(db == null){
            db = new Database(defaultUsername, defaultPassword, defaultUrl);
            return db;
        }
        return db;
    }

    public Statement getStatement() {
        return statement;
    }
}
