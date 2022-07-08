package com.project.dogfaw.comment.repository;

import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);

    // Comment findByPostId(Long postId);
}
