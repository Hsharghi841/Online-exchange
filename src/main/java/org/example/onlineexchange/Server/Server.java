package org.example.onlineexchange.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    ServerSocket Server;
    int serverPort = 8080;
    public Server() {
        ArrayList<Thread> threads = new ArrayList<>();
        try {
            Server = new ServerSocket(serverPort);

            System.out.println("Server init!");
            while (true) {
                Socket client = Server.accept();

                System.out.println("Connected to New Client!");

                Thread t = new Thread(new ClientHandler(client));

                threads.add(t);
                t.start();

            }
        } catch (IOException e) {
        }

    }

    public static void main(String[] args) {
        new Server();
    }


}
