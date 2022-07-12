package com.project.dogfaw.comment.service;

import com.project.dogfaw.comment.dto.CommentPutDto;
import com.project.dogfaw.comment.dto.CommentRequestDto;
import com.project.dogfaw.comment.dto.CommentResponseDto;
import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.repository.CommentRepository;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 등록
    @Transactional
    public void saveNewComments(Long postId, User user, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(RuntimeException::new);
        String profileImg = user.getProfileImg();
        String nickname = user.getNickname();
        String content = requestDto.getContent();
        Comment cmt = new Comment(content, nickname, profileImg, user, post);
        post.increaseCmCount();

        commentRepository.save(cmt);
    }


    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> commentListByPostId = commentRepository.findAllByPostId(postId);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentListByPostId) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }

        return commentResponseDtoList;
    }

    // 댓글 삭제
    public Boolean deleteComment(Long commentId, User user, Long postId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);

        Long writerId = comment.getUser().getId();
        Long userId = user.getId();
        if (!writerId.equals(userId)) {
            return false;
        } else {
            post.decreaseCmCount();
            commentRepository.delete(comment);
        }

        return true;
    }

    // 댓글 수정
    public Boolean updateComment(Long commentId, User user, CommentPutDto requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
        // comment내의 memberid와 로그인한 member아이디 일치하는지 확인
        Long writerId = comment.getUser().getId();
        Long userId = user.getId();
        String cmt = requestDto.getContent();
        if (!writerId.equals(userId)) {
            return false;
        } else {
            comment.setContent(cmt);
            commentRepository.save(comment);
        }
        return true;
    }
}

