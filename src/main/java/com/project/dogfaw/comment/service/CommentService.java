package com.project.dogfaw.comment.service;

import com.amazonaws.services.networkfirewall.model.Header;
import com.project.dogfaw.comment.dto.CmtReplyResponseDto;
import com.project.dogfaw.comment.dto.CommentPutDto;
import com.project.dogfaw.comment.dto.CommentRequestDto;
import com.project.dogfaw.comment.dto.CommentResponseDto;
import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.model.CommentReply;
import com.project.dogfaw.comment.repository.CommentRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.sse.model.NotificationType;
import com.project.dogfaw.sse.service.NotificationService;
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
    private final NotificationService notificationService;

    // 댓글 등록
    @Transactional
    public void saveNewComments(Long postId, User user, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.COMMENT_WRONG_INPUT));
        String profileImg = user.getProfileImg();
        String nickname = user.getNickname();
        String content = requestDto.getContent();
        Comment cmt = new Comment(content, nickname, profileImg, user, post);

        //알림
        //해당 댓글로 이동하는 url
        String Url = "https://dogpaw.kr/detail/"+post.getId();
        //댓글 생성 시 모집글 작성 유저에게 실시간 알림 전송 ,
        String notificationContent = user.getNickname()+"님이 댓글을 남기셨습니다!";

        //본인의 게시글에 댓글을 남길때는 알림을 보낼 필요가 없다.
        if(!Objects.equals(user.getId(), post.getUser().getId())) {
            notificationService.send(post.getUser(), NotificationType.REPLY, notificationContent, Url);
        }

        post.increaseCmCount();

        commentRepository.save(cmt);
    }


    // 댓글 조회
    @Transactional(readOnly = true)
    public StatusResponseDto getCommentsByPostId(Long postId) {
        //레포에서 코멘트 리스트 찾음
        List<Comment> commentListByPostId = commentRepository.findAllByPostId(postId);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();


        for (Comment comment : commentListByPostId) {
            //ArrayList 배열이 초기화 되어지지 않아 이전 대댓글 배열이 계속 쌓여서 다음으로 넘어가고 있었음
            List<CmtReplyResponseDto> commentReplies = new ArrayList<>();
            List<CommentReply> commentReplyList = comment.getCommentReplyList();
            for(CommentReply commentReply: commentReplyList){
                CmtReplyResponseDto cmtReplyResponseDto = new CmtReplyResponseDto(commentReply);
                commentReplies.add(cmtReplyResponseDto);

            }
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment,commentReplies);
            commentResponseDtoList.add(commentResponseDto);
        }
        return new StatusResponseDto("댓글 조회에 성공하였습니다", commentResponseDtoList);
    }
    @Transactional
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

