package com.project.dogfaw.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleUserInfoDto  {

    private String id;
    private String email;
}
