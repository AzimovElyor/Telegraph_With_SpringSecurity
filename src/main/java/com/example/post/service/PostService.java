package com.example.post.service;

import com.example.post.dto.PostRequestDto;
import com.example.post.dto.PostResponseDto;
import com.example.post.entity.Post;
import com.example.post.entity.User;
import com.example.post.repository.PostRepository;
import com.example.post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Value("${post.link}")
    String link;
    public PostResponseDto create(PostRequestDto postRequestDto, Principal principal){
        User user = getUserInToken(principal);
        if(user.isBlocked()) throw new RuntimeException("User is blocked");
        Post post = modelMapper.map(postRequestDto, Post.class);
        post.setOwner(user);
        LocalDateTime now = LocalDateTime.now();
        post.setLink(postRequestDto.getTitle().replace(" ","_") + "-" + now.getMonth() + "-" + now.getDayOfMonth() + "-" + now.getHour() + "-" + now.getNano());
        postRepository.save(post);

        return mapPostToPostResponse(post);
    }

    private User getUserInToken(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public PostResponseDto getPostByLink(String link) {
        Post post = postRepository.findByLink(link).
                orElseThrow(() -> new RuntimeException("Post not found this link '%s".formatted(link)));
        return mapPostToPostResponse(post);
    }

    private PostResponseDto mapPostToPostResponse(Post post) {
        PostResponseDto response = modelMapper.map(post, PostResponseDto.class);
        response.setOwnerName(post.getOwner().getFullName());
        response.setLink(link + post.getLink());
        return response;
    }

    public List<PostResponseDto> getAllPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Post> all = postRepository.findAll(pageable);

       return all.get()
                .map(this::mapPostToPostResponse)
                .toList();

    }

    public List<PostResponseDto> getUserPosts(Integer page, Integer size, Principal principal) {
        Pageable pageable = PageRequest.of(page,size);
        User user = getUserInToken(principal);
        System.out.println("user = postlarifdagi user " + user);
        List<Post> byOwnerId = postRepository.findByOwnerId(user.getId(), pageable);
        return byOwnerId.stream()
                .map(this::mapPostToPostResponse)
                .toList();
    }

    public String deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found this '%s' id".formatted(id)));
        postRepository.delete(post);
        return "Successfully deleted";
    }
}
