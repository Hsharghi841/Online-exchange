package org.example.onlineexchange.ClientApp;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Objects;
import java.util.Scanner;

public class ClientSocket {

    private static int defaultPort = 8000;
    private static String defaultIP = "localhost";

    private static ClientSocket cl;

    private final int port;
    private final String IP;

    private static Socket socket ;

    private final Scanner scanner;
    private final Formatter printer;

    private ClientSocket(int port, String IP) throws IOException {
        this.port = port;
        this.IP = IP;

        socket = new Socket(IP, port);

        scanner = new Scanner(socket.getInputStream());
        printer = new Formatter(socket.getOutputStream());


    }

    public static ClientSocket getClientSocket (int port, String IP) throws IOException {
        if(cl == null){
            cl = new ClientSocket(port, IP);
            return cl;
        }

        if(cl.port == port && Objects.equals(cl.IP, IP)){
            return cl;
        }else {
            return cl = new ClientSocket(port, IP);
        }
    }

    public static ClientSocket getClientSocket () throws IOException {
        if(cl == null){
            cl = new ClientSocket(defaultPort, defaultIP);
            return cl;
        }
        return cl;
    }

    public void send(String mail){
        printer.format(mail);
    }

    public String receive(){
        return scanner.next();
    }

    public static Socket getSocket() {
        return socket;
    }
}
