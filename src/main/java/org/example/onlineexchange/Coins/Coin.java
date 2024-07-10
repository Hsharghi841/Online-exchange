package org.example.onlineexchange.Coins;

import java.util.ArrayList;

public class Coin {
    protected String name;
    protected double price, maxprice, minprice=100000000, percentchenge=1, pastprice = 0;
    protected ArrayList<Double> prices = new ArrayList<>();
    protected ArrayList<Orders> orders = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        setPastprice(this.price);
        this.price = price;
    }

    public double getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(double maxprice) {
        if (maxprice > this.maxprice)
            this.maxprice = maxprice;
    }

    public double getMinprice() {
        return minprice;
    }

    public void setMinprice(double minprice) {
        if (minprice < this.minprice) {
            this.minprice = minprice;
        }
    }

    public double getPercentchenge() {
        return percentchenge;
    }

    public void setPercentchenge(double percentchenge) {
        this.percentchenge = percentchenge;
    }

    public double getPastprice() {
        return pastprice;
    }

    public void setPastprice(double pastprice) {
        this.pastprice = pastprice;
    }

    public void setPrice1(double price) {
        pastprice = price;
        this.price = price;
    }
}
