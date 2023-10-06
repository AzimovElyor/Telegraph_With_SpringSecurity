package com.example.post.dto;

import com.example.post.entity.Permission;
import com.example.post.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterDto {
    private String fullName;
    private String username;
    private String password;
    private Role role;
    private Set<Permission> permissions;
}
