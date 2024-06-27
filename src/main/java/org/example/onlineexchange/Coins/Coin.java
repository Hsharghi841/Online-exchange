package org.example.onlineexchange.Coins;

public class Coin {
    protected String name;
    protected double price, maxprice, minprice,percentchenge;

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
        this.price = price;
    }

    public double getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(double maxprice) {
        this.maxprice = maxprice;
    }

    public double getMinprice() {
        return minprice;
    }

    public void setMinprice(double minprice) {
        this.minprice = minprice;
    }

    public double getPercentchenge() {
        return percentchenge;
    }

    public void setPercentchenge(double percentchenge) {
        this.percentchenge = percentchenge;
    }
}
