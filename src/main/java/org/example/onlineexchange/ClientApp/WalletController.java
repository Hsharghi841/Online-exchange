package org.example.onlineexchange.ClientApp;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.onlineexchange.Coins.Coin;
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
    User user=User.user;

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
        user.value[0]=Double.valueOf(r.getParameter(0));
        totalAssets.setText(r.getParameter(0));

        cs.send(new Request("GET WALLET").toString());

        r = Request.requestProcessor(cs.receive());

        if(!Objects.equals(r.getCommand(), "SUCCESS")){
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText("failed");
            err.showAndWait();
            return;
        }
        user.value[1]=Double.valueOf(r.getParameter(0));
        usd.setText(r.getParameter(0));
        user.value[2]=Double.valueOf(r.getParameter(1));
        eur.setText(r.getParameter(1));
        user.value[3]=Double.valueOf(r.getParameter(2));
        yen.setText(r.getParameter(2));
        user.value[4]=Double.valueOf(r.getParameter(3));
        gbp.setText(r.getParameter(3));
    }
    public void WITHHandler(Event e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("withdrow.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}
