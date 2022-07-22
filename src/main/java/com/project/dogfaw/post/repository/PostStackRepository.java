package com.project.dogfaw.post.repository;

import com.project.dogfaw.post.model.PostStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostStackRepository extends JpaRepository<PostStack, Long> {

    List<PostStack> findByPostId(Long postId);
    List<PostStack> deleteByPostId(Long postId);

}
