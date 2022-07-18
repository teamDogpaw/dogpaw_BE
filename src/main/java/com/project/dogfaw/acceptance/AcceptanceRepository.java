package com.project.dogfaw.acceptance;

import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcceptanceRepository extends JpaRepository<Acceptance, Long> {

    List<Acceptance> findAllByUser(User user);
    Boolean existsByUserAndPost(User user, Post post);

}
