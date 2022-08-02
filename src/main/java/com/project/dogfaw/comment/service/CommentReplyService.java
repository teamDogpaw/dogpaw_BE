package com.project.dogfaw.comment.service;

import com.project.dogfaw.comment.dto.CmtReplyPutDto;
import com.project.dogfaw.comment.dto.CmtReplyReqeustDto;
import com.project.dogfaw.comment.dto.CmtReplyResponseDto;
import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.model.CommentReply;
import com.project.dogfaw.comment.repository.CommentReplyRepository;
import com.project.dogfaw.comment.repository.CommentRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.sse.model.NotificationType;
import com.project.dogfaw.sse.service.NotificationService;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Component

public class CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    //대댓글등록
    @Transactional
    public void saveCmtReply(Long commentId, User user, CmtReplyReqeustDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(RuntimeException::new);

        String content = requestDto.getContent();
        CommentReply cmtReply = new CommentReply(content, user, comment);

        commentReplyRepository.save(cmtReply);

        //알림
        String Url = "https://dogpaw.kr/detail/"+comment.getPost().getId();
        //댓글 생성 시 모집글 작성 유저에게 실시간 알림 전송 ,
        String notificationContent = user.getNickname()+"님이 댓글을 남기셨습니다!";


        //본인의 게시글에 댓글을 남길때는 알림을 보낼 필요가 없다.
        if(!Objects.equals(user.getId(), comment.getUser().getId())) {
            notificationService.send(comment.getUser(), NotificationType.REPLY, notificationContent, Url);
        }
    }

    //대댓글조회
    @Transactional
    public List<CmtReplyResponseDto> getCommentReplyByCommentId(Long commentId) {
        List<CommentReply> commentReplyListByCommentId = commentReplyRepository.findByCommentId(commentId);

        List<CmtReplyResponseDto> cmtReplyResponseDtoList = new ArrayList<>();

        for (CommentReply commentReply : commentReplyListByCommentId) {
            CmtReplyResponseDto cmtReplyResponseDto = new CmtReplyResponseDto(commentReply);
            cmtReplyResponseDtoList.add(cmtReplyResponseDto); 

        }
        return cmtReplyResponseDtoList;
    }

        //대댓글삭제
        public Boolean deleteCommentReply(Long commentReplyId, User user, Long commentId) {
            CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                    .orElseThrow(()->new CustomException(ErrorCode.COMMENT_NOT_FOUND)
            );

            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(RuntimeException::new);
            Long writerId = commentReply.getUser().getId();
            Long userId = user.getId();
            if (!writerId.equals(userId)) {
                return false;
            } else {
                commentReplyRepository.delete(commentReply);
            }
            return true;
        }

        //대댓글수정
        public Boolean updateCommentReply(Long commentReplyId, User user, CmtReplyPutDto requestDto) {
            CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                    .orElseThrow(()->new CustomException(ErrorCode.COMMENT_NOT_FOUND)
            );
            Long writerId = commentReply.getUser().getId();
            Long userId = user.getId();
            String cmtReply = requestDto.getContent();
            if (!writerId.equals(userId)) {
                return false;
            } else {
                commentReply.setContent(cmtReply);
                commentReplyRepository.save(commentReply);
            }
            return true;
        }

}
