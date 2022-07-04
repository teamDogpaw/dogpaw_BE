package com.project.dogfaw.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfo {

    private Long userId;
    private Long kakaoId;
    private String kakaoMemberId;
//    private boolean isProfileSet;
}
