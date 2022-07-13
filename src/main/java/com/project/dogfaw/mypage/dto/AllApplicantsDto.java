package com.project.dogfaw.mypage.dto;

import com.project.dogfaw.user.model.Stack;
import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Getter
public class AllApplicantsDto {
    private Long userId;
    private String username;
    private String nickname;
    private String profileImg;
    private List<Stack> stacks;

    public AllApplicantsDto(User applier) {
        this.userId = applier.getId();
        this.username = applier.getUsername();
        this.nickname = applier.getNickname();
        this.profileImg = applier.getProfileImg();
        this.stacks = applier.getStacks();
    }
}
