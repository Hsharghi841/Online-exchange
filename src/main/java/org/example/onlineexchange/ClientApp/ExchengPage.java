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
    boolean True,TRUEof;
    boolean sell;
    String Coin;
    ClientSocket socket;
    double max,value;

    @FXML
    Label Value;
    @FXML
    TextField Error;
    @FXML
    VBox SELL;
    @FXML
    VBox BUY;
    @FXML
    ChoiceBox<String> choiceBox;
    @FXML
    TextField slid;
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
        System.out.println("1245");
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
        System.out.println("12345");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user=User.user;
        choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Coin=newValue;
            if(newValue=="USD") {
                max=user.wallet.getUsd();
            } else if(newValue=="EUR") {
                max=user.wallet.getEur();
            }else if(newValue=="YEN") {
                max=user.wallet.getYen();
            }else if(newValue=="GBP") {
                max=user.wallet.getGbp();
            }
            System.out.println("1265845");
        });
    }

    public void Chengevalue(){
        TRUEof=false;
        if(Double.valueOf(slid.getText())>0) {
            value = Double.valueOf(slid.getText());
            TRUEof=true;
            System.out.println("111");
        }
    }

    public void check(){
        True=true;
        if(Double.valueOf(Error.getText())>0){
            True=true;
            //te.setText("این مقدار معتبر است");
        }else{
            //Error.setText("این مقدار نا معتبر است");
        }
        System.out.println("123645");
    }

    public  void Sell(){
        sell=true;
        System.out.println("145");
    }
    public  void buy(){
        sell=false;
        System.out.println("45");
    }
    public void Done(){
        System.out.println(True);
        System.out.println(TRUEof);
        if(True&&TRUEof){
            try {
                socket = ClientSocket.getClientSocket();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.orders.add(new Orders(value,Double.valueOf(Error.getText()),sell,Coin));
            if(sell)
                SellOrders(value,Double.valueOf(Error.getText()));
            else
                BuyOrders(value,Double.valueOf(Error.getText()));
            System.out.println("1236745");
            ;
        }
    }
}
