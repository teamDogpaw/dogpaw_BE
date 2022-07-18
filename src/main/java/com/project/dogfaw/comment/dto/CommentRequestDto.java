package com.project.dogfaw.comment.dto;

import com.project.dogfaw.comment.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CommentRequestDto {

    private String content;
}