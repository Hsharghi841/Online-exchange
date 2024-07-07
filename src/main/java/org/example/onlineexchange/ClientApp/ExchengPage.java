package org.example.onlineexchange.ClientApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExchengPage implements Initializable {
    @FXML
    VBox SELL;
    @FXML
    VBox BUY;

    public void SellOrders(){
        try {
            HBox child= new FXMLLoader(ClientApplication.class.getResource("ordersHBox.fxml")).load();
            SELL.getChildren().add(child);
        } catch (IOException e) {
            System.out.println("cant open");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SellOrders();
    }
}
