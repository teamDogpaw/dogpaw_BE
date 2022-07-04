package com.project.dogfaw.post.service;

import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.PostResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

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
    //post 전체
    public List<PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtoList.add(postResponseDto);
        }
        return postResponseDtoList;
    }


    //게시글 수정

    //게시글 삭제

    //북마크

    //댓글
}