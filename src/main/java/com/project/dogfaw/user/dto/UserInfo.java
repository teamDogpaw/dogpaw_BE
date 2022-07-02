package com.project.dogfaw.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfo {

    private String username;
    private String nickname;
}
