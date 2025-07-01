package com.PetCare.Api;

import com.PetCare.Entity.Appointment;
import com.PetCare.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentApi {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        try {
            appointmentService.bookAppointment(appointment);
            return ResponseEntity.ok("Appointment booked successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Appointment>> getCustomerAppointments(@PathVariable int customerId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForCustomer(customerId);
        return ResponseEntity.ok(appointments);
    }
}
