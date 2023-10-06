package com.example.post.controller;

import com.example.post.dto.PostRequestDto;
import com.example.post.dto.PostResponseDto;
import com.example.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequestMapping("/post")
@RestController
public class PostController {
    @Autowired
    private PostService postService;
   @PreAuthorize("hasAuthority('CREATE_POST') and hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody PostRequestDto postRequestDto,
            Principal principal
    ){
        return ResponseEntity.status(201).body(postService.create(postRequestDto,principal));
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<PostResponseDto>> getAllPosts(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ){
       return ResponseEntity.ok(postService.getAllPosts(page,size));
    }
    @GetMapping("/link/{link}")
    public ResponseEntity<PostResponseDto> getPostByLink(@PathVariable String link){
       return new ResponseEntity<>(postService.getPostByLink(link), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('READ_USER_POSTS') or hasRole('ADMIN')")
    @GetMapping("/user-post")
    public ResponseEntity<List<PostResponseDto>> getUserPosts(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            Principal principal
    ){
       return new ResponseEntity<>(postService.getUserPosts(page, size, principal),HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('DELETE_POST') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable UUID id){
      return ResponseEntity.ok(postService.deletePost(id));
    }
}
