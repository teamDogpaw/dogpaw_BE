package com.project.dogfaw.post.service;


import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.bookmark.repository.BookMarkRepository;

import com.project.dogfaw.post.dto.PostDetailResponseDto;

import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.model.PostStack;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.dto.StackDto;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BookMarkRepository bookMarkRepository;
    private final UserRepository userRepository;

    //전체조회
    public ArrayList<PostResponseDto> allPost(User user) {


        //유저가 북마크한 것을 리스트로 모두 불러옴
        List<BookMark> userPosts = bookMarkRepository.findAllByUser(user);
        //유저가 북마크한 게시글을 찾아 리스트에 담아주기 위해 ArrayList 생성
        ArrayList<Post> userPostings = new ArrayList<>();
        for (BookMark userPost:userPosts){
            Post userPosting = userPost.getPost();
            userPostings.add(userPosting);
        }

        //BookmarkStatus를 true || false를 담아주기 위해 for문을 두번 돌리기 때문에
        //모든 게시글을 다 찾아와서 for문을 돌렸을 때 서버 부하가 어느정도 올지 모르겠음(만약 게시글이 많을 경우)
        //게시글이 많을 경우 이중 for문때문에 부하가 상당할 것으로 예상이 되므로 메인페이지에서 최신 게시글 Top20정도만
        //보여주는 것도 하나의 방법이 될 것 같음.

        //내림차순으로 Top 20만 내림차순으로
        //List<Post> posts = postRepository.findTop20ByOrderByModifiedAtDesc();

        //모든 게시글 내림차순으로
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();

        //BookMarkStatus를 추가적으로 담아줄 ArrayList 생성
        ArrayList<PostResponseDto> postList = new ArrayList<>();

        //true || false 값을 담아줄 Boolean type의 bookMarkStatus 변수를 하나 생성
        Boolean bookMarkStatus = null ;

        //일치하면 bookMarkStatus = true 아니면 false를 bookMarkStatus에 담아줌
        for (Post post : posts) {
            Long postId = post.getId();
            User writer = post.getUser();
            for (Post userPost : userPostings) {
                Long userPostId = userPost.getId();
                //객체대 객체일경우 if문으로 동일 비교 불가?
                //객체를 불러올경우 메모리에 할당되는 주소값으로 불려지기 때문에 비교시 다를 수 밖에 없음
                //객체 안의 특정 타입 id, nickname 등으로 비교하여 문제 해결
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




//     post 등록
//    @Transactional
//    public PostResponseDto postPost(PostRequestDto postRequestDto, String username) {
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalArgumentException("해당 Id의 회원이 존재하지 않습니다.")
//        );
//        // 게시글 작성자 저장 (편의 메서드 -> user에도 post에 해당 post add)
//        Post post = new Post(postRequestDto, user, bookMarkRepository);
//        PostResponseDto postResponseDto = new PostResponseDto(postRepository.save(post));
//        // 저장된 Post -> PostResponseDto에 담아 리턴
//        return postResponseDto;
//
//    }

    //post 상세조회
    public PostDetailResponseDto getPostDetail(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        //int bookMarkCnt = bookMarkRepository.findAllByPost(post).size();

        return new PostDetailResponseDto(post);
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto, String username) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 Id의 회원이 존재하지 않습니다.")
        );
        if (!Objects.equals(username, post.getUser().getUsername())){
            throw new IllegalArgumentException("본인의 게시글만 수정할 수 있습니다.");
        }
        post.update(postRequestDto, user.getId());
    }

    //게시글 삭제
    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        if (!Objects.equals(username, post.getUser().getUsername())){
            throw new IllegalArgumentException("본인의 게시글만 삭제할 수 있습니다.");
        }
        postRepository.deleteById(postId);
    }

    private List<PostStack> tostackByPostId(List<StackDto> requestDto, Post post) {
        List<PostStack> stackList = new ArrayList<>();
        for(StackDto stackdto : requestDto){
            stackList.add(new PostStack(stackdto, post ));
        }
        return stackList;
    }
}

    //북마크

    //댓글
}

