package org.example.model;

import jakarta.persistence.*;


import javax.tools.DocumentationTool;
import javax.xml.stream.Location;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id")
    private Location pickupLocation;


    @ManyToOne
    @JoinColumn
    private int days;
    private double price;
    private boolean paid;
    private String status;



}
