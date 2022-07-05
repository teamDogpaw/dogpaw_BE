package com.project.dogfaw.post.service;


import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BookMarkRepository bookMarkRepository;
    private final UserRepository userRepository;

    //전체조회
    public ArrayList<PostResponseDto> allPost(User user) {

        //굳이 UserRepository를 두 번 돌릴 필요가 없을 것 같음
//        Long userId = userRepository.findByUsername(username).get().getId();

//        User user = userRepository.findById(userId).orElseThrow(
//                () -> new NullPointerException("해당 ID가 존재하지 않음")
//        );

        //북마크는 해당 유저라는 객체와 해당 게시글이라는 객체가 함께 저장되기에
        //해당 유저의 객체로 저장된 북마크들에서 .getPost를 이용하여 함께 저장되어진 Post 객체들을 가져옴
        //findBy를 쓰면 해당하는 북마크 하나만 찾아올 것 같음
        //궁금점: 북마크가 하나가 아닐텐데 findAllBy를 써서 리스트로 담아줘야 하나..?
//        Post userPost = bookMarkRepository.findByUser(user).get().getPost();

        List<BookMark> userPosts = bookMarkRepository.findAllByUser(user);
        ArrayList<Post> userPostings = new ArrayList<>();
        for (BookMark userPost:userPosts){
            Post userPosting = userPost.getPost();
            userPostings.add(userPosting);
        }

        //PostRepository에서 모든 게시글을 리스트 형태로 불러옴
        List<Post> posts = postRepository.findAll();

        //BookMarkStatus를 추가적으로 담아줄 ArrayList 생성
        ArrayList<PostResponseDto> postList = new ArrayList<>();

        //true || false 값을 담아줄 boolean type의 bookMarkStatus 변수를 하나 생성

        Boolean bookMarkStatus = null ;
        //for문을 돌려 아까 찾아온 userPost라는 객체에 담아준 해당유저가 북마크한 게시물 객체를 모든 게시물과 비교
        //일치하면 bookMarkStatus = true 아니면 false를 bookMarkStatus에 담아줌
        //ex) 3개의 작성글이 있고
        for (Post post : posts) {
            Long postId = post.getId();
            User writer = post.getUser();
            //ex) 2개의 북마크가 있음
            for (Post userPost: userPostings ) {
                Long userPostId = userPost.getId();
                //객체대 객체일경우 if문으로 동일 비교 불가?
                //객체 안에있는 특정 데이터 타입으로 비교해줘야 함
                if (postId.equals(userPostId)) {
                    bookMarkStatus = true;
                } else {
                    bookMarkStatus = false;
                }
            }
                //PostResponseDto를 이용해 게시글과, 북마크 상태,user는 해당 게시글 유저의 프로필 이미지를 불러오기 위함
                //궁금증 현재 변수 user에 담긴 것은 로그인한 유저이기 때문에 이렇게 되면 해당유저가 작성한 글에만 프로필 이미지가 뜨지 않을까..?
                PostResponseDto postDto = new PostResponseDto(post, bookMarkStatus, writer);
                //아까 생성한 ArrayList에 새로운 모양의 값을 담아줌
                postList.add(postDto);
        }
        return postList;
    }




    // post 등록
    @Transactional
    public PostResponseDto postPost(PostRequestDto postRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 Id의 회원이 존재하지 않습니다.")
        );

        // 게시글 작성자 저장 (편의 메서드 -> member에도 post에 해당 post add)
        Post post = new Post(postRequestDto, user);
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.save(post));
        // 저장된 Post -> PostResponseDto에 담아 리턴
        return postResponseDto;

    }


    //게시글 수정

    //게시글 삭제

    //북마크

    //댓글
}

