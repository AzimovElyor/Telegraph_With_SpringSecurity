package com.example.post.service;

import com.example.post.dto.JwtResponse;
import com.example.post.dto.UserResponseDto;
import com.example.post.dto.UserUpdateDto;
import com.example.post.entity.User;
import com.example.post.jwt.JwtService;
import com.example.post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
     private final UserRepository userRepository;
     private final ModelMapper modelMapper;
     private final JwtService jwtService;
     private final PasswordEncoder passwordEncoder;

    @Transactional
    public String blockUser(UUID userId){
      userRepository.findById(userId).
          orElseThrow(() -> new RuntimeException("User not found") );
      userRepository.blockedUser(userId);
      return "Successfully blocked";
    }

    public List<UserResponseDto> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> all = userRepository.findAll(pageable);
         return modelMapper.map(all.getContent(),new TypeToken<List<UserResponseDto>>(){}.getType());
    }
    public JwtResponse updateUser(UserUpdateDto updateDto, Principal principal){
         if(updateDto.getPassword() != null){
             updateDto.setPassword(passwordEncoder.encode(updateDto.getPassword()));
         }
        User user = getUserInToken(principal);
        if(updateDto.getUsername() != null && !user.getUsername().equals(updateDto.getUsername())){
            Boolean existUserName = userRepository.existsUserByUsername(updateDto.getUsername());
            if(existUserName) throw new RuntimeException("username already exists");

            modelMapper.map(updateDto,user);
            System.out.println(user);
            userRepository.save(user);
            return JwtResponse.builder().jwtToken(jwtService.generateToken(user)).build();
        }
        modelMapper.map(updateDto,user);
        userRepository.save(user);
        return JwtResponse.builder().jwtToken(jwtService.generateToken(user)).build();
    }
    private User getUserInToken(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String deleteUser(Principal principal) {
        User user = getUserInToken(principal);
        userRepository.delete(user);
        return "Successfully deleted";
    }
}
