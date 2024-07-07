package org.example.onlineexchange.ClientApp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.onlineexchange.Request;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class tokenViewController implements Initializable {

    @FXML
    LineChart lineChart;
    @FXML
    Label tokenNameLbl;
    @FXML
    Label tokenPriceLbl;


    Timeline chartTimeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chartTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> updater()));

    }

    public void init(String tokenName){
        tokenNameLbl.setText(tokenName);
        updater();
    }


    public void updater(){
        System.out.println("123");
        ClientSocket cl;
        try {
            cl = ClientSocket.getClientSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cl.send(new Request("GET PRICE", tokenNameLbl.getText()).toString());

        Request result = Request.requestProcessor(cl.receive());

        if (!Objects.equals(result.getCommand(), "SUCCESS"))return;

        tokenPriceLbl.setText(result.getParameter(0));

    }

}
