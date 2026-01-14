package org.example.service;

import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;

public class CustomerService {

    private final CustomerRepository customerRepository = new CustomerRepository();

    public void saveCustomer(Customer customer) {
        // Validering: kolla om e-post redan finns
        Optional<Customer> existing = customerRepository.findByEmail(customer.getEmail());
        if (existing.isPresent()) {
            System.out.println("Kunden finns redan i systemet.");
            return;
        }
        customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
