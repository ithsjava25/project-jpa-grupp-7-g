package org.example.model;

public class Extra {
    private String name;
    private double price;

    // Konstruktor
    public Extra(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getters och setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
