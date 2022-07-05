package com.project.dogfaw.post.service;


import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.post.dto.PostDetailResponseDto;
import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
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
    public ArrayList<PostResponseDto> allPost(String username) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal();
//        String username = principal.getUsername();

        Long userId = userRepository.findByUsername(username).get().getId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("해당 ID가 존재하지 않음")
        );


        Post userPost = bookMarkRepository.findByUser(user).get().getPost();

        List<Post> posts = postRepository.findAll();
        ArrayList<PostResponseDto> postList = new ArrayList<>();
        boolean bookMarkStatus;
        for (Post post : posts) {
            if (userPost.equals(post)) {
                bookMarkStatus = true;

            } else {
                bookMarkStatus = false;
            }

            PostResponseDto postDto = new PostResponseDto(post, bookMarkStatus,user);
            postList.add(postDto);
        }
        return postList;
    }



    // post 등록
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

    //북마크

    //댓글
}

