package com.onemoreuse.service;

import com.onemoreuse.dto.AuthRequest;
import com.onemoreuse.dto.AuthResponse;
import com.onemoreuse.dto.RegisterRequest;
import com.onemoreuse.entity.User;
import com.onemoreuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service managing user registration, authentication, and profiles.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     * Validates that the email is not already taken, hashes the password using BCrypt,
     * and persists the user to MySQL.
     */
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail().trim().toLowerCase())) {
            return AuthResponse.failure("An account with this email address already exists.");
        }

        String hashedPassword = passwordEncoder.encode(req.getPassword());
        User user = new User(
                req.getName().trim(),
                req.getEmail().trim().toLowerCase(),
                hashedPassword,
                req.getLocation() != null && !req.getLocation().isBlank() ? req.getLocation().trim() : "Pune"
        );

        User saved = userRepository.save(user);

        // Simple token format for college demonstration (user ID + email base)
        String demoToken = "omu_token_" + saved.getId() + "_" + System.currentTimeMillis();

        return AuthResponse.success(saved.getId(), saved.getName(), saved.getEmail(), saved.getLocation(), demoToken);
    }

    /**
     * Authenticates an existing user by email and password.
     */
    public AuthResponse login(AuthRequest req) {
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail().trim().toLowerCase());

        if (userOpt.isEmpty()) {
            return AuthResponse.failure("Invalid email or password.");
        }

        User user = userOpt.get();

        // Verify password against BCrypt hash
        boolean passwordMatches = passwordEncoder.matches(req.getPassword(), user.getPasswordHash());

        // Self-healing: if database was initialized from SQL script with the seed placeholder hash,
        // accept "password123" and automatically update the database with a fresh BCrypt hash.
        if (!passwordMatches && user.getPasswordHash() != null && user.getPasswordHash().contains("wT8hA8oN")
                && "password123".equals(req.getPassword())) {
            user.setPasswordHash(passwordEncoder.encode("password123"));
            userRepository.save(user);
            passwordMatches = true;
        }

        if (!passwordMatches) {
            return AuthResponse.failure("Invalid email or password.");
        }

        String demoToken = "omu_token_" + user.getId() + "_" + System.currentTimeMillis();
        return AuthResponse.success(user.getId(), user.getName(), user.getEmail(), user.getLocation(), demoToken);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
