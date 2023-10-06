package com.example.post.controller;

import com.example.post.dto.JwtResponse;
import com.example.post.dto.UserResponseDto;
import com.example.post.dto.UserUpdateDto;
import com.example.post.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/block-user/{id}")
    public ResponseEntity<String> blockUser(@PathVariable UUID id){
        return new ResponseEntity<>(userService.blockUser(id), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ){
        return ResponseEntity.status(200).body(userService.getAllUsers(page,size));
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/update")
    public ResponseEntity<JwtResponse> updateUser(
            @RequestBody UserUpdateDto userUpdateDto,
            Principal principal
    ){
        return ResponseEntity.ok(userService.updateUser(userUpdateDto,principal));
    }
    @PreAuthorize("hasAuthority('DELETE_USER') and hasRole('USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Principal principal){
        return ResponseEntity.ok(userService.deleteUser(principal));
    }
}
