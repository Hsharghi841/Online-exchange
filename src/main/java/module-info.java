module org.example.onlineexchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires mysql.connector.j;
    requires java.sql;


    opens org.example.onlineexchange to javafx.fxml;
    exports org.example.onlineexchange;
    exports org.example.onlineexchange.ClientApp;
    opens org.example.onlineexchange.ClientApp to javafx.fxml;
    exports org.example.onlineexchange.Server;
    opens org.example.onlineexchange.Server to javafx.fxml;
}