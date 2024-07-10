package org.example.onlineexchange.ClientApp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.onlineexchange.Request;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Level;

public class tokenViewController implements Initializable {

    @FXML
    LineChart lineChart;
    @FXML
    Label tokenNameLbl;
    @FXML
    Label tokenPriceLbl;


    Timeline chartTimeline;
    int interval = 1;
    XYChart.Series<String, Double> series = new XYChart.Series<>();
    ClientSocket cl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cl = ClientSocket.getClientSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lineChart.getData().add(series);

        chartTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> updater()));
        chartTimeline.setCycleCount(Animation.INDEFINITE);
        chartTimeline.play();
    }

    public void init(String tokenName){
        tokenNameLbl.setText(tokenName);
        updater();
    }


    public void updater(){

        cl.send(new Request("GET PRICE", tokenNameLbl.getText()).toString());
        Request result = Request.requestProcessor(cl.receive());
        if (!Objects.equals(result.getCommand(), "SUCCESS"))return;
        tokenPriceLbl.setText(STR."\{Math.round(Double.parseDouble(result.getParameter(0)))}");

        cl.send(new Request("GET CHART", tokenNameLbl.getText(), STR."\{interval}").toString());
        result = Request.requestProcessor(cl.receive());
        if (!Objects.equals(result.getCommand(), "SUCCESS"))return;
        LocalTime time = LocalTime.parse(result.getParameter(0));

        series.getData().clear();

        for (int i = 1; i <= 15; i++) {
            String value = result.getParameter(i);
            if(Objects.equals(value, "null"))value = "0.0";
            series.getData().add(new XYChart.Data<>(time.minusMinutes((long) (15 - i) * interval).format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    Double.parseDouble(value)));
        }

        int n = 15;
        double sumX = 0.0, sumY = 0.0, sumXY = 0.0, sumX2 = 0.0;

        for (int i = 1; i <= n; i++) {
            String value = result.getParameter(i);
            if(Objects.equals(value, "null"))value = "0.0";
            sumX += i;
            sumY += Double.parseDouble(value);
            sumXY += i * Double.parseDouble(value);
            sumX2 += i * i;
        }

        double xMean = sumX / n;
        double yMean = sumY / n;

        double slope = (sumXY - sumX * yMean) / (sumX2 - sumX * xMean);
        double intercept = yMean - slope * xMean;

        double next = intercept + slope * 16;

        series.getData().add(new XYChart.Data<>("future", next));

        System.out.println("chart change");

    }

    public void oneMinute(){interval = 1;}
    public void fifteenMinute(){interval = 15;}
    public void sixtyMinute(){interval = 60;}


}
