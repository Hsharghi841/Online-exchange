package org.example.onlineexchange.ClientApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.example.onlineexchange.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WalletController implements Initializable {

    @FXML
    Label totalAssets;
    @FXML
    Label usd;
    @FXML
    Label eur;
    @FXML
    Label yen;
    @FXML
    Label gbp;

    ClientSocket cs;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            cs = ClientSocket.getClientSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cs.send(new Request("TOTAL ASSETS").toString());

        Request r = Request.requestProcessor(cs.receive());

        if(!Objects.equals(r.getCommand(), "SUCCESS")){
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText("failed");
            err.showAndWait();
            return;
        }

        totalAssets.setText(r.getParameter(0));

        cs.send(new Request("GET WALLET").toString());

        r = Request.requestProcessor(cs.receive());

        if(!Objects.equals(r.getCommand(), "SUCCESS")){
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText("failed");
            err.showAndWait();
            return;
        }

        usd.setText(r.getParameter(0));
        eur.setText(r.getParameter(1));
        yen.setText(r.getParameter(2));
        gbp.setText(r.getParameter(3));
    }
}
