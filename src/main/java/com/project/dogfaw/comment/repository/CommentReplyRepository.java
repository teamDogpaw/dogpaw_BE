package com.project.dogfaw.comment.repository;

import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.model.CommentReply;
import com.project.dogfaw.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {
    List<CommentReply> findByCommentId(Long commentId);
}
