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

import java.io.IOException;

public class Withdrow {

    boolean done;
    double from;
    User user=User.user;
    @FXML
    TextField textfield;
    @FXML
    ChoiceBox<String> choiceBox1;
    @FXML
    ChoiceBox<String> choiceBox2;
    @FXML
    Label value;

    public void WITHHandler(Event e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("wallet-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
    public void valuefrom(){
        done=false;
        if(choiceBox1.getValue()=="USD"){
            if(Double.valueOf(textfield.getText())>0&&Double.valueOf(textfield.getText())<user.value[1]){
                if(choiceBox2.getValue()=="USD"){
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[0].getPrice()*0.99)/HomepageController.coins[0].getPrice()));
                } else if (choiceBox2.getValue()=="EUR") {
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[0].getPrice()*0.99)/HomepageController.coins[1].getPrice()));
                }else if (choiceBox2.getValue()=="YEN") {
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[0].getPrice()*0.99)/HomepageController.coins[3].getPrice()));
                }else if (choiceBox2.getValue()=="GBP") {
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[0].getPrice()*0.99)/HomepageController.coins[4].getPrice()));
                }
                done=true;
            }
        }else if(choiceBox1.getValue()=="EUR"){
            if(Double.valueOf(textfield.getText())>0&&Double.valueOf(textfield.getText())<user.value[2]){
                if(choiceBox2.getValue()=="USD"){
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[1].getPrice()*0.99)/HomepageController.coins[0].getPrice()));
                } else if (choiceBox2.getValue()=="EUR") {
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[1].getPrice()*0.99)/HomepageController.coins[1].getPrice()));
                }else if (choiceBox2.getValue()=="YEN") {
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[1].getPrice()*0.99)/HomepageController.coins[3].getPrice()));
                }else if (choiceBox2.getValue()=="GBP") {
                    value.setText(String.valueOf((Double.valueOf(textfield.getText())*HomepageController.coins[1].getPrice()*0.99)/HomepageController.coins[4].getPrice()));
                }
                done=true;
            }
        }
    }
}
