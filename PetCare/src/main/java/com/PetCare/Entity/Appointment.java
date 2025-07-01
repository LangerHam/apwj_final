package com.PetCare.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("appointments")
public class Appointment {
    @Id
    private Integer appointmentId;
    private Integer customerId;
    private Integer petId;
    private Integer employeeId;
    private Integer serviceId;
    private LocalDateTime appointmentDate;
    private String status;
    private String notes;
}
