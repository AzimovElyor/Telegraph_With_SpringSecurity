package com.example.post.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum Permission {
    DELETE_USER(Set.of(Role.USER)),
    READ_USERS(Set.of(Role.ADMIN)),
    UPDATE_USER(Set.of(Role.USER,Role.ADMIN)),
    BLOCK_USER(Set.of(Role.ADMIN)),

    CREATE_POST(Set.of(Role.USER)),
    DELETE_POST(Set.of(Role.USER,Role.ADMIN)),
    UPDATE_POST(Set.of(Role.USER)),
    READ_USER_POSTS(Set.of(Role.USER,Role.ADMIN)),
    READ_ALL_POSTS(Collections.emptySet());
    private final Set<Role> roles;

}
