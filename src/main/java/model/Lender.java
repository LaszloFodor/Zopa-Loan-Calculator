package model;

public class Lender {

    private String name;

    private double rate;

    private int amount;

    public Lender(String name, double rate, int amount) {
        this.name = name;
        this.rate = rate;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    public int getAmount() {
        return amount;
    }

}
