package org.example.onlineexchange.ClientApp;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.onlineexchange.Coins.*;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Comparator;
import java.util.Formatter;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

public class HomepageController implements Initializable {

    private ClientSocket socket;

    private boolean updated = false,sorted[]={false,false,false,false};

    private int numofresive = 0;
    private String input, output;
    private String[] orders;
    private Coin coins[] = new Coin[5], temp;
    private int numcoinselection;
    @FXML
    Label USDPRICE;
    @FXML
    Label EURPRICE;
    @FXML
    Label TOMANPRICE;
    @FXML
    Label YENPRICE;
    @FXML
    Label GBPPRICE;
    @FXML
    Label USDMAXPRICE;
    @FXML
    Label EURMAXPRICE;
    @FXML
    Label TOMANMAXPRICE;
    @FXML
    Label YENMAXPRICE;
    @FXML
    Label GBPMAXPRICE;
    @FXML
    Label USDPERCENT;
    @FXML
    Label EURPERCENT;
    @FXML
    Label TOMANPERCENT;
    @FXML
    Label YENPERCENT;
    @FXML
    Label GBPPERCENT;
    @FXML
    Label USDMINPRICE;
    @FXML
    Label EURMINPRICE;
    @FXML
    Label TOMANMINPRICE;
    @FXML
    Label YENMINPRICE;
    @FXML
    Label GBPMINPRICE;
    @FXML
    VBox vbox;
    @FXML
    HBox USDHBOX;
    @FXML
    HBox EURHBOX;
    @FXML
    HBox TOMANHBOX;
    @FXML
    HBox YENHBOX;
    @FXML
    HBox GBPHBOX;
    @FXML
    Label PRICE;
    @FXML
    Label PERCENT;
    @FXML
    Label MAXPRICE;
    @FXML
    Label MINPRICE;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            socket = ClientSocket.getClientSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        coins[0] = new USD();
        coins[1] = new EUR();
        coins[2] = new TOMAN();
        coins[3] = new YEN();
        coins[4] = new GBP();
        Timeline timeline1 =new Timeline(new KeyFrame(Duration.seconds(1),event -> UpDate()));
        timeline1.setCycleCount(2);
        timeline1.play();
        Timeline timeline =new Timeline(new KeyFrame(Duration.seconds(60),event -> UpDate()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void UpDate(){
        while (true) {
            if (!updated) {
                numofresive = 0;
                updated = true;
                output = "[UPDATE]";
                socket.send(output + "\n");
            }
            input = socket.receive();
            orders = input.split(",");
            if (orders[0].equals("[PRICECHENGE]")) {
                for (int i = 0; i < coins.length; i++) {
                    if (orders[1].equals(coins[i].getName())) {
                        temp = coins[i];
                        numcoinselection = i;
                        break;
                    }
                }
                temp.setPrice(Double.valueOf(orders[2]));
                if (numcoinselection == 0) {
                    USDPRICE.setText(orders[2]);
                } else if (numcoinselection == 1) {
                    EURPRICE.setText(orders[2]);
                } else if (numcoinselection == 2) {
                    TOMANPRICE.setText(orders[2]);
                } else if (numcoinselection == 3) {
                    YENPRICE.setText(orders[2]);
                } else if (numcoinselection == 4) {
                    GBPPRICE.setText(orders[2]);
                }
                numofresive++;
                socket.send("[SUCCSFUL],1" + "\n");
            } else if (orders[0].equals("[PRICECHENGEUPDATED]")) {
                numofresive++;
                socket.send("[SUCCSFUL],1" + "\n");
            } else if (orders[0].equals("[MAXPRICECHENGE]")) {
                for (int i = 0; i < coins.length; i++) {
                    if (orders[1].equals(coins[i].getName())) {
                        temp = coins[i];
                        numcoinselection = i;
                        break;
                    }
                }
                temp.setMaxprice(Double.valueOf(orders[2]));
                if (numcoinselection == 0) {
                    USDMAXPRICE.setText(orders[2]);
                } else if (numcoinselection == 1) {
                    EURMAXPRICE.setText(orders[2]);
                } else if (numcoinselection == 2) {
                    TOMANMAXPRICE.setText(orders[2]);
                } else if (numcoinselection == 3) {
                    YENMAXPRICE.setText(orders[2]);
                } else if (numcoinselection == 4) {
                    GBPMAXPRICE.setText(orders[2]);
                }
                numofresive++;
                socket.send("[SUCCSFUL],2" + "\n");
            } else if (orders[0].equals("[MAXPRICECHENGEUPDATED]")) {
                numofresive++;
                socket.send("[SUCCSFUL],2" + "\n");
            } else if (orders[0].equals("[PERCENTCHENGECHENGE]")) {
                for (int i = 0; i < coins.length; i++) {
                    if (orders[1].equals(coins[i].getName())) {
                        temp = coins[i];
                        numcoinselection = i;
                        break;
                    }
                }
                temp.setPercentchenge(Double.valueOf(orders[2]));
                if (numcoinselection == 0) {
                    USDPERCENT.setText(orders[2]);
                } else if (numcoinselection == 1) {
                    EURPERCENT.setText(orders[2]);
                } else if (numcoinselection == 2) {
                    TOMANPERCENT.setText(orders[2]);
                } else if (numcoinselection == 3) {
                    YENPERCENT.setText(orders[2]);
                } else if (numcoinselection == 4) {
                    GBPPERCENT.setText(orders[2]);
                }
                numofresive++;
                socket.send("[SUCCSFUL],3" + "\n");
            } else if (orders[0].equals("[PERCENTCHENGECHENGEUPDATED]")) {
                numofresive++;
                socket.send("[SUCCSFUL],3" + "\n");
            } else if (orders[0].equals("[MINPRICECHENGE]")) {
                for (int i = 0; i < coins.length; i++) {
                    if (orders[1].equals(coins[i].getName())) {
                        temp = coins[i];
                        numcoinselection = i;
                        break;
                    }
                }
                temp.setMinprice(Double.valueOf(orders[2]));
                if (numcoinselection == 0) {
                    USDMINPRICE.setText(orders[2]);
                } else if (numcoinselection == 1) {
                    EURMINPRICE.setText(orders[2]);
                } else if (numcoinselection == 2) {
                    TOMANMINPRICE.setText(orders[2]);
                } else if (numcoinselection == 3) {
                    YENMINPRICE.setText(orders[2]);
                } else if (numcoinselection == 4) {
                    GBPMINPRICE.setText(orders[2]);
                }
                socket.send("[SUCCSFUL],4" + "\n");
                numofresive++;
            } else if (orders[0].equals("[MINPRICECHENGEUPDATED]")) {
                numofresive++;
                socket.send("[SUCCSFUL],4" + "\n");
            }
            System.out.println(numofresive);
            if (numofresive == 20) {
                break;
            }
        }
        updated=false;
        System.out.println(updated);
    }

    public void SORTPRICE(){
        ObservableList observableList=vbox.getChildren();
        vbox.getChildren().removeAll();
        if(!sorted[0]){
            observableList.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(1);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(1);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? -1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:1);
                }
            });
            sorted[0]=true;
        }else{
            vbox.getChildren().sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(1);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(1);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? 1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:-1);
                }
            });
            sorted[0]=false;
        }
        vbox.getChildren().addAll(observableList);
    }

    public void SORTPERCENT(){
        if(!sorted[1]){
            vbox.getChildren().sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(2);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(2);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? -1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:1);
                }
            });
            sorted[1]=true;
        }else{
            vbox.getChildren().sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(2);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(2);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? 1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:-1);
                }
            });
            sorted[1]=false;
        }
    }

    public void SORTMAXPRICE(){
        if(!sorted[2]){
            vbox.getChildren().sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(3);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(3);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? -1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:1);
                }
            });
            sorted[2]=true;
        }else{
            vbox.getChildren().sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(3);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(3);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? 1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:-1);
                }
            });
            sorted[2]=false;
        }
    }

    public void SORTMINPRICE(){
        if(!sorted[3]){
            vbox.getChildren().sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(4);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(4);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? -1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:1);
                }
            });
            sorted[3]=true;
        }else{
            vbox.getChildren().sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    HBox o3=(HBox) o1;
                    HBox o4=(HBox) o2;
                    AnchorPane a1= (AnchorPane) o3.getChildren().get(4);
                    AnchorPane a2= (AnchorPane) o4.getChildren().get(4);
                    Label l1= (Label) a1.getChildren().get(0);
                    Label l2= (Label) a2.getChildren().get(0);
                    return Double.valueOf(l1.getText())<Double.valueOf(l2.getText())? 1:(Double.valueOf(l1.getText())==Double.valueOf(l2.getText())? 0:-1);
                }
            });
            sorted[3]=false;
        }
    }
}

