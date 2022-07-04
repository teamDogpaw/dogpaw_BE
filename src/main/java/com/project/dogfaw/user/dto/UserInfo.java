package com.project.dogfaw.user.dto;

import com.project.dogfaw.user.model.Stack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserInfo {

    private String username;
    private String nickname;
    private String profileImg;
    private List<Stack> stacks;
}
