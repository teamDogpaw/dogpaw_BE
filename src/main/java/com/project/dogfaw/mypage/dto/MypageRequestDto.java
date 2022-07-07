package com.project.dogfaw.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Stack;


@NoArgsConstructor
@Getter
public class MypageRequestDto {
    private String profileImg;
    private String nickName;
    private List<Stack> stacks;

    public MypageRequestDto(String profileImg, String nickName, List<Stack> stacks) {
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.stacks = stacks;
    }
}
