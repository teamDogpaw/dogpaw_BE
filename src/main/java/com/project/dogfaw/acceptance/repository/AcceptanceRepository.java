package com.project.dogfaw.acceptance.repository;

import com.project.dogfaw.acceptance.model.Acceptance;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcceptanceRepository extends JpaRepository<Acceptance, Long> {

    List<Acceptance> findAllByUser(User user);
    List<Acceptance> findAllByUserOrderByIdDesc(User user);
    List<Acceptance> findAllByPost(Post post);
    Boolean existsByUserAndPost(User user, Post post);

    Optional<Acceptance>deleteByUserAndPost(User user, Post post);
    Optional<Acceptance>findByUserAndPost(User user, Post post);

    Optional<Acceptance> deleteAllByPost(Post post);


    void deleteByUserId(Long userId);
}
