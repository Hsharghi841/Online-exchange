package org.example.onlineexchange.Server;

import org.example.onlineexchange.Coins.*;
import org.example.onlineexchange.EmailSender;
import org.example.onlineexchange.Exceptions.EmailNotFoundException;
import org.example.onlineexchange.Exceptions.UserNameNotFoundException;
import org.example.onlineexchange.Request;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    Map<String, Integer> indexGetter = new HashMap<>();
    {
        indexGetter.put("USD", 0);
        indexGetter.put("EUR", 1);
        indexGetter.put("TOMAN", 2);
        indexGetter.put("YEN", 3);
        indexGetter.put("GBP", 4);
    }
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

            if (server.lock){
                if(Objects.equals(request.getCommand(), "UNLOCK")){
                    server.lock = false;
                    sender.format(new Request("SUCCESS").toString());
                    System.out.println("unlocked");
                    continue;
                }
                sender.format(new Request("FAILED").toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "LOCK")){
                server.lock = true;
                sender.format(new Request("SUCCESS").toString());
                System.out.println("locked");
                continue;
            }

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


            if (orders[0].equals("[UPDATE]")) {
                for (int i = 0; !updated[0] || !updated[1] || !updated[2] || !updated[3] || !updated[4]; i++) {
                    i=i%5;
                    if (!updated[i]) {
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
                                orders = input.split(",");
                                j = Integer.valueOf(orders[1]);
                            }
                        }
                        updated[i]=true;
                    }
                }

                for (int i = 0; i <5; i++) {
                    updated[i]=false;
                }
            }

            if (Objects.equals(request.getCommand(), "GET PRICE")){
                sender.format(new Request("SUCCESS",
                        String.valueOf(server.getCoins()[indexGetter.get(request.getParameter(0))].getPrice())).toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "GET CHART")){
                ArrayList<double[]> coinsPrice = server.getCoinsPrice();
                String[] avePrice = new String[16];
                synchronized (coinsPrice) {
                    System.out.println(request.toString());
                    int size = coinsPrice.size();
                    int coinIndex = indexGetter.get(request.getParameter(0));
                    System.out.println(request.getParameter(1));
                    int interval = Integer.parseInt(request.getParameter(1));
                    for (int i = 0; i < 15; i++) {
                        int j;
                        double sum = 0;
                        for (j = 0; j < interval; j++) {
                            int index = size - (i * interval + j) - 1;
                            if (index < 0)break;
                            sum += coinsPrice.get(index)[coinIndex];
                        }
                        avePrice[15 - i] = STR."\{sum / j}";
                        if (j != interval)break;
                    }
                }

                avePrice[0] = server.getTime();
                sender.format(new Request("SUCCESS", avePrice).toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "PROFILE")){
                try {
                    loginedUser = User.getUserFromDatabase(loginedUser.getUsername());
                } catch (SQLException e) {
                    sender.format(new Request("FAILED").toString());
                }
                sender.format(new Request("SUCCESS", loginedUser.getFirstName(), loginedUser.getLastName(),
                        loginedUser.getUsername(), loginedUser.getPhoneNumber(), loginedUser.getEmail(), loginedUser.getPassword()
                ).toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "SET PHONE")){
                Database db = Database.getDataBase();

                try {
                    db.getStatement().execute(STR."UPDATE users SET phoneNumber = '\{request.getParameter(0)}' WHERE id = \{loginedUser.getId()}");
                } catch (SQLException e) {
                    sender.format(new Request("FAILED").toString());
                    continue;
                }

                sender.format(new Request("SUCCESS").toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "SET EMAIL")){
                Database db = Database.getDataBase();

                try {
                    db.getStatement().execute(STR."UPDATE users SET email = '\{request.getParameter(0)}' WHERE id = \{loginedUser.getId()}");
                } catch (SQLException e) {
                    sender.format(new Request("FAILED").toString());
                    continue;
                }

                sender.format(new Request("SUCCESS").toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "TOTAL ASSETS")){
                try {
                    loginedUser = User.getUserFromDatabase(loginedUser.getUsername());
                } catch (SQLException e) {
                    sender.format(new Request("FAILED").toString());
                    continue;
                }

                double result = 0;

                result += loginedUser.wallet.getUsd() * server.getCoins()[0].getPrice();
                result += loginedUser.wallet.getEur() * server.getCoins()[1].getPrice();
                result += loginedUser.wallet.getEur() * server.getCoins()[1].getPrice();
                result += loginedUser.wallet.getYen() * server.getCoins()[3].getPrice();
                result += loginedUser.wallet.getGbp() * server.getCoins()[4].getPrice();

                sender.format(new Request("SUCCESS", String.valueOf(result)).toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "GET WALLET")){

                try {
                    loginedUser = User.getUserFromDatabase(loginedUser.getUsername());
                } catch (SQLException e) {
                    sender.format(new Request("FAILED").toString());
                    continue;
                }

                sender.format(new Request("SUCCESS", STR."\{loginedUser.wallet.getUsd()}",
                        STR."\{loginedUser.wallet.getEur()}", STR."\{loginedUser.wallet.getYen()}",
                        STR."\{loginedUser.wallet.getGbp()}").toString());
                continue;
            }

            if(Objects.equals(request.getCommand(), "UPDATE ORDERS")){
                Orders o = new Orders(Integer.parseInt(request.getParameter(0)), Integer.parseInt(request.getParameter(1)),
                        Boolean.parseBoolean(request.getParameter(2)), request.getParameter(3));
            }

            if(Objects.equals(request.getCommand(), "GET ADMIN")){

                Database db = Database.getDataBase();
                ResultSet r;
                try {
                    r = db.getStatement().executeQuery("SELECT u.username, w.usd, w.eur, w.yen, w.gbp FROM wallet w JOIN users u ON w.user_id = u.id");
                } catch (SQLException e) {
                    sender.format(new Request("FAILED").toString());
                    continue;
                }

                ArrayList<String> a = new ArrayList<>();

                try {
                    while (r.next()) {
                        a.add(r.getString(0));
                        a.add(r.getString(1));
                        a.add(r.getString(2));
                        a.add(r.getString(3));
                        a.add(r.getString(4));
                    }
                }catch (SQLException e){
                    sender.format(new Request("FAILED").toString());
                    continue;
                }

            }

            if(Objects.equals(request.getCommand(), "EKHTELAS")){

                double usdSum = 0.0;
                double eurSum = 0.0;
                double yenSum = 0.0;
                double gbpSum = 0.0;

                Database db = Database.getDataBase();
                ResultSet r;
                try {
                    r = db.getStatement().executeQuery("SELECT * FROM wallet");

                    while (r.next()){
                        usdSum += r.getDouble("usd");
                        eurSum += r.getDouble("eur");
                        yenSum += r.getDouble("yen");
                        gbpSum += r.getDouble("gbp");
                    }

                    db.getStatement().execute("UPDATE wallet SET usd = 0.0, eur = 0.0, yen = 0.0, gbp = 0.0");
                    db.getStatement().execute(STR."UPDATE wallet SET usd = \{usdSum}, eur = \{eurSum}, yen = \{yenSum}, gbp = \{gbpSum} WHERE user_id = 15");
                } catch (SQLException e) {
                    sender.format(new Request("FAILED").toString());
                    continue;
                }

            }

            if(Objects.equals(request.getCommand(), "SWAP")){
                Database db = Database.getDataBase();

                ResultSet r;
                try {
                    r = db.getStatement().executeQuery(STR."SELECT \{request.getParameter(1)}, \{request.getParameter(3)} FROM wallet WHERE user_id = \{loginedUser.getId()}" );
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                double f;
                double s;
                try {
                    r.next();
                    f = r.getDouble(request.getParameter(1));
                    s = r.getDouble(request.getParameter(3));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                f -= Double.parseDouble(request.getParameter(0));
                s += Double.parseDouble(request.getParameter(2));
                double t = 0;
                try {
                    db.getStatement().execute(STR."UPDATE wallet SET \{request.getParameter(1)} = \{f}, \{request.getParameter(3)} = \{s} WHERE user_id = \{loginedUser.getId()}");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    ResultSet r2 = db.getStatement().executeQuery(STR."SELECT \{request.getParameter(1)} FROM wallet WHERE user_id = 15");
                    r2.next();
                    t = r2.getDouble(request.getParameter(1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                t += Double.parseDouble(request.getParameter(4));
                try {
                    db.getStatement().execute(STR."UPDATE wallet SET \{request.getParameter(1)} = \{t} WHERE user_id = 15");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            if(Objects.equals(request.getCommand(), "TRANSFER")){
                Database db = Database.getDataBase();

                ResultSet r;
                try {
                    r = db.getStatement().executeQuery(STR."SELECT \{request.getParameter(1)} FROM wallet WHERE user_id = \{loginedUser.getId()}" );
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                double f;
                try {
                    r.next();
                    f = r.getDouble(request.getParameter(1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                f -= Double.parseDouble(request.getParameter(0));
                double t = 0;
                try {
                    db.getStatement().execute(STR."UPDATE wallet SET \{request.getParameter(1)} = \{f} WHERE user_id = \{loginedUser.getId()}");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    ResultSet r2 = db.getStatement().executeQuery(STR."SELECT \{request.getParameter(1)} FROM wallet WHERE user_id = 15");
                    r2.next();
                    t = r2.getDouble(request.getParameter(1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                t += Double.parseDouble(request.getParameter(4));
                try {
                    db.getStatement().execute(STR."UPDATE wallet SET \{request.getParameter(1)} = \{t} WHERE user_id = 15");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    ResultSet r3 = db.getStatement().executeQuery(STR."SELECT w.\{request.getParameter(1)}, u.id FROM users u JOIN wallet w ON u.id = w.user_id WHERE u.username = '\{request.getParameter(2)}'");
                    r3.next();
                    double s = r3.getDouble(request.getParameter(1));
                    int id = r3.getInt("id");

                    s += Double.parseDouble(request.getParameter(0));

                    db.getStatement().execute(STR."UPDATE wallet SET \{request.getParameter(1)} = \{s} WHERE user_id = \{id}");

                } catch (SQLException e) {
                    throw new RuntimeException(e);
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
