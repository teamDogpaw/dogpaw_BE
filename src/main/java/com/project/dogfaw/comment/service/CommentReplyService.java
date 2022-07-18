package com.project.dogfaw.comment.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.project.dogfaw.comment.dto.CmtReplyPutDto;
import com.project.dogfaw.comment.dto.CmtReplyReqeustDto;
import com.project.dogfaw.comment.dto.CmtReplyResponseDto;
import com.project.dogfaw.comment.model.Comment;
import com.project.dogfaw.comment.model.CommentReply;
import com.project.dogfaw.comment.repository.CommentReplyRepository;
import com.project.dogfaw.comment.repository.CommentRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Component

public class CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    //댓글등록
    @Transactional
    public void saveCmtReply(Long commentId, User user, CmtReplyReqeustDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(RuntimeException::new);
        String profileImg = user.getProfileImg();
        String nickname = user.getNickname();
        String content = requestDto.getContent();
        CommentReply cmtReply = new CommentReply(content, nickname, profileImg, user, comment);

        commentReplyRepository.save(cmtReply);
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
            CommentReply commentReply = commentReplyRepository.findById(commentReplyId).orElseThrow(() -> new IllegalArgumentException("존재하지않는 댓글입니다")
            );
            Comment comment = commentRepository.findById(commentId).orElseThrow(RuntimeException::new);
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
            CommentReply commentReply = commentReplyRepository.findById(commentReplyId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
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
