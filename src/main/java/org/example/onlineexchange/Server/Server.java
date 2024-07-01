package org.example.onlineexchange.Server;


import org.example.onlineexchange.Coins.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class Server {


    public ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    ServerSocket serverSocket;

    private Coin coins[] = new Coin[5], temp;
    private Server server=this;

    public Server() {

        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            System.err.println("Server socket error.");
        }
        coins[0] = new USD();
        coins[1] = new EUR();
        coins[2] = new TOMAN();
        coins[3] = new YEN();
        coins[4] = new GBP();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Socket clientSk;
                    try {
                        clientSk = serverSocket.accept();
                    } catch (IOException e) {
                        System.err.println("Server socket accepting error. tying again ...");
                        continue;
                    }

                    ClientHandler clientHandler;
                    try {
                        clientHandler = new ClientHandler(clientSk, server);
                    } catch (IOException e) {
                        System.err.println("client socket error. tying again ...");
                        continue;
                    }

                    System.out.println("a client accepted!");

                    int i;
                    for (i = 0; i < clients.size(); i++) {
                        if (!Objects.equals(clients.get(i).name, STR."client \{i + 1}")) {
                            break;
                        }
                    }
                    clients.add(i, clientHandler);
                    clientHandler.name = STR."client \{clients.indexOf(clientHandler) + 1}";
                    System.out.println(STR."this client named : '\{clientHandler.name}'");
                    System.out.println("=====================================");

                    new Thread(clientHandler).start();
                }
            }
        }.start();
        new Thread(){
            private String input;
            private String[] orders;
            BufferedReader read;
            @Override
            public void run() {
                try {
                    read = new BufferedReader(new FileReader("currency_prices (3).csv"));
                } catch (FileNotFoundException e) {
                    System.out.println("please restart the server(cant open csv file)!");
                }
                for (int i = 0; i < 3; i++) {
                    try {
                        input = read.readLine();
                    } catch (IOException e) {
                        System.out.println("cant read");
                    }
                }
                orders=input.split(",");
                coins[0].setPrice1(Double.valueOf(orders[2]));
                coins[0].setMaxprice(Double.valueOf(orders[2]));
                coins[0].setMinprice(Double.valueOf(orders[2]));
                coins[0].setPercentchenge(0.0);
                coins[1].setPrice1(Double.valueOf(orders[3]));
                coins[1].setMaxprice(Double.valueOf(orders[3]));
                coins[1].setMinprice(Double.valueOf(orders[3]));
                coins[1].setPercentchenge(0.0);
                coins[1].setPrice1(Double.valueOf(orders[4]));
                coins[1].setMaxprice(Double.valueOf(orders[4]));
                coins[1].setMinprice(Double.valueOf(orders[4]));
                coins[1].setPercentchenge(0.0);
                coins[3].setPrice1(Double.valueOf(orders[5]));
                coins[3].setMaxprice(Double.valueOf(orders[5]));
                coins[3].setMinprice(Double.valueOf(orders[5]));
                coins[3].setPercentchenge(0.0);
                coins[4].setPrice1(Double.valueOf(orders[6]));
                coins[4].setMaxprice(Double.valueOf(orders[6]));
                coins[4].setMinprice(Double.valueOf(orders[6]));
                coins[4].setPercentchenge(0.0);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("cant sleep in server");
                }
                for (int j = 0; j <1440; j++){
                    for (int i = 0; i < 2; i++) {
                        try {
                            input = read.readLine();
                        } catch (IOException e) {
                            System.out.println("cant read");
                        }
                    }
                    orders=input.split(",");
                    coins[0].setPrice(Double.valueOf(orders[2]));
                    coins[0].setMaxprice(Double.valueOf(orders[2]));
                    coins[0].setMinprice(Double.valueOf(orders[2]));
                    coins[0].setPercentchenge((coins[0].getPrice()-coins[0].getPastprice())*100/coins[0].getPastprice());
                    coins[1].setPrice(Double.valueOf(orders[3]));
                    coins[1].setMaxprice(Double.valueOf(orders[3]));
                    coins[1].setMinprice(Double.valueOf(orders[3]));
                    coins[1].setPercentchenge((coins[1].getPrice()-coins[1].getPastprice())*100/coins[1].getPastprice());
                    coins[1].setPrice(Double.valueOf(orders[4]));
                    coins[1].setMaxprice(Double.valueOf(orders[4]));
                    coins[1].setMinprice(Double.valueOf(orders[4]));
                    coins[1].setPercentchenge((coins[2].getPrice()-coins[2].getPastprice())*100/coins[2].getPastprice());
                    coins[3].setPrice(Double.valueOf(orders[5]));
                    coins[3].setMaxprice(Double.valueOf(orders[5]));
                    coins[3].setMinprice(Double.valueOf(orders[5]));
                    coins[3].setPercentchenge((coins[3].getPrice()-coins[3].getPastprice())*100/coins[3].getPastprice());
                    coins[4].setPrice(Double.valueOf(orders[6]));
                    coins[4].setMaxprice(Double.valueOf(orders[6]));
                    coins[4].setMinprice(Double.valueOf(orders[6]));
                    coins[4].setPercentchenge((coins[4].getPrice()-coins[4].getPastprice())*100/coins[4].getPastprice());
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("cant sleep in server in loop");
                    }
                }
            }
        }.start();
    }

    public Coin[] getCoins() {
        return coins;
    }

    public static void main(String[] args) {
        new Server();
    }
}
