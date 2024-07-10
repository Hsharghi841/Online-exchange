package org.example.onlineexchange.ClientApp;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.example.onlineexchange.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class profileViewController implements Initializable {

    @FXML
    Label nameLbl;
    @FXML
    Label usernameLbl;
    @FXML
    Label phoneNumLbl;
    @FXML
    Label emailLbl;


    User user;
    ClientSocket cs;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            cs = ClientSocket.getClientSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cs.send(new Request("PROFILE").toString());

        Request result = Request.requestProcessor(cs.receive());
        if(!Objects.equals(result.getCommand(), "SUCCESS"))return;

        nameLbl.setText(STR."\{result.getParameter(0)} \{result.getParameter(1)}");
        usernameLbl.setText(result.getParameter(2));
        phoneNumLbl.setText(result.getParameter(3));
        emailLbl.setText(result.getParameter(4));

        user = new User(result.getParameter(0), result.getParameter(1), result.getParameter(3),
                result.getParameter(4), result.getParameter(2), result.getParameter(5));



    }

    public void editPhoneNum(Event e) throws IOException {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("edit phone number");
        dialog.setHeaderText("Enter new phone number :");

        final String[] phoneNum = new String[1];
        dialog.showAndWait().ifPresent(s -> phoneNum[0] = s);

        try {
            user.setPhoneNumber(phoneNum[0]);
        }catch (RuntimeException e1){
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText(e1.getMessage());
            err.showAndWait();
            return;
        }

        cs.send(new Request("SET PHONE", phoneNum[0]).toString());

        Request result = Request.requestProcessor(cs.receive());

        if(!Objects.equals(result.getCommand(), "SUCCESS")){
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText("failed");
            err.showAndWait();
            return;
        }

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText("phone number successfully changed!");
        info.showAndWait();

        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("profile-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
        System.out.println("ok");
    }

    public void editEmail(Event e) throws IOException {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("edit email");
        dialog.setHeaderText("Enter new email :");

        final String[] email = new String[1];
        dialog.showAndWait().ifPresent(s -> email[0] = s);

        try {
            user.setEmail(email[0]);
        }catch (RuntimeException e1){
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText(e1.getMessage());
            err.showAndWait();
            return;
        }

        cs.send(new Request("SET EMAIL", email[0]).toString());

        Request result = Request.requestProcessor(cs.receive());

        if(!Objects.equals(result.getCommand(), "SUCCESS")){
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText("failed");
            err.showAndWait();
            return;
        }

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText("email successfully changed!");
        info.showAndWait();

        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("profile-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}
