package com.inventory.repositories;

import com.inventory.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    
    // For the IT Manager to find pending admins
    List<User> findByRoleAndValidated(String role, boolean validated);
}