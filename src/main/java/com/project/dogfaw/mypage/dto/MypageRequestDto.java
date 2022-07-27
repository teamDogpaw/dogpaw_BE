package com.project.dogfaw.mypage.dto;

import com.project.dogfaw.user.dto.StackDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class MypageRequestDto {
    private String nickname;
    private List<StackDto> stacks = new ArrayList<>();

}
