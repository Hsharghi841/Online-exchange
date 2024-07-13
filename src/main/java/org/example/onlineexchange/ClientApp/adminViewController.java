package org.example.onlineexchange.ClientApp;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.example.onlineexchange.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class adminViewController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClientSocket cs = null;
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
                try {
                    HBox child = new FXMLLoader(ClientApplication.class.getResource("admim-list.fxml")).load();
                } catch (IOException e) {
                    System.out.println("cant open");
                }

            }catch (RuntimeException e){

            }


        }







    }
}
