package com.project.dogfaw.post.service;


import com.project.dogfaw.bookmark.repository.BookMarkRepository;
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

