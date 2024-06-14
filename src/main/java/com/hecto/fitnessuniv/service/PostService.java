package com.hecto.fitnessuniv.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hecto.fitnessuniv.entity.PostEntity;
import com.hecto.fitnessuniv.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity getPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public PostEntity createPost(PostEntity postEntity) {
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
