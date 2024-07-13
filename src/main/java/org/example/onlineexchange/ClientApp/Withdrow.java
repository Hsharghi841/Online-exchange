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

public class Withdrow {

    boolean done,finish=true;
    double from;
    User user = User.user;
    @FXML
    TextField textfield;
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

    public void valuefrom() {
        done = false;
        if (Objects.equals(choiceBox1.getValue(), "USD")) {
            if (Double.valueOf(textfield.getText()) > 0 && Double.valueOf(textfield.getText()) < user.value[1]) {
                if (Objects.equals(choiceBox2.getValue(), "USD")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[0].getPrice() * 0.99) / HomepageController.coins[0].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "EUR")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[0].getPrice() * 0.99) / HomepageController.coins[1].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "YEN")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[0].getPrice() * 0.99) / HomepageController.coins[3].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "GBP")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[0].getPrice() * 0.99) / HomepageController.coins[4].getPrice()));
                }
                done = true;
            }
        } else if (Objects.equals(choiceBox1.getValue(), "EUR")) {
            if (Double.valueOf(textfield.getText()) > 0 && Double.valueOf(textfield.getText()) < user.value[2]) {
                if (Objects.equals(choiceBox2.getValue(), "USD")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[1].getPrice() * 0.99) / HomepageController.coins[0].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "EUR")) {
                    System.out.println("123");
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[1].getPrice() * 0.99) / HomepageController.coins[1].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "YEN")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[1].getPrice() * 0.99) / HomepageController.coins[3].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "GBP")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[1].getPrice() * 0.99) / HomepageController.coins[4].getPrice()));
                }
                done = true;
            }
        }else if (Objects.equals(choiceBox1.getValue(), "YEN")) {
            if (Double.valueOf(textfield.getText()) > 0 && Double.valueOf(textfield.getText()) < user.value[3]) {
                if (Objects.equals(choiceBox2.getValue(), "USD")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[3].getPrice() * 0.99) / HomepageController.coins[0].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "EUR")) {
                    System.out.println("123");
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[3].getPrice() * 0.99) / HomepageController.coins[1].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "YEN")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[3].getPrice() * 0.99) / HomepageController.coins[3].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "GBP")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[3].getPrice() * 0.99) / HomepageController.coins[4].getPrice()));
                }
                done = true;
            }
        }else if (Objects.equals(choiceBox1.getValue(), "GBP")) {
            if (Double.valueOf(textfield.getText()) > 0 && Double.valueOf(textfield.getText()) < user.value[4]) {
                if (Objects.equals(choiceBox2.getValue(), "USD")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[4].getPrice() * 0.99) / HomepageController.coins[0].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "EUR")) {
                    System.out.println("123");
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[4].getPrice() * 0.99) / HomepageController.coins[1].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "YEN")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[4].getPrice() * 0.99) / HomepageController.coins[3].getPrice()));
                } else if (Objects.equals(choiceBox2.getValue(), "GBP")) {
                    value.setText(String.valueOf(from=(Double.valueOf(textfield.getText()) * HomepageController.coins[4].getPrice() * 0.99) / HomepageController.coins[4].getPrice()));
                }
                done = true;
            }
        }
    }

    public void Done(){
        valuefrom();
        if(done&&finish) {
            ClientSocket cs;
            try {
                cs = ClientSocket.getClientSocket();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new Request("SWAP",textfield.getText(),choiceBox1.getValue(),String.valueOf(from),choiceBox2.getValue(),String.valueOf(Double.valueOf(textfield.getText())*0.01)).toString();
            finish=false;
            v.setText("معامله انجام شد به کیف پول برگردید");
        }
        else{
            v.setText("امکان انجام معامله وجود ندارد");
        }
    }
}
