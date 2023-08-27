package com.springboot.blog.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//@Controller
//public class ImageController {
//
//    @Value("${image.upload.path}")
//    private String imageUploadPath;
//
//    @GetMapping("/images/{imageName}")
//    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws MalformedURLException {
//        System.out.println("inside getImage method");
//        // Construct the path to the uploaded image
//        Path imagePath = Paths.get(imageUploadPath, imageName);
//        Resource imageResource = new UrlResource(imagePath.toUri());
//        System.out.println("imageResource");
//        if (imageResource.exists()) {
//            return ResponseEntity.ok()
////                    .contentType(MediaType.IMAGE_JPEG) // Set the appropriate content type
//                    .contentType(MediaType.IMAGE_PNG)
//                    .body(imageResource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}


//@Controller
//public class ImageController {
//    @Value("${image.upload.path}")
//    private String imageUploadPath;
//
//
//    @GetMapping("/images/{imageName}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
//        // Load image data from your source (e.g., file system, database)
//        // Here's a simple example using the resource loader
//        Resource imageResource = new ClassPathResource(imageUploadPath + imageName);
//
//        // Read image bytes from InputStream
//        byte[] imageBytes = Files.readAllBytes(imageResource.getFile().toPath());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG); // Set the appropriate content type
//
//        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//    }
//}


@Controller
@RequestMapping("/api/v1")
public class ImageController {

    @Value("${image.user.upload.path}")
    private String imageUploadUserPath; //= "uploaded-images/"; // Update this with your actual path

    @Value("${image.post.upload.path}")
    private String imageUploadPostPath;

    @GetMapping("/images/user/{imageName:.+}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String imageName) throws IOException {
        // Construct the path to the uploaded image
        Path imagePath = Paths.get(imageUploadUserPath, imageName);

        if (Files.exists(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type

            return ResponseEntity.ok().headers(headers).body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/images/post/{imageName:.+}")
    public ResponseEntity<byte[]> getPostImage(@PathVariable String imageName) throws IOException {
        // Construct the path to the uploaded image
        Path imagePath = Paths.get(imageUploadPostPath, imageName);

        if (Files.exists(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type

            return ResponseEntity.ok().headers(headers).body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}