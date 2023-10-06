package com.example.post.repository;

import com.example.post.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Boolean existsUserByUsername(String username);
    @Modifying
    @Query(nativeQuery = true,value = "update users set blocked = true,updated_date=current_timestamp where id = :id")
     void blockedUser(@Param("id") UUID id);
}
