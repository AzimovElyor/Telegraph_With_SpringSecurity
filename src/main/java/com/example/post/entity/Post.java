package com.example.post.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post extends BaseEntity{
    private String title;
    private String author;
    private String content;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private User owner;
    private String link;

}
