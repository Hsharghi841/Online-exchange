package org.example.onlineexchange.Coins;


import org.example.onlineexchange.ClientApp.User;

public class Orders {
    int Value,Price;
    User seller,buyer;
    boolean sell,finish;
    Coin coin;

    public Orders(int value, int price, boolean sell, Coin coin) {
        Value = value;
        Price = price;
        this.sell = sell;
        this.coin = coin;
        finish=false;
    }
}
