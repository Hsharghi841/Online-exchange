package org.example.onlineexchange.ClientApp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import org.example.onlineexchange.Coins.*;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Formatter;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HomepageController implements Initializable {

    private Socket socket = ClientSocket.getSocket();

    private final Scanner scanner;
    private final Formatter printer;

    private boolean updated = false;

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

    {
        try {
            scanner = new Scanner(socket.getInputStream());
            printer = new Formatter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coins[0] = new USD();
        coins[1] = new EUR();
        coins[2] = new TOMAN();
        coins[3] = new YEN();
        coins[4] = new GBP();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!updated) {
                        updated=true;
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            System.out.println("cant sleep");
                        }
                        output = "[UPDATE]";
                        printer.format(output + "\n");
                    }
                    input = scanner.nextLine();
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
                        updated=false;
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
                        updated=false;
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
                        updated=false;
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
                        updated=false;
                    }
                }
            }
        }.start();
    }
}
