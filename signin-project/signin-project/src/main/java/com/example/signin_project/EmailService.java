// in src/main/java/com/example/signinproject/EmailService.java
package com.example.signin_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // <-- IMPORT THIS
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Inject the username and password from application.properties
    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    public void sendSuccessEmail(String toEmail, String name) {
        
        // --- THIS IS THE DEBUGGING CODE ---
        System.out.println("=========================================");
        System.out.println("Attempting to send email with credentials:");
        System.out.println("Username: [" + mailUsername + "]");
        System.out.println("Password: [" + mailPassword + "]");
        System.out.println("Password Length: " + mailPassword.length());
        System.out.println("=========================================");
        // --- END OF DEBUGGING CODE ---

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Successful!");
        message.setText("Hello " + name + ",\n\nWelcome! Your registration was successful. Thank you for joining us.");
        
        mailSender.send(message);
    }
}