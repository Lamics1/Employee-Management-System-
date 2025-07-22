package com.example.employeemanagementsystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Employee {
    private String id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, message = "Name must be more than 2 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Size(min = 5, message = "Email must be more than 4 characters")
    @Email(message = "Email must be a valid format")
    private String email;

    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be 10 digits")
    private String phoneNumber;

    @NotNull(message = "Age cannot be null")
    @Min(value = 26, message = "Age must be more than 25")
    private Integer age;

    @NotNull(message = "Position cannot be null")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "Position must be supervisor or coordinator")
    private String position;

    private boolean onLeave = false;

    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date must be in the past or today")
    private LocalDate hireDate;

    @NotNull(message = "Annual leave cannot be null")
    @Positive(message = "Annual leave must be positive")
    private Integer annualLeave;
}

