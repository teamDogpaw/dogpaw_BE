package com.project.dogfaw.mypage.service;

import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final PostRepository postRepository;
    private final BookMarkRepository bookMarkRepository;
    private final UserRepository userRepository;

    /*내가 북마크한 글 조회*/
    public ArrayList<PostResponseDto> myBookmark(User user) {

        //유저가 북마크한 것을 리스트로 모두 불러옴
        List<BookMark> userPosts = bookMarkRepository.findAllByUser(user);
        //모든 게시글 내림차순으로
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        //BookMarkStatus를 추가적으로 담아줄 ArrayList 생성
        ArrayList<PostResponseDto> postList = new ArrayList<>();
        //true || false 값을 담아줄 Boolean type의 bookMarkStatus 변수를 하나 생성
        Boolean bookMarkStatus = null ;
        //유저가 북마크한 게시글을 찾아 리스트에 담아주기 위해 ArrayList 생성
        ArrayList<Post> userPostings = new ArrayList<>();


        for (BookMark userPost:userPosts){
            Post userPosting = userPost.getPost();
            userPostings.add(userPosting);
        }

        //일치하면 bookMarkStatus = true 아니면 false를 bookMarkStatus에 담아줌
        for (Post post : posts) {
            Long postId = post.getId();
            User writer = post.getUser();
            for (Post userPost: userPostings ) {
                Long userPostId = userPost.getId();
                //객체를 불러올경우 메모리에 할당되는 주소값으로 불려지기 때문에 비교시 다를 수 밖에 없음
                // 객체 안에있는 특정 데이터 타입으로 비교해줘야 함
                if (postId.equals(userPostId)) {
                    bookMarkStatus = true;
                    break; //true일 경우 탈출
                } else {
                    bookMarkStatus = false;
                }
            }
            //PostResponseDto를 이용해 게시글과, 북마크 상태,writer 는 해당 게시글 유저의 프로필 이미지를 불러오기 위함
            PostResponseDto postDto = new PostResponseDto(post, bookMarkStatus, writer);
            //아까 생성한 ArrayList에 새로운 모양의 값을 담아줌
            postList.add(postDto);
        }
        return postList;
    }

    /*내가 작성한 글 조회*/
    public ArrayList<PostResponseDto> myPost(User user) {

        String nickname = user.getNickname();
        //유저가 북마크한 것을 리스트로 모두 불러옴
        List<BookMark> userPosts = bookMarkRepository.findAllByUser(user);
        //유저가 작성한 모든글 불러옴
        List<Post> posts = postRepository.findByNickname(nickname);
        //BookMarkStatus를 추가적으로 담아줄 ArrayList 생성
        ArrayList<PostResponseDto> postList = new ArrayList<>();
        //true || false 값을 담아줄 Boolean type의 bookMarkStatus 변수를 하나 생성
        Boolean bookMarkStatus = null ;
        //유저가 북마크한 게시글을 찾아 리스트에 담아주기 위해 ArrayList 생성
        ArrayList<Post> userPostings = new ArrayList<>();


        for (BookMark userPost:userPosts){
            Post userPosting = userPost.getPost();
            userPostings.add(userPosting);
        }

        //일치하면 bookMarkStatus = true 아니면 false를 bookMarkStatus에 담아줌
        for (Post post : posts) {
            Long postId = post.getId();
            User writer = post.getUser();
            for (Post userPost: userPostings ) {
                Long userPostId = userPost.getId();
                //객체를 불러올경우 메모리에 할당되는 주소값으로 불려지기 때문에 비교시 다를 수 밖에 없음
                // 객체 안에있는 특정 데이터 타입으로 비교해줘야 함
                if (postId.equals(userPostId)) {
                    bookMarkStatus = true;
                    break; //true일 경우 탈출
                } else {
                    bookMarkStatus = false;
                }
            }
            //PostResponseDto를 이용해 게시글과, 북마크 상태,writer 는 해당 게시글 유저의 프로필 이미지를 불러오기 위함
            PostResponseDto postDto = new PostResponseDto(post, bookMarkStatus, writer);
            //아까 생성한 ArrayList에 새로운 모양의 값을 담아줌
            postList.add(postDto);
        }
        return postList;
    }

    /*내가 참여신청한 프로젝트 조회*/
    public ArrayList<PostResponseDto> myApply(User user) {

        String nickname = user.getNickname();
        //유저가 북마크한 것을 리스트로 모두 불러옴
        List<BookMark> userPosts = bookMarkRepository.findAllByUser(user);
        //유저가 작성한 모든글 불러옴
        List<Post> posts = postRepository.findByNickname(nickname);
        //BookMarkStatus를 추가적으로 담아줄 ArrayList 생성
        ArrayList<PostResponseDto> postList = new ArrayList<>();
        //true || false 값을 담아줄 Boolean type의 bookMarkStatus 변수를 하나 생성
        Boolean bookMarkStatus = null ;
        //유저가 북마크한 게시글을 찾아 리스트에 담아주기 위해 ArrayList 생성
        ArrayList<Post> userPostings = new ArrayList<>();

        //로그인한 유저가 북마크한 게시글을 다 찾아서 ArrayList에 담아줌
        for (BookMark userPost:userPosts){
            Post userPosting = userPost.getPost();
            userPostings.add(userPosting);
        }

        //일치하면 bookMarkStatus = true 아니면 false를 bookMarkStatus에 담아줌
        for (Post post : posts) {
            Long postId = post.getId();
            User writer = post.getUser();
            for (Post userPost: userPostings ) {
                Long userPostId = userPost.getId();
                //객체를 불러올경우 메모리에 할당되는 주소값으로 불려지기 때문에 비교시 다를 수 밖에 없음
                // 객체 안에있는 특정 데이터 타입으로 비교해줘야 함
                if (postId.equals(userPostId)) {
                    bookMarkStatus = true;
                    break; //true일 경우 탈출
                } else {
                    bookMarkStatus = false;
                }
            }
            //PostResponseDto를 이용해 게시글과, 북마크 상태,writer 는 해당 게시글 유저의 프로필 이미지를 불러오기 위함
            PostResponseDto postDto = new PostResponseDto(post, bookMarkStatus, writer);
            //아까 생성한 ArrayList에 새로운 모양의 값을 담아줌
            postList.add(postDto);
        }
        return postList;
    }
}
