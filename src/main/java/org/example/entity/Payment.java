package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String personalNumber;

    @OneToOne
    private Booking booking;

    public Payment() {}

    public Payment(String firstName, String lastName, String email, String personalNumber, Booking booking) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalNumber = personalNumber;
        this.booking = booking;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPersonalNumber() { return personalNumber; }
    public Booking getBooking() { return booking; }
}
