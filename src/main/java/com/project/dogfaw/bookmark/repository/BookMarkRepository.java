package com.project.dogfaw.bookmark.repository;

import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {


    boolean existsByUserAndPost(User user, Post post);

    boolean existsByUser(User user);

//    Optional<BookMark> findAllByUser(User user);

    List<BookMark> findAllByUserOrderByIdDesc(User user);
    List<BookMark> findAllByUser(User user);

    Optional<BookMark> deleteAllByPost(Post post);

    BookMark getBookMarkByUserAndPost(User user, Post post);


    boolean findByUserAndPostId(User user, Long postId);
}
