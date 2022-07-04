package com.project.dogfaw.bookmark.service;

import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;



    @Transactional // Transactinal을 사용함으로써 set 내용이 dirtyCheck를 하고 통과되면 db에 반영이 됨
    public Boolean bookMarkUp(Long postId, Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(
                ()-> new NullPointerException("해당 ID가 존재하지 않음")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new NullPointerException("해당 게시물이 존재하지 않음")
        );
        //북마크 db에 저장이 되어있지 않다면 저장을 시키고 Post테이블의 currentMember +1 시킴
        //Post 테이블의 currentMember +1은 작성자가 참가신청을 걸러서 받을 경우 수락 시 +1로 수정 필요함!
         if(bookMarkRepository.findByMemberAndPost(member,post))==null){
            BookMark bookMark = new BookMark(member, post);
            bookMarkRepository.save(bookMark);
            bookMark.increaseCnt();  //post 테이블 완성되면 post 테이블에 생성자 추가//
            return true;
        }else{ //북마크 db에 저장이 되어있지 않다면 저장을 시키고 Post테이블의 currentMember -1 시킴
            BookMark bookMark = bookMarkRepository.getBookMarkByMemberAndPost(member,post);
            bookMarkRepository.delete(bookMark);
            bookMark.decreaseCnt(); //post 테이블 완성되면 post 테이블에 생성자 추가
            return false;
        }
    }
}
