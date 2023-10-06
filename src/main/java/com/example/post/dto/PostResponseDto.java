package com.example.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostResponseDto {
    private UUID id;
    private String ownerName;
    private String title;
    private String author;
    private String content;
    private String link;
}
