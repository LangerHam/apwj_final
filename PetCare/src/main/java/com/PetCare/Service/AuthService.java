package com.PetCare.Service;

import com.PetCare.DTO.AuthRequest;
import com.PetCare.DTO.AuthResponse;
import com.PetCare.Entity.EmployeeRole;
import com.PetCare.Repository.EmployeeRepository;
import com.PetCare.Entity.Employee;
import com.PetCare.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServices userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    public Employee registerEmployee(Employee employee) throws Exception {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new Exception("Email address is already in use.");
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        if (employee.getRole() == null) {
            employee.setRole(EmployeeRole.REGULAR_EMPLOYEE);
        }
        employeeRepository.save(employee);
        return employee;
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(jwt);
    }
}
