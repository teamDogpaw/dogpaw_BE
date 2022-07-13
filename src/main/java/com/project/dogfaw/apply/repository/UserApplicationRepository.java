package com.project.dogfaw.apply.repository;

import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplication,Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<UserApplication> deleteAllByPost(Post post);

    UserApplication getUserApplicationByUserAndPost(User user, Post post);

    List<UserApplication> findAllByUser(User user);
    List<UserApplication> findAllByPost(Post psot);
}

