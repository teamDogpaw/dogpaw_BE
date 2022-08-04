package com.project.dogfaw.post.repository;

import com.project.dogfaw.post.dto.MyApplyingResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
//    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findAllByOrderByCreatedAtDesc();

    Slice<Post> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);

    Slice<MyApplyingResponseDto> findByOrderByCreatedAtDesc(PageRequest pageRequest);

    List<Post> findTop20ByOrderByModifiedAtDesc();

    List<Post> findAllByUserOrderByIdDesc(User user);

//    List<BookmarkRankResponseDto> findByOrderByBookmarkCntDesc(PageRequest pageRequest);

    List<Post> findByOrderByBookmarkCntDesc(PageRequest pageRequest);



//    Optional<Post> findByUsername(String username );
}