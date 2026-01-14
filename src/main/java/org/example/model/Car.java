package org.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private int year;

    @Enumerated(EnumType.STRING)
    private CarType type;
    private double dailyPrice;
    private boolean available = true;
    private boolean damaged = false;

    public Car() {}
    public Car(String brand, String model, int year, CarType type, double dailyPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.type = type;
        this.dailyPrice = dailyPrice;
    }

    // Getters som krävs för TableView (PropertyValueFactory)
    public Long getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public CarType getType() { return type; }
    public double getDailyPrice() { return dailyPrice; }
    public boolean isAvailable() { return available; }
    public boolean isDamaged() { return damaged; }

    // Setters
    public void setAvailable(boolean available) { this.available = available; }
    public void setDamaged(boolean damaged) { this.damaged = damaged; }

    @Override
    public String toString() {
        return brand + " " + model + " (" + year + ") - " + dailyPrice + " kr/dag";
    }
}



