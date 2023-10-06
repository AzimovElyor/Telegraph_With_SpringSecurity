package com.example.post.repository;

import com.example.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByOwnerId(UUID ownerId, Pageable pageable);
    Optional<Post> findByLink(String link);
}
