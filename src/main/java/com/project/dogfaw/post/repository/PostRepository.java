package com.project.dogfaw.post.repository;

import com.project.dogfaw.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByModifiedAtDesc();


//    Optional<Post> findByUsername(String username );
}