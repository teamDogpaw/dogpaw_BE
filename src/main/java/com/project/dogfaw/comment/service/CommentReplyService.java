//package com.project.dogfaw.comment.service;
//
//import com.amazonaws.services.dynamodbv2.xspec.S;
//import com.project.dogfaw.comment.dto.CmtReplyReqeustDto;
//import com.project.dogfaw.comment.model.Comment;
//import com.project.dogfaw.comment.repository.CommentReplyRepository;
//import com.project.dogfaw.comment.repository.CommentRepository;
//import com.project.dogfaw.user.model.User;
//import com.project.dogfaw.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//
//@RequiredArgsConstructor
//@Service
//public class CommentReplyService {
//
//    private final CommentReplyRepository commentReplyRepository;
//    private final CommentRepository commentRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public void saveCmtReply(Long commentId, User user, CmtReplyReqeustDto requestDto) {
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(RuntimeException::new);
//        String profileImg = user.getProfileImg();
//        String nickname = user.getNickname();
//        //String content = requestDto.getContent();
//
//    }
//
//
//
//}
//
