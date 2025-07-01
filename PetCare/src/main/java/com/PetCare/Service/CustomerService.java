package com.PetCare.Service;

import com.PetCare.Entity.Customer;
import com.PetCare.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer getCustomerProfile(int customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
    }

    public int updateCustomerProfile(Customer customer) {
        customerRepository.findById(customer.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customer.getCustomerId()));
        return customerRepository.update(customer);
    }

    public void changeCustomerPassword(String email, String newPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));

        String hashedPassword = passwordEncoder.encode(newPassword);
        customerRepository.updatePassword(email, hashedPassword);
    }
}
