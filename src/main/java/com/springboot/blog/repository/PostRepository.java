package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

//    List<Post> findByCategoryId(Long categoryId);
    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);
//    List<Post> findByCategoryName(String category);
    List<Post> findByCategory(String category);

}
