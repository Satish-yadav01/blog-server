package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostCreateResponse;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.payload.RegisterResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Value("${image.post.upload.path}")
    private String postImagePath;

    private PostRepository postRepository;

    private final UserRepository userRepository;

    private ModelMapper mapper;

    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ModelMapper mapper,
                           CategoryRepository categoryRepository) {
          this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
          this.categoryRepository = categoryRepository;
    }

    @Override
    public PostCreateResponse createPost(PostDto postDto) throws IOException {
//        System.out.println(postDto.getTitle());
//        System.out.println(postDto.getDescription());
//        System.out.println("loggedInUSer " + postDto.getLoggedInUserId());
//        System.out.println("getCategoryId " + postDto.getCategoryId());

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

        String imageName = System.currentTimeMillis() + "_" + postDto.getCoverUrl().getOriginalFilename();
//        String absoluteImagePath = servletContext.getRealPath(profileImagePath) + imageName;

        Path imagePath = Paths.get(postImagePath + imageName);

        // Log the absolute path
        System.out.println("Absolute Path: " + imagePath.toAbsolutePath());

        Files.copy(postDto.getCoverUrl().getInputStream(), imagePath);

        //find loggedInUser
        System.out.println("loggedInUSer " + postDto.getLoggedInUserId());

        User loggedInUSer = userRepository.findById(postDto.getLoggedInUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", postDto.getLoggedInUserId()));
//        System.out.println(postDto.getLoggedInUserId());
//        User loggedInUSer = user.orElse(new User());

        /* convert DTO to entity */
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setCategory(category);
        post.setCoverUrl(imageName);
        post.setCreatedBy(loggedInUSer);


//        Post post = mapToEntity(postDto);
//        post.setCategory(category);
        Post newPost = postRepository.save(post);

        //RegisterResponse entity
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setName(loggedInUSer.getName());
        registerResponse.setEmail(loggedInUSer.getEmail());
        registerResponse.setUsername(loggedInUSer.getUsername());
        registerResponse.setProfileImagePath(loggedInUSer.getProfileImagePath());
//
        // convert entity to postCreateResponse
        PostCreateResponse postCreateResponse = new PostCreateResponse();
        postCreateResponse.setTitle(newPost.getTitle());
        postCreateResponse.setDescription(newPost.getDescription());
        postCreateResponse.setCategoryId(newPost.getCategory().getId());
        postCreateResponse.setCoverUrl(post.getCoverUrl());
        postCreateResponse.setCreatedBy(registerResponse);

        return postCreateResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content= listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostCreateResponse getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        PostCreateResponse postCreateResponse = new PostCreateResponse();
        postCreateResponse.setTitle(post.getTitle());
        postCreateResponse.setDescription(post.getDescription());
        postCreateResponse.setCategoryId(post.getCategory().getId());
        postCreateResponse.setCoverUrl(post.getCoverUrl());
        return postCreateResponse;
    }

//    @Override
//    public List<PostDto> getPostByCategoryName(String categoryName) {
//        List<Post> byCategory = postRepository.findByCategoryName(categoryName);
//        List<PostDto> collect = byCategory.stream().map(cat -> mapToDTO(cat)).collect(Collectors.toList());
//        return collect;
//    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        Category category = categoryRepository.findById(postDto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        post.setCategory(category);
        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

//    @Override
//    public List<PostDto> getPostsByCategory(Long categoryId) {
//
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
//
//        List<Post> posts = postRepository.findByCategoryId(categoryId);
//
//        return posts.stream().map((post) -> mapToDTO(post))
//                .collect(Collectors.toList());
//    }


    @Override
    public PostResponse getPostsByCategory(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Fetch posts by category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Page<Post> posts = postRepository.findByCategoryId(categoryId, pageable);

        // Get content for page object
        List<PostDto> content = posts.getContent().stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }


    // convert Entity into DTO
    private PostDto mapToDTO(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    // convert DTO to entity
    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
