package com.project.dogfaw.comment.repository;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import jdk.javadoc.internal.doclets.toolkit.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Content, Long> {
    List<Content> findAllByPost(Post postId);

    List<Content> findAllByPostAndUser(Post post, User user);
}
