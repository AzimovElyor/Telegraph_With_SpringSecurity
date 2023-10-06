package com.example.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestDto {
    @NotBlank(message = "title bosh yoki null bolmasligi kerak")
    private String title;
    @NotBlank(message = "author bosh yoki null bolmasligi kerak")
    private String author;
    @NotBlank(message = "content bosh yoki null bolmasligi kerak")
    private String content;

}
