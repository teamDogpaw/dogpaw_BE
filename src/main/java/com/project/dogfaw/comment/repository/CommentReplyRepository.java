package com.project.dogfaw.comment.repository;

import com.project.dogfaw.comment.model.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {
}
