module org.example.onlineexchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;


    opens org.example.onlineexchange to javafx.fxml;
    exports org.example.onlineexchange;
    exports org.example.onlineexchange.ClientApp;
    opens org.example.onlineexchange.ClientApp to javafx.fxml;
}