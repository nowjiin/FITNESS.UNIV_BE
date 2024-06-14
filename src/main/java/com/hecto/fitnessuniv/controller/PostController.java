package com.hecto.fitnessuniv.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hecto.fitnessuniv.entity.PostEntity;
import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;
import com.hecto.fitnessuniv.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<PostEntity>> getAllPosts(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                return ResponseEntity.ok(postService.getAllPosts());
            }
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostEntity> getPostById(
            @PathVariable Long id, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                return ResponseEntity.ok(postService.getPostById(id));
            }
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping
    public ResponseEntity<PostEntity> createPost(
            @RequestBody PostEntity postEntity, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<UserEntity> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    postEntity.setAuthor(userOptional.get().getUserName());
                    return ResponseEntity.ok(postService.createPost(postEntity));
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            }
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostEntity> updatePost(
            @PathVariable Long id, @RequestBody PostEntity postEntity, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<UserEntity> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    return ResponseEntity.ok(postService.updatePost(id, postEntity));
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            }
        }
        return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validate(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                Optional<UserEntity> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    postService.deletePost(id);
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.status(404).build();
                }
            }
        }
        return ResponseEntity.status(401).build();
    }
}
