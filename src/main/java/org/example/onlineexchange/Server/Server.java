package org.example.onlineexchange.Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


    public ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    ServerSocket serverSocket;

    public Server() {

        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            System.err.println("Server socket error.");
        }

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
                clientHandler = new ClientHandler(clientSk, this);
            } catch (IOException e) {
                System.err.println("client socket error. tying again ...");
                continue;
            }

            System.out.println("a client accepted!");
            new Thread(clientHandler).start();

            clients.add(clientHandler);
        }
    }



    public static void main(String[] args) {
        new Server();
    }
}
