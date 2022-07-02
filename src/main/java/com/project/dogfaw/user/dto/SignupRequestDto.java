package com.project.dogfaw.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private String stack;
}
