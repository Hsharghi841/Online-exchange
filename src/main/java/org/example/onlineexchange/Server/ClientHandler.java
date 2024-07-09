package org.example.onlineexchange.Server;

import org.example.onlineexchange.Coins.*;
import javafx.scene.control.Alert;
import org.example.onlineexchange.EmailSender;
import org.example.onlineexchange.Exceptions.EmailNotFoundException;
import org.example.onlineexchange.Exceptions.UserNameNotFoundException;
import org.example.onlineexchange.Request;
import org.example.onlineexchange.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    public String name;

    private final Socket socket;
    private final Printer sender;
    private final Scanner receiver;
    private final Server server;

    private User loginedUser;

    private static final String OPERATOR_EMAIL = "online.exchange.project@gmail.com";
    private static final String OPERATOR_EMAIL_PASSWORD = "pnixokhcnqrixqmp";
    private String input, output;
    private String[] orders;
    Scanner scanner;
    Formatter formatter;
    private Coin coins[] = new Coin[5];
    private boolean updated[] = {false, false, false, false, false};


    ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        sender = new Printer(socket.getOutputStream());
        receiver = new Scanner(socket.getInputStream());
        scanner = new Scanner(socket.getInputStream());
        formatter = new Formatter(socket.getOutputStream());
        coins[0] = new USD();
        coins[1] = new EUR();
        coins[2] = new TOMAN();
        coins[3] = new YEN();
        coins[4] = new GBP();
    }

    @Override
    public void run() {
        while (true) {
            Request request;
            try {
                input = receiver.nextLine();
                orders = input.split(",");
                request = Request.requestProcessor(input);
            } catch (NoSuchElementException e) {
                break;
            }
            System.out.println(STR."a msg received from : '\{name}'");

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

            if(Objects.equals(request.getCommand(), "FORGET PASSWORD")){

                try {

                    User currentUser = User.getUserFromDatabaseByEmail(request.getParameter(0));

                    EmailSender emailSender = new EmailSender(OPERATOR_EMAIL, OPERATOR_EMAIL_PASSWORD);
                    emailSender.send("Your Requested Password",
                            STR."""                
                                    Dear""" + ' ' + currentUser.getUsername() + '\n' +
                                    """
                                                                                
                                            We received a request to send you the password for your account associated with this email address. Please find your password below:
                                                                        
                                            Password:""" + ' ' + currentUser.getPassword() + '\n' +
                                    """                     
                                                                                
                                            For security reasons, we recommend that you keep your password confidential and do not share it with anyone. If you have any concerns about the security of your account, please consider changing your password.
                                                                        
                                            If you have any questions or need further assistance, please do not hesitate to contact us.
                                                                        
                                            Best regards,
                                            Support Team
                                            """, currentUser.getEmail()
                    );

                    sender.format(new Request("SUCCESS").toString());
                }catch (EmailNotFoundException e){
                    sender.format(new Request("EMAIL NOT FOUND").toString());
                } catch (SQLException | MessagingException e) {
                    sender.format(new Request("FAILED").toString());
                    continue;
                }
                continue;
            }
            System.out.println(input);
            if (orders[0].equals("[UPDATE]")) {
                for (int i = 0; !updated[0] || !updated[1] || !updated[2] || !updated[3] || !updated[4]; i++) {
                    i=i%5;
                    if (!updated[i]) {
                        System.out.println("123");
                        for (int j = 0; j < 4; ) {
                            if (j == 0) {
                                if (server.getCoins()[i].getPrice() != coins[i].getPrice()) {
                                    coins[i].setPrice(server.getCoins()[i].getPrice());
                                    sender.format(output="[PRICECHENGE]," + coins[i].getName() + "," + String.valueOf(coins[i].getPrice())+"\n");
                                    System.out.println(output);
                                } else {
                                    sender.format("[PRICECHENGEUPDATED]"+"\n");
                                }
                                input = receiver.nextLine();
                                //System.out.println(input);
                                orders = input.split(",");
                                j = Integer.valueOf(orders[1]);
                                //System.out.println(orders[1]);
                            } else if (j == 1) {
                                if (server.getCoins()[i].getMaxprice() != coins[i].getMaxprice()) {
                                    coins[i].setMaxprice(server.getCoins()[i].getMaxprice());
                                    sender.format("[MAXPRICECHENGE]" + "," + coins[i].getName() + "," + String.valueOf(coins[i].getMaxprice())+"\n");
                                } else {
                                    sender.format("[MAXPRICECHENGEUPDATED]"+"\n");
                                }
                                input = receiver.nextLine();
                                //System.out.println(input);
                                orders = input.split(",");
                                j = Integer.valueOf(orders[1]);
                                System.out.println(j);
                            } else if (j == 2) {
                                if (server.getCoins()[i].getPercentchenge() != coins[i].getPercentchenge()) {
                                    coins[i].setPercentchenge(server.getCoins()[i].getPercentchenge());
                                    System.out.println("percent chenge ok");
                                    sender.format("[PERCENTCHENGECHENGE]" + "," + coins[i].getName() + "," + String.valueOf(coins[i].getPercentchenge())+"\n");
                                } else {
                                    sender.format("[PERCENTCHENGECHENGEUPDATED]"+"\n");
                                }
                                input = receiver.nextLine();
                                //System.out.println(input);
                                orders = input.split(",");
                                j = Integer.valueOf(orders[1]);
                            } else if (j == 3) {
                                if (server.getCoins()[i].getMinprice() != coins[i].getMinprice()) {
                                    coins[i].setMinprice(server.getCoins()[i].getMinprice());
                                    System.out.println("ok set");
                                    sender.format("[MINPRICECHENGE]" + "," + coins[i].getName() + "," + String.valueOf(coins[i].getMinprice())+"\n");
                                } else {
                                    sender.format("[MINPRICECHENGEUPDATED]"+"\n");
                                    System.out.println("not ok set");
                                }
                                input = receiver.nextLine();
                                //System.out.println(input);
                                orders = input.split(",");
                                j = Integer.valueOf(orders[1]);
                            }
                        }
                        System.out.println(updated);
                        updated[i]=true;
                    }
                }
                System.out.println("finish");
                for (int i = 0; i <5; i++) {
                    updated[i]=false;
                }
            }

        }

        sender.close();
        receiver.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(STR."'\{name}' disconnected");
        server.clients.remove(this);
    }

    private static class Printer {
        private final Formatter formatter;
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
