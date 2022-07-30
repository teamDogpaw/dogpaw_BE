package com.project.dogfaw.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    USER_STATUS_MASTER("master"), //관리자
    USER_STATUS_AUTHOR("author"), //작성자
    USER_STATUS_PARTICIPANT("participant"), //수락된사람
    USER_STATUS_APPLICANT("applicant"), //신청완료
    USER_STATUS_MEMBER("MEMBER"), //지원안한사람
    USER_STATUS_ANONYMOUS("Guest"); //로그인하지않은사람

    private String userStatus; 
}
