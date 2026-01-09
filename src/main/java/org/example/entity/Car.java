package org.example.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;

    @Column(unique = true)
    private String registrationNumber;

    private double pricePerHour;
    private double pricePerDay;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public Car() {}

    public Car(String brand, String model, String registrationNumber,
               double pricePerHour, double pricePerDay) {
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
    }

    public Long getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getRegistrationNumber() { return registrationNumber; }
    public double getPricePerHour() { return pricePerHour; }
    public double getHourlyPrice() { return pricePerHour; }
    public double getPricePerDay() { return pricePerDay; }
    public double getDailyPrice() { return pricePerDay; }

    @Override
    public String toString() {
        return brand + " " + model + " (" + registrationNumber + ")";
    }
}
