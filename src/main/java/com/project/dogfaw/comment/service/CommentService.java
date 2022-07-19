package com.project.dogfaw.comment.service;

import com.amazonaws.services.networkfirewall.model.Header;
import com.project.dogfaw.comment.dto.CommentPutDto;
import com.project.dogfaw.comment.dto.CommentRequestDto;
import com.project.dogfaw.comment.dto.CommentResponseDto;
import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.repository.CommentRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
                .orElseThrow(()->new CustomException(ErrorCode.COMMENT_WRONG_INPUT));
        String profileImg = user.getProfileImg();
        String nickname = user.getNickname();
        String content = requestDto.getContent();
        Comment cmt = new Comment(content, nickname, profileImg, user, post);
        post.increaseCmCount();

        commentRepository.save(cmt);
    }


    // 댓글 조회
    @Transactional(readOnly = true)
    public StatusResponseDto getCommentsByPostId(Long postId) {
        List<Comment> commentListByPostId = commentRepository.findAllByPostId(postId);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentListByPostId) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        return new StatusResponseDto("댓글 조회에 성공하였습니다", commentResponseDtoList);
    }

    // 댓글 삭제
    public Boolean deleteComment(Long commentId, User user, Long postId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.COMMENT_NOT_FOUND)
                );

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.COMMENT_DELETE_WRONG_ACCESS)
                );
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
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );
        String cmt = requestDto.getContent();
        //유저 확인 로직
        if (!comment.getUser().getId().equals(user.getId())) {
            return false;
        } else {
            comment.updateComment(cmt);
            commentRepository.save(comment);
        }
        return true;
    }
}

