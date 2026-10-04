package com.ridelink.account.service;

import com.ridelink.account.dto.*;
import com.ridelink.account.model.*;
import com.ridelink.account.repository.UserRepository;
import com.ridelink.account.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AccountService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use: " + req.getEmail());
        }

        User user = new User(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getFullName(),
                req.getPhoneNumber(),
                req.getRole(),
                AccountStatus.ACTIVE
        );

        User saved = userRepository.save(user);
        String token = tokenProvider.generateToken(saved);
        return new AuthResponse(token, saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole(), saved.getStatus());
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (user.getStatus() == AccountStatus.SUSPENDED) {
            throw new IllegalStateException("Account is suspended. Please contact RideLink support.");
        }
        if (user.getStatus() == AccountStatus.DEACTIVATED) {
            throw new IllegalStateException("Account is deactivated.");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole(), user.getStatus());
    }

    public User getProfile(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    public User updateProfile(String userId, String fullName, String phoneNumber) {
        User user = getProfile(userId);
        if (fullName != null && !fullName.isBlank()) user.setFullName(fullName);
        if (phoneNumber != null && !phoneNumber.isBlank()) user.setPhoneNumber(phoneNumber);
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    public User updateStatus(String userId, AccountStatus status) {
        User user = getProfile(userId);
        user.setStatus(status);
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }
}