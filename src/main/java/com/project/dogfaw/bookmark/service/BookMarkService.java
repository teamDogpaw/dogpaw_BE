package com.project.dogfaw.bookmark.service;

import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.Member;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;



    @Transactional // Transactinal을 사용함으로써 set 내용이 dirtyCheck를 하고 통과되면 db에 반영이 됨
    public Boolean bookMarkUp(Long postId, Long userId) {
        //user와 post Id 찾아옴
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NullPointerException("해당 ID가 존재하지 않음")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new NullPointerException("해당 게시물이 존재하지 않음")
        );

        //북마크 db에 해당 userId와 PostId 가 존재하지 않으면 db에 저장(북마크 등록)
         if(!bookMarkRepository.existsByUserAndPost(user,post)){
            BookMark bookMark = new BookMark(user, post);
            bookMarkRepository.save(bookMark);
            return true;
            //다시 눌러서 요청할 경우 db에 존재하기 때문에 삭제후 return false(북마크 등록 취소)
        }else{
            BookMark bookMark = bookMarkRepository.getBookMarkByUserAndPost(user,post);
            bookMarkRepository.delete(bookMark);
            return false;
        }
    }
}
