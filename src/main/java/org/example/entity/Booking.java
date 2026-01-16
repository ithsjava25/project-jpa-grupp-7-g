package org.example.entity;

// Booking - ManyToOne (Car), ManyToMany (Addons), OneToOne (Payment)

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private BookingType type;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.ACTIVE;

    private double totalPrice;

    private String firstName;
    private String lastName;
    private String email;
    private String personalNumber;

    @ManyToOne
    private Car car;

    @ManyToMany
    private List<Addon> addons = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public Booking() {}

    public Booking(LocalDateTime startDate, LocalDateTime endDate, BookingType type, Car car,
                   String firstName, String lastName, String email, String personalNumber) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.car = car;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalNumber = personalNumber;
    }

    public Long getId() { return id; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public BookingType getType() { return type; }
    public BookingStatus getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }
    public Car getCar() { return car; }
    public List<Addon> getAddons() { return addons; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPersonalNumber() { return personalNumber; }

    public void setAddons(List<Addon> addons) { this.addons = addons; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPersonalNumber(String personalNumber) { this.personalNumber = personalNumber; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
}
