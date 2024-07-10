package org.example.onlineexchange.ClientApp;

import javafx.css.converter.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.onlineexchange.Coins.Orders;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExchengPage implements Initializable {
    User user;
    boolean True;
    boolean sell;
    String Coin;
    @FXML
    Label Value;
    @FXML
    Label Error;
    @FXML
    VBox SELL;
    @FXML
    VBox BUY;
    @FXML
    ChoiceBox<String> choiceBox;
    @FXML
    Slider slid;
    @FXML
    TextField text;

    public void SellOrders(double value, double price){
        try {
            HBox child= new FXMLLoader(ClientApplication.class.getResource("SELLHBox.fxml")).load();
            ((Label)((AnchorPane)child.getChildren().get(0)).getChildren()).setText(String.valueOf(value));
            ((Label)((AnchorPane)child.getChildren().get(1)).getChildren()).setText(String.valueOf(price));
            SELL.getChildren().add(child);
        } catch (IOException e) {
            System.out.println("cant open");
        }

    }

    public void BuyOrders(double value, double price){
        try {
            HBox child= new FXMLLoader(ClientApplication.class.getResource("BUYHBox.fxml")).load();
            ((Label)((AnchorPane)child.getChildren().get(0)).getChildren()).setText(String.valueOf(value));
            ((Label)((AnchorPane)child.getChildren().get(1)).getChildren()).setText(String.valueOf(price));
            BUY.getChildren().add(child);
        } catch (IOException e) {
            System.out.println("cant open");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user=User.user;
        choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Coin=newValue;
            if(newValue=="USD") {
                slid.setMax(user.wallet.getUsd());
            } else if(newValue=="EUR") {
                slid.setMax(user.wallet.getEur());
            }else if(newValue=="YEN") {
                slid.setMax(user.wallet.getYen());
            }else if(newValue=="GBP") {
                slid.setMax(user.wallet.getGbp());
            }
        });
    }

    public void Chengevalue(){
        Value.setText(String.valueOf(slid.getValue()));
    }

    public void check(){
        True=false;
        if(Double.valueOf(text.getText())>0){
            True=true;
            Error.setText("این مقدار معتبر است");
        }else{
            Error.setText("این مقدار نا معتبر است");
        }
    }

    public  void Sell(){
        sell=true;
    }
    public  void buy(){
        sell=false;
    }
    public void Done(){
        if(True){
            user.orders.add(new Orders(slid.getValue(),Double.valueOf(text.getText()),sell,Coin));
            if(sell)
                SellOrders(slid.getValue(),Double.valueOf(text.getText()));
            else
                BuyOrders(slid.getValue(),Double.valueOf(text.getText()));
        }
    }
}
