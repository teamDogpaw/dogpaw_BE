package com.project.dogfaw.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    USER_STATUS_WRITER("writer"), //작성자
    USER_STATUS_MEMBER("member"), //일반유저
    USER_STATUS_NORMAL("memr"), //신청완료
    USER_STATUS_NOTYET("mer"); //지원안한사람

    private String userStatus;
}
