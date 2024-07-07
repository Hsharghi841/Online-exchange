package org.example.onlineexchange.ClientApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class tokenViewController implements Initializable {


    @FXML
    VBox v;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            AnchorPane parent = new FXMLLoader(ClientApplication.class.getResource("test.fxml")).load();
            v.getChildren().add(parent);
            parent = new FXMLLoader(ClientApplication.class.getResource("test.fxml")).load();
            v.getChildren().add(parent);
            ((Button)(((AnchorPane)parent).getChildren().getFirst())).setText("گاااااااوووو");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
