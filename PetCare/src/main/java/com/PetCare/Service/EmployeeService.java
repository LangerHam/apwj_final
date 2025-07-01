package com.PetCare.Service;

import com.PetCare.Entity.Employee;
import com.PetCare.Entity.EmployeeRole;
import com.PetCare.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee getEmployeeProfile(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
    }

    public void changeEmployeePassword(String email, String oldPassword, String newPassword) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        if (!passwordEncoder.matches(oldPassword, employee.getPassword())) {
            throw new RuntimeException("Invalid old password.");
        }
        String hashedPassword = passwordEncoder.encode(newPassword);
        employeeRepository.updatePassword(email, hashedPassword);
    }

    public List<Employee> getEmployeesByRole(EmployeeRole role) {
        return employeeRepository.findByRole(role);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
