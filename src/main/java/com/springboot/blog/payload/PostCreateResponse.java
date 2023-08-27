package com.springboot.blog.payload;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateResponse {
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
//    private Set<CommentDto> comments;

    @NotNull
    private Long categoryId;

    private String coverUrl;
}
