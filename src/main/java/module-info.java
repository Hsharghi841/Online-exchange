module org.example.onlineexchange {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.onlineexchange to javafx.fxml;
    exports org.example.onlineexchange;
    exports org.example.onlineexchange.Server;
    opens org.example.onlineexchange.Server to javafx.fxml;
}