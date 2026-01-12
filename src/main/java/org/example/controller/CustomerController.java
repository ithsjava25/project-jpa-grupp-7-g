package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.model.Customer;
import org.example.service.CustomerService;

public class CustomerController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;

    private final CustomerService customerService = new CustomerService();

    @FXML
    public void handleAddCustomer() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        // Nu matchar vi konstruktorn Customer(String, String, String)
        Customer customer = new Customer(firstName, lastName, email);
        customerService.saveCustomer(customer);

        System.out.println("Kund sparad: " + firstName + " " + lastName);
    }
}
