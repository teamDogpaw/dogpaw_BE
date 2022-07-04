package com.project.dogfaw.apply.repository;

import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplication,Long> {

    boolean existsByUserAndPost(User user, Post post);


    UserApplication getUserApplicationByUserAndPost(User user, Post post);
}

