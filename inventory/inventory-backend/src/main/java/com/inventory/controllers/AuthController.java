package com.inventory.controllers;

import com.inventory.models.User;
import com.inventory.payload.JwtResponse;
import com.inventory.payload.LoginRequest;
import com.inventory.payload.MessageResponse;
import com.inventory.payload.SignUpRequest;
import com.inventory.repositories.UserRepository;
import com.inventory.security.JwtUtil;
import com.inventory.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Custom logic: Check if admin is validated
        if (userDetails.getRole().equals("ROLE_ADMIN") && !userDetails.isValidated()) {
            return ResponseEntity.status(401).body(new MessageResponse("Error: Admin account is pending approval!"));
        }

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getRole(),
                userDetails.isValidated()
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setDept(signUpRequest.getDept());
        user.setPhoneNo(signUpRequest.getPhoneNo());
        user.setWarehouseLocation(signUpRequest.getWarehouseLocation());
        
        String role = signUpRequest.getRole();
        user.setRole(role);

        if (role.equals("ROLE_ADMIN")) {
            user.setValidated(false); // Admin needs IT Manager approval
        } else {
            user.setValidated(true); // User is auto-approved
        }

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}