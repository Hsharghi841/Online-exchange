package org.example.onlineexchange.Server;

import org.example.onlineexchange.Coins.*;
import org.example.onlineexchange.User;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Objects;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    Socket socket;

    Scanner scanner;
    Formatter formatter;
    private Coin coins[] = new Coin[5], temp;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
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
        String command = scanner.nextLine();
        while (!Objects.equals(command, "[q]")) {
            User user = null;
            String[] values = command.split(",");
            boolean repeat = false;
            boolean login = false;
            if (Objects.equals(values[0], "[SIGN IN]")) {
                System.out.println("1");
                for (int i = 0; i < User.users.size(); i++) {

                    if (values[1].equals(User.users.get(i).getUsername()))
                        repeat = true;
                }
                if (!repeat) {
                    user = new User(values[1], values[2], values[3], values[4]);
                    formatter.format("[successful]\n");
                    User.users.add(user);
                } else {
                    formatter.format("[unsuccessful]\n");
                }
            }
            if (Objects.equals(values[0], "[DELETE]")) {
                boolean delete=false;
                for (int i = 0; i < User.users.size(); i++) {
                    if (values[1].equals(User.users.get(i).getUsername())) {
                        if (values[2].equals(User.users.get(i).getPassword())) {
                            User.users.remove(i);
                            delete = true;
                            formatter.format("[successful]\n");
                            break;
                        }
                        break;
                    }
                }
                if (!delete)
                    formatter.format("[unsuccessful]\n");
            }
            if (Objects.equals(values[0], "[LOGIN]")) {
                for (int i = 0; i < User.users.size(); i++) {
                    if (values[1].equals(User.users.get(i).getUsername())) {
                        if (values[2].equals(User.users.get(i).getPassword())) {
                            user = User.users.get(i);
                            login = true;
                            formatter.format("[successful]\n");
                            break;
                        }
                        break;
                    }
                }
                if (!login)
                    formatter.format("[unsuccessful]\n");
            }
            if (Objects.equals(values[0], "[SEND]")) {
                String[] values2 = values[1].split("_");
                User other = null;
                boolean moafagh = false;
                for (int i = 0; i < User.users.size(); i++) {
                    if (values2[1].equals(User.users.get(i).getUsername())) {
                        other = User.users.get(i);
                        other.massages.add(values2[2] + values2[3]);
                        formatter.format("[successful]\n");
                        moafagh = true;
                        break;
                    }
                }
                if (!moafagh)
                    formatter.format("[unsuccessful]\n");
            }
            if (Objects.equals(values[0], "[GET]")) {
                String massage = "";
                massage += "[successful]";
                for (int i = 0; i < user.massages.size(); i++){
                    massage += "," + user.massages.get(i);
                }
                formatter.format(massage + "\n");
            }
            if (Objects.equals(values[0], "[ECHO]")){
                formatter.format(values[1] + "\n");
            }
            command = scanner.nextLine();
        }


    }
}

