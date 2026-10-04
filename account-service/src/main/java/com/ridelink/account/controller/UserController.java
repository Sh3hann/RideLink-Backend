package com.ridelink.account.controller;

import com.ridelink.account.dto.UpdateProfileRequest;
import com.ridelink.account.dto.UpdateStatusRequest;
import com.ridelink.account.model.User;
import com.ridelink.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Endpoints for viewing and updating user profiles and account statuses")
public class UserController {

    private final AccountService accountService;

    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "View user profile", description = "Retrieves the profile of the specified user. Requires authentication.")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal == #id")
    public ResponseEntity<User> getProfile(@PathVariable String id) {
        User user = accountService.getProfile(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user profile", description = "Updates the full name and/or phone number of the specified user. Requires authentication.")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal == #id")
    public ResponseEntity<User> updateProfile(@PathVariable String id, @RequestBody UpdateProfileRequest request) {
        User updatedUser = accountService.updateProfile(id, request.getFullName(), request.getPhoneNumber());
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update account status", description = "Updates the account status of the specified user. Requires ADMIN role.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateStatus(@PathVariable String id, @RequestBody UpdateStatusRequest request) {
        User updatedUser = accountService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(updatedUser);
    }
}
