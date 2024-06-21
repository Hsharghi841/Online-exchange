package org.example.onlineexchange.Server;

import org.example.onlineexchange.Exceptions.UserNameNotFoundException;
import org.example.onlineexchange.Request;
import org.example.onlineexchange.User;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final Printer sender;
    private final Scanner receiver;
    private final Server server;

    private User loginedUser;

    ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        sender = new Printer(socket.getOutputStream());
        receiver = new Scanner(socket.getInputStream());
    }

    @Override
    public void run() {
        while (true){
            Request request;
            try {
                request = Request.requestProcessor(receiver.nextLine());
            }catch (NoSuchElementException e){
                break;
            }
            System.out.println("a msg received");

            if(Objects.equals(request.getCommand(), "DISCONNECT")){
                break;
            }

            if (Objects.equals(request.getCommand(), "LOGIN")) {
                User requestedUser;
                try {
                    requestedUser = User.getUserFromDatabase(request.getParameter(0));
                } catch (SQLException e) {
                    System.err.println(
                            STR."sql exception in logining of \{request.getParameter(0)} \{request.getParameter(1)}");
                    sender.format(new Request("FAILED").toString());
                    continue;
                }catch (UserNameNotFoundException e){
                    sender.format(new Request("USER NOT FOUND").toString());
                    continue;
                }

                if(Objects.equals(requestedUser.getPassword(), request.getParameter(1))){
                    loginedUser = requestedUser;
                    System.out.println(STR."\{loginedUser.getFirstName()} \{loginedUser.getLastName()} logined");
                    sender.format(new Request("SUCCESS").toString());
                }else {
                    sender.format(new Request("PASSWORD NOT MATCH").toString());
                }
                continue;
            }

            if(Objects.equals(request.getCommand(), "SIGN IN")){
                try {
                    User.addUserToDatabase(new User(
                            request.getParameter(0), request.getParameter(1),
                            request.getParameter(2),request.getParameter(3),
                            request.getParameter(4),request.getParameter(5)
                            ));
                    sender.format(new Request("SUCCESS").toString());
                } catch (SQLException e) {
                    System.err.println(
                            STR."sql exception in sign in of \{request.getParameter(0)} \{request.getParameter(1)} " +
                                    request.getParameter(2) + " " + request.getParameter(3) + " " +
                                    request.getParameter(4) + " " + request.getParameter(5)
                    );
                    sender.format(new Request("FAILED").toString());
                }
                continue;
            }


        }

        sender.close();
        receiver.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.clients.remove(this);
    }

    private static class Printer {
        Formatter formatter;
        public Printer(OutputStream o){
            formatter = new Formatter(o);
        }
        public void format(String format){
            formatter.format(format);
            formatter.flush();
        }
        public void close() {
            formatter.close();
        }
    }
}
