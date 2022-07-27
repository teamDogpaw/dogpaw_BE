package com.project.dogfaw.comment.repository;

import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);

    Optional<Comment> deleteAllByPost(Post post);


    // Comment findByPostId(Long postId);
}
