package org.example.onlineexchange.ClientApp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.onlineexchange.Coins.*;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {

    private ClientSocket socket;

    private boolean updated = false;

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
    @FXML
    Label usernameLbl;


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
        Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), event -> UpDate()));
        timeline1.setCycleCount(2);
        timeline1.play();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(60), event -> UpDate()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void UpDate() {
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
                    USDPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 1) {
                    EURPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 3) {
                    YENPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 4) {
                    GBPPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
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
                    USDMAXPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 1) {
                    EURMAXPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));

                } else if (numcoinselection == 3) {
                    YENMAXPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 4) {
                    GBPMAXPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
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

                double d = Double.valueOf(orders[2]);
                d = ((double) Math.round(d * 100)) / 100;
                if (numcoinselection == 0) {
                    USDPERCENT.setText(STR."\{d}");
                } else if (numcoinselection == 1) {
                    EURPERCENT.setText(STR."\{d}");
                } else if (numcoinselection == 3) {
                    YENPERCENT.setText(STR."\{d}");
                } else if (numcoinselection == 4) {
                    GBPPERCENT.setText(STR."\{d}");
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
                    USDMINPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 1) {
                    EURMINPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 3) {
                    YENMINPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
                } else if (numcoinselection == 4) {
                    GBPMINPRICE.setText(String.valueOf(Math.round(Double.valueOf(orders[2]))));
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
        updated = false;
        System.out.println(updated);
    }

    void sort(int i, boolean ascending){
        ObservableList<Node> o1 = vbox.getChildren();

        ArrayList<Node> o2 = new ArrayList<>(o1);

        o2.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                HBox h1 = (HBox) o1;
                HBox h2 = (HBox) o2;

                Label l1 = (Label) ((AnchorPane)h1.getChildren().get(i)).getChildren().getFirst();
                Label l2 = (Label) ((AnchorPane)h2.getChildren().get(i)).getChildren().getFirst();

                double value1 = Double.valueOf(l1.getText());
                double value2 = Double.valueOf(l2.getText());

                if(ascending)return Double.compare(value1, value2);
                return Double.compare(value2, value1);
            }
        });

        o1.clear();

        for (int j = 0; j < o2.size(); j++) {
            o1.add(o2.get(j));
        }
    }

    boolean sorted1 = false;
    boolean sorted2 = false;
    boolean sorted3 = false;
    boolean sorted4 = false;

    public void SORTPRICE() {
        sort(1, !sorted1);
        sorted1 = !sorted1;
        sorted2 = false;
        sorted3 = false;
        sorted4 = false;
    }
    public void SORTPERCENT() {
        sort(2, !sorted2);
        sorted2 = !sorted2;
        sorted1 = false;
        sorted3 = false;
        sorted4 = false;
    }
    public void SORTMAXPRICE() {
        sort(3, !sorted3);
        sorted3 = !sorted3;
        sorted1 = false;
        sorted2 = false;
        sorted4 = false;
    }
    public void SORTMINPRICE() {
        sort(4, !sorted4);
        sorted4 = !sorted4;
        sorted1 = false;
        sorted2 = false;
        sorted3 = false;
    }

    @FXML
    public void token_clicked(MouseEvent e) throws IOException {
        System.out.println("***");
        String tokenName = ((Label) e.getSource()).getText();
        System.out.println("***");
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("token-view.fxml"));
        stage.setScene(new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight()));

        if(Objects.equals(tokenName, "USD(دلار)"))
            ((tokenViewController)fxmlLoader.getController()).init("USD");
        else if(Objects.equals(tokenName, "EUR(یورو)"))
            ((tokenViewController)fxmlLoader.getController()).init("EUR");
        else if(Objects.equals(tokenName, "YEN(ین)"))
            ((tokenViewController)fxmlLoader.getController()).init("YEN");
        else if(Objects.equals(tokenName, "GBP(پوند)"))
            ((tokenViewController)fxmlLoader.getController()).init("GBP");

    }

    public void mouseOnUsername(){
        usernameLbl.setUnderline(true);
    }

    public void mouseNotOnUsername(){
        usernameLbl.setUnderline(false);
    }

    public void usernameMouseClicked(MouseEvent e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("profile-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

}

