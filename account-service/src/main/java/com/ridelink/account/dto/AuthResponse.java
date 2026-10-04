package com.ridelink.account.dto;
import com.ridelink.account.model.Role;
import com.ridelink.account.model.AccountStatus;
public class AuthResponse {
    private String token; private String id; private String email; private String fullName; private Role role; private AccountStatus status;
    public AuthResponse(String token, String id, String email, String fullName, Role role, AccountStatus status) {
        this.token = token; this.id = id; this.email = email; this.fullName = fullName; this.role = role; this.status = status;
    }
    public String getToken() { return token; } public void setToken(String token) { this.token = token; }
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; } public void setFullName(String fullName) { this.fullName = fullName; }
    public Role getRole() { return role; } public void setRole(Role role) { this.role = role; }
    public AccountStatus getStatus() { return status; } public void setStatus(AccountStatus status) { this.status = status; }
}
