package org.example.onlineexchange.ClientApp;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.onlineexchange.Request;

import java.io.IOException;
import java.util.Objects;

public class trasfer {

    boolean done,finish=true;
    double from;
    User user = User.user;
    @FXML
    TextField textfield;
    @FXML
    TextField textfield2;
    @FXML
    ChoiceBox<String> choiceBox1;
    @FXML
    ChoiceBox<String> choiceBox2;
    @FXML
    Label value;
    @FXML
    Label v;

    public void WITHHandler(Event e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("wallet-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    public void Done(){

            ClientSocket cs;
            try {
                cs = ClientSocket.getClientSocket();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            cs.send(new Request("TRANSFER",textfield.getText(),choiceBox1.getValue().toLowerCase(), textfield2.getText(),String.valueOf(Double.valueOf(textfield.getText())*0.01)).toString());
            finish=false;
            v.setText("معامله انجام شد به کیف پول برگردید");


    }
}
