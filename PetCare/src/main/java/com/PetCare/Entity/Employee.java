package com.PetCare.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("employees")
public class Employee {
    @Id
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private EmployeeRole role; // Changed from String to Enum
    private String phone;
    private String email;
    private LocalDate hireDate;
    private String password;
}
