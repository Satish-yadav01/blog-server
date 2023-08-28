package com.springboot.blog.payload;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostDto {
    private long id;

    // title should not be null  or empty
    // title should have at least 2 characters
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    // post description should be not null or empty
    // post description should have at least 10 characters
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    // post content should not be null or empty
//    @NotEmpty
//    private String content;
    private Set<CommentDto> comments;

    @NotNull
    private Long categoryId;

    private MultipartFile coverUrl;

    @NotNull
    private Long loggedInUserId;
//    @CreationTimestamp
//    private LocalDateTime CreatedAt;
//
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
}
