package com.project.dogfaw.bookmark.repository;

import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.post.Post;
import com.project.dogfaw.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {


    boolean findByMemberAndPost(Member member, Post post);

    BookMark getBookMarkByMemberAndPost(Member member, Post post);


}
