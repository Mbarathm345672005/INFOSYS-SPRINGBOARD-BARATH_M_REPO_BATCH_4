package com.example.signin_project;



import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject the encoder

    @Autowired
    private EmailService emailService; // Inject the email service

    @PostMapping("/register") // Changed endpoint name to be more descriptive
    public ResponseEntity<String> handleRegistration(@Valid @RequestBody User user) {
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userRepository.save(user);
        
        emailService.sendSuccessEmail(user.getEmail(), user.getFullName());

        System.out.println("Saved user to database with email: " + user.getEmail());
        
        return ResponseEntity.ok("{\"message\": \"User registered successfully!\"}");
    }
}