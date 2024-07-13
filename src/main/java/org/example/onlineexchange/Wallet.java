package org.example.onlineexchange;

public class Wallet {

    private double usd;
    private double eur;
    private double Toman;
    private double yen;
    private double gbp;

    public double getToman() {
        return Toman;
    }

    public void setToman(double toman) {
        Toman = toman;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getEur() {
        return eur;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }

    public double getYen() {
        return yen;
    }

    public void setYen(double yen) {
        this.yen = yen;
    }

    public double getGbp() {
        return gbp;
    }

    public void setGbp(double gbp) {
        this.gbp = gbp;
    }
}
