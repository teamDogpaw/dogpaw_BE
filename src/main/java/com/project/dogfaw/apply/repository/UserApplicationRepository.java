package com.project.dogfaw.apply.repository;

import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplication,Long> {

    static UserApplication findByMemberAndPost(Member member, Post post);


    UserApplication getUserApplicationByMemberAndPost(Member member, Post post);
}

