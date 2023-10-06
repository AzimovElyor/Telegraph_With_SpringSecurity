package com.example.post.service;

import com.example.post.dto.JwtResponse;
import com.example.post.dto.LoginDto;
import com.example.post.dto.RegisterDto;
import com.example.post.entity.Permission;
import com.example.post.entity.Role;
import com.example.post.entity.User;
import com.example.post.jwt.JwtService;
import com.example.post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

  public JwtResponse register(RegisterDto registerDto){
      if (userRepository.existsUserByUsername(registerDto.getUsername())) {
          throw new RuntimeException("Username already exists");
      }
      User user = modelMapper.map(registerDto, User.class);
      System.out.println(user);
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      if(!isPermissionsValid(user.getPermissions(),user.getRole())){
         throw new RuntimeException("Invalid role permissions");
      }
      userRepository.save(user);
      String token = jwtService.generateToken(user);
      return new JwtResponse(token);
  }
  public JwtResponse login(LoginDto loginDto){
      System.out.println(":::::::::::Login ga keldi");
   User user = userRepository.findByUsername(loginDto.getUsername())
           .orElseThrow(() -> new UsernameNotFoundException("User not found"));
   if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
       throw new UsernameNotFoundException("User not found");
   }
   return new JwtResponse(jwtService.generateToken(user));
  }
  private boolean isPermissionsValid(Set<Permission> permissionSet, Role role){
      for (Permission permission : permissionSet) {
          if(!permission.getRoles().contains(role))return false;
      }
      return true;
  }
}
