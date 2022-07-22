package com.project.dogfaw.mypage.dto;

import com.project.dogfaw.user.dto.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Getter
public class OtherUserMypageResponseDto {

    UserInfo userInfo;
    List<MyAcceptanceResponseDto> acceptedPost;
    List<MyPostResponseDto> myPost;

    public OtherUserMypageResponseDto(UserInfo userInfo, List<MyAcceptanceResponseDto> myAcceptanceResponseDto, List<MyPostResponseDto> myPostResponseDto) {
        this.userInfo = userInfo;
        this.acceptedPost = myAcceptanceResponseDto;
        this.myPost = myPostResponseDto;
    }
}
