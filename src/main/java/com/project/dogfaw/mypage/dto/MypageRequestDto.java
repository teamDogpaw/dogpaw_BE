package com.project.dogfaw.mypage.dto;

import com.project.dogfaw.user.model.Stack;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
public class MypageRequestDto {
    private String nickName;
    private List<Stack> stacks;

//    public MypageRequestDto(String nickName, List<Stack> stacks) {
//        this.nickName = nickName;
//        this.stacks = stacks;
//    }
}
