package com.hecto.fitnessuniv.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hecto.fitnessuniv.entity.PostEntity;
import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.repository.PostRepository;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity getPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public PostEntity createPost(PostEntity postEntity, String userId) {
        UserEntity user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        postEntity.setUserId(user);
        postEntity.setAuthor(user.getUserName());
        return postRepository.save(postEntity);
    }

    @Transactional
    public PostEntity updatePost(Long id, PostEntity postEntity) {
        PostEntity existingPost = getPostById(id);
        existingPost.setTitle(postEntity.getTitle());
        existingPost.setContent(postEntity.getContent());
        existingPost.setAuthor(postEntity.getAuthor());
        return postRepository.save(existingPost);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
