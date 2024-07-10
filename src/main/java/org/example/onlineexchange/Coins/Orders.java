package org.example.onlineexchange.Coins;


import org.example.onlineexchange.ClientApp.User;

public class Orders {
    double Value,Price;
    User seller,buyer;
    boolean sell,finish;
    Coin coin;

    public Orders(double value, double price, boolean sell, String coin) {
        Value = value;
        Price = price;
        this.sell = sell;
        if(coin=="USD")
            this.coin = new USD();
        if(coin=="EUR")
            this.coin = new EUR();
        if(coin=="YEN")
            this.coin = new YEN();
        if(coin=="GBP")
            this.coin = new GBP();
        finish=false;
    }
}
