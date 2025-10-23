package com.example.signin_project;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
   @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Company name cannot be blank")
    private String companyName;

    @Email(message = "Please provide a valid email address")
    @NotBlank
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String role;
    private String contactNumber;
    private String warehouseLocation;
}