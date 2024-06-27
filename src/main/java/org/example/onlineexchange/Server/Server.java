package org.example.onlineexchange.Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

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

            int i;
            for (i = 0;i < clients.size(); i++) {
                if(!Objects.equals(clients.get(i).name, STR."client \{i + 1}")){
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



    public static void main(String[] args) {
        new Server();
    }
}
