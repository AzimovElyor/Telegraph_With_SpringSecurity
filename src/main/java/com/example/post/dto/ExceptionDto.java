package com.example.post.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ExceptionDto {
    private String message;
    private Integer statusCode;
}
