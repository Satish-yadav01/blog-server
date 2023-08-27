package com.springboot.blog.service;

import com.springboot.blog.payload.PostCreateResponse;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostCreateResponse createPost(PostDto postDto) throws IOException;

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostCreateResponse getPostById(long id);

//    List<PostDto> getPostByCategoryName(String categoryName);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);

//    List<PostDto> getPostsByCategory(Long categoryId);
    PostResponse getPostsByCategory(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
}
