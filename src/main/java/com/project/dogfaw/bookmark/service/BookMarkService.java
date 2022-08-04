package com.project.dogfaw.bookmark.service;

import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
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


    /*북마크저장*/
    @Transactional // Transactinal을 사용함으로써 set 내용이 dirtyCheck를 하고 통과되면 db에 반영이 됨
    public Boolean bookMarkUp(Long postId, User user) {

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new CustomException(ErrorCode.POST_NOT_FOUND)
        );

        //북마크 db에 해당 userId와 PostId 가 존재하지 않으면 db에 저장(북마크 등록) 및 post의 북마크 총 갯수 +1
         if(!bookMarkRepository.existsByUserAndPost(user,post)){
            BookMark bookMark = new BookMark(user, post);
            bookMarkRepository.save(bookMark);
            post.increaseBmCount();
            return true;
            //다시 눌러서 요청할 경우 db에 존재하기 때문에 삭제후 return false(북마크 등록 취소) 및 post의 북마크 총 갯수 -1
        }else{
            BookMark bookMark = bookMarkRepository.getBookMarkByUserAndPost(user,post);
            bookMarkRepository.delete(bookMark);
            post.decreaseBmCount();
            return false;
        }

    }
}
