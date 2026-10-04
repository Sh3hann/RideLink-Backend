package com.ridelink.account.service;

import com.ridelink.account.dto.*;
import com.ridelink.account.model.*;
import com.ridelink.account.repository.UserRepository;
import com.ridelink.account.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AccountService accountService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("alice@example.com", "hash", "Alice S.", "+94771234567", Role.PASSENGER, AccountStatus.ACTIVE);
        sampleUser.setId("usr-100");
    }

    @Test
    @DisplayName("Should successfully register a new passenger user")
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("alice@example.com");
        req.setPassword("Password123!");
        req.setFullName("Alice S.");
        req.setPhoneNumber("+94771234567");
        req.setRole(Role.PASSENGER);

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(userRepository.save(any())).thenReturn(sampleUser);
        when(tokenProvider.generateToken(any())).thenReturn("jwt.token.val");

        AuthResponse resp = accountService.register(req);
        assertNotNull(resp);
        assertEquals("jwt.token.val", resp.getToken());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception when registering duplicate email")
    void testRegisterDuplicateEmail() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("alice@example.com");
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> accountService.register(req));
        verify(userRepository, never()).save(any());
    }
}