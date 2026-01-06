package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id")
    private Location pickupLocation;

    @ManyToOne
    @JoinColumn(name = "dropoff_location_id")
    private Location dropoffLocation;

    @OneToMany
    @JoinColumn(name = "booking_id")
    private List<Extra> extras;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime createdAt;

    // ----------------------------
    // Konstruktörer
    // ----------------------------
    protected Booking() {
        // JPA requires a no-arg constructor
    }

    public Booking(Customer customer, Car car, Location pickupLocation, Location dropoffLocation,
                   LocalDate startDate, LocalDate endDate) {
        this.customer = customer;
        this.car = car;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = BookingStatus.RESERVED;
        this.createdAt = LocalDateTime.now();
    }

    // ----------------------------
    // Getters och setters
    // ----------------------------
    public Long getId() { return id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public Location getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(Location pickupLocation) { this.pickupLocation = pickupLocation; }

    public Location getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(Location dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public List<Extra> getExtras() { return extras; }
    public void setExtras(List<Extra> extras) { this.extras = extras; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ----------------------------
    // Inner class Extra
    // ----------------------------
    @Entity
    public static class Extra {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private double price;

        protected Extra() {}

        public Extra(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}

