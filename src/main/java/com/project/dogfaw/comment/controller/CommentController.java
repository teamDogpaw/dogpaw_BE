//package com.project.dogfaw.comment.controller;
//
//import com.project.dogfaw.comment.dto.CommentRequestDto;
//import com.project.dogfaw.comment.model.Comment;
//import com.project.dogfaw.comment.service.CommentService;
//import com.project.dogfaw.security.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//public class CommentController {
//
//    private final CommentService commentService;
//
//    @GetMapping("/api/posts/{postId}/comments")
//    public List<Comment> getComment(@PathVariable Long contentId){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//        String username = principal.getUser().getUsername();
//
//        return commentService.getComment(contentId);
//    }
//
//    @PostMapping("/api/posts/{postId}/comments")
//    public Long postComment(@PathVariable Long contentId, @RequestBody CommentRequestDto commentDto){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//        String username = principal.getUser().getUsername();
//
//        return commentService.postComment(contentId,commentDto);
//    }
//
//    @PutMapping("/api/posts/{postId}/comments/{commentId}")
//    public Long updateComment(@PathVariable Long contentId, @PathVariable Long id){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//        String username = principal.getUser().getUsername();
//
//        return commentService.updateComment(contentId,id);
//    }
//
//    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
//    public Long deleteComment(@PathVariable Long contentId,@PathVariable Long id){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//        String username = principal.getUser().getUsername();
//
//        return commentService.deleteComment(contentId,id);
//    }
//}