package org.example.onlineexchange.ClientApp;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.onlineexchange.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class adminViewController implements Initializable {


    @FXML
    VBox v;
    @FXML
    Button bazarBtn;

    ClientSocket cs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            cs = ClientSocket.getClientSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cs.send(new Request("GET ADMIN").toString());

        Request r = Request.requestProcessor(cs.receive());
        int i = 0;
        while (true){
            try {
                HBox child = null;
                try {
                    child = new FXMLLoader(ClientApplication.class.getResource("admim-list.fxml")).load();
                } catch (IOException e) {
                    System.out.println("cant open");
                }

                v.getChildren().add(child);

                i++;

            }catch (RuntimeException e){

            }


        }


    }

    public void ekhtelasHandler(){
        cs.send(new Request("EKHTELAS").toString());

        Request r = Request.requestProcessor(cs.receive());

    }

    public void bazarHandler(){
        if(Objects.equals(bazarBtn.getText(), "بستن بازار")){

            cs.send(new Request("LOCK").toString());

            Request r = Request.requestProcessor(cs.receive());
            if()

        }



    }

    public void backHandler(Event e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("home-page.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}
