package com.project.dogfaw.mypage.dto;

import com.project.dogfaw.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class AllTeammateDto {
    private Long userId;
    private String username;
    private String nickname;
    private String profileImg;
    private List<String> stacks;

    public AllTeammateDto(User teammateUser, List<String> stackList) {
        this.userId = teammateUser.getId();
        this.username = teammateUser.getUsername();
        this.nickname = teammateUser.getNickname();
        this.profileImg = teammateUser.getProfileImg();
        this.stacks = stackList;
    }

}
