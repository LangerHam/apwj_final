package com.PetCare.Service;

import com.PetCare.Entity.Appointment;
import com.PetCare.Entity.Customer;
import com.PetCare.Repository.AppointmentRepository;
import com.PetCare.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public int bookAppointment(Appointment appointment) {

        int result = appointmentRepository.save(appointment);

        Customer customer = customerRepository.findById(appointment.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        emailService.sendSimpleMessage(
                customer.getEmail(),
                "Appointment Confirmation",
                "Your appointment for " + appointment.getAppointmentDate() + " has been successfully booked."
        );
        return result;
    }

    public List<Appointment> getAppointmentsForCustomer(int customerId) {
        return appointmentRepository.findAllByCustomerId(customerId);
    }
}
