package com.PetCare.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private org.springframework.mail.javamail.JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
        message.setFrom("niloygomes088@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendAppointmentReminder(String to, String petName, String appointmentDate) {
        String subject = "Appointment Reminder for " + petName;
        String text = "Hello,\n\nThis is a reminder for your upcoming appointment for " + petName + " on " + appointmentDate + ".\n\nWe look forward to seeing you!\n\nThe Pet Care Team";
        sendSimpleMessage(to, subject, text);
    }
}
