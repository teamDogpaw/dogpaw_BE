package com.project.dogfaw.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(HttpStatus.OK,  "200", "true"),


    //문자열 체크
    NOT_VALIDCONTENT(HttpStatus.BAD_REQUEST,"400","유효하지 않는 내용입니다."),
    NOT_VALIDURL(HttpStatus.BAD_REQUEST,"400","요효하지 않는 URL 입니다."),

    // 회원가입
    SIGNUP_MEMBERID_WRONG_INPUT(HttpStatus.BAD_REQUEST, "400", "아이디 형식을 맞춰주세요"),
    SIGNUP_PASSWORD_WRONG_INPUT(HttpStatus.BAD_REQUEST, "400", "비밀번호 형식을 맞춰주세요"),
    SIGNUP_PWCHECK_WRONG_INPUT(HttpStatus.BAD_REQUEST, "400", "비밀번호가 일치하지 않습니다"),

    SIGNUP_MEMBERID_DUPLICATE_CHECK(HttpStatus.BAD_REQUEST, "400", "아이디 중복확인을 해주세요"),
    SIGNUP_NICKNAME_DUPLICATE_CHECK(HttpStatus.BAD_REQUEST, "400", "닉네임 중복확인을 해주세요"),

    SIGNUP_USERID_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "userId가 존재하지 않습니다"),

    SIGNUP_MEMBERID_DUPLICATE(HttpStatus.BAD_REQUEST, "400", "해당 아이디가 이미 존재합니다"),
    SIGNUP_MEMBERID_CORRECT(HttpStatus.OK, "200", "사용할 수 있는 아이디입니다"),
    SIGNUP_NICKNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "400", "해당 닉네임이 이미 존재합니다"),
    SIGNUP_NICKNAME_OK(HttpStatus.BAD_REQUEST, "400", "해당 닉네임은 사용가능합니다"),
    SIGNUP_NICKNAME_CORRECT(HttpStatus.OK, "200", "사용할 수 있는 닉네임입니다"),

    // Token
    JWT_TOKEN_WRONG_SIGNATURE(HttpStatus.UNAUTHORIZED, "401", "잘못된 JWT 서명입니다"),
    JWT_TOKEN_NOT_SUPPORTED(HttpStatus.UNAUTHORIZED, "401", "지원되지 않는 JWT 토큰입니다."),
    JWT_TOKEN_WRONG_FORM(HttpStatus.UNAUTHORIZED, "401", "JWT 토큰이 잘못되었습니다."),

    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "401", "로그인이 만료되었습니다. 재로그인 하세요."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "Refresh Token이 존재하지 않습니다. 로그인 해주세요"),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "401", "Refresh Token이 일치하지 않습니다"),
    REFRESH_TOKEN_REISSUE_WRONG_INPUT(HttpStatus.BAD_REQUEST, "400", "userId, accessToken, refreshToken을 입력해주세요"),

    // 로그인
    LOGIN_NOT_FOUNT_MEMBERID(HttpStatus.BAD_REQUEST, "400", "해당 이메일을 찾을 수 없습니다"),
    LOGIN_NOT_KAKAOUSER(HttpStatus.BAD_REQUEST, "400", "카카오 USER는 카카오 로그인을 해주세요."),
    LOGIN_MEMBERID_EMPTY(HttpStatus.BAD_REQUEST, "400", "아이디를 입력해주세요"),
    LOGIN_PASSWORD_EMPTY(HttpStatus.BAD_REQUEST, "400", "비밀번호를 입력해주세요"),
    LOGIN_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "400", "비밀번호가 틀렸습니다. 다시 입력해주세요"),

    NOT_MATCH_USER_INFO(HttpStatus.BAD_REQUEST, "400", "유저 정보가 일치하지 않습니다"),

    //기타
    NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT(HttpStatus.INTERNAL_SERVER_ERROR, "998", "Security Context에 인증 정보가 없습니다."),
    NOT_FOUND_USER_INFO(HttpStatus.NOT_FOUND, "404", "해당 유저가 존재하지 않습니다"),

    // 이미지
    WRONG_INPUT_IMAGE(HttpStatus.BAD_REQUEST, "400", "이미지는 반드시 있어야 합니다"),
    IMAGE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "400", "이미지 업로드에 실패했습니다"),
    WRONG_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "400", "지원하지 않는 파일 형식입니다"),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 게시물을 찾을 수 없습니다"),

    POST_ALL_LOAD_FAIL(HttpStatus.NOT_FOUND, "404", "전체게시물 불러오기를 실패했습니다."),
    POST_RANK_LOAD_FAIL(HttpStatus.NOT_FOUND, "404", "랭크 게시물 불러오기를 실패했습니다."),

    POST_INQUIRY_NO_AUTHORITY(HttpStatus.FORBIDDEN,"403", "모집글 작성자만 가능합니다"),
    POST_PEOPLE_SET_CLOSED(HttpStatus.BAD_REQUEST, "400", "모집인원이 모두 찼을 경우 모집마감취소가 불가능합니다. 모집인원을 늘리거나 팀원 조정이 필요합니다."),
    POST_UPDATE_WRONG_ACCESS(HttpStatus.BAD_REQUEST, "400", "본인의 게시물만 수정할 수 있습니다"),
    POST_DELETE_WRONG_ACCESS(HttpStatus.BAD_REQUEST, "400", "본인의 게시물만 삭제할 수 있습니다"),
    POST_WRONG_INPUT(HttpStatus.BAD_REQUEST, "400", "비어있는 항목을 채워주세요"),
    POST_MAJOR_WRONG_INPUT(HttpStatus.BAD_REQUEST, "400", "모집 분야를 선택해주세요"),


    // comment
    COMMENT_WRONG_INPUT(HttpStatus.BAD_REQUEST, "400", "댓글을 입력해주세요"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 댓글을 찾을 수 없습니다"),
    COMMENT_UPDATE_WRONG_ACCESS(HttpStatus.BAD_REQUEST, "400", "본인의 댓글만 수정할 수 있습니다"),
    COMMENT_DELETE_WRONG_ACCESS(HttpStatus.BAD_REQUEST, "400", "본인의 댓글만 삭제할 수 있습니다"),

    // apply

    ALREADY_APPLY_POST_ERROR(HttpStatus.BAD_REQUEST,"400", "이미 지원한 프로젝트입니다"),
    APPLY_NOT_FOUND(HttpStatus.BAD_REQUEST,"404", "해당 지원 정보를 찾을 수 없습니다"),


    //acceptance
    ACCEPTANCE_NOT_FOUND(HttpStatus.FORBIDDEN,"404", "참여 수락정보가 존재하지 않습니다"),
    ACCEPTANCE_PEOPLE_SET_CLOSED(HttpStatus.BAD_REQUEST, "400", "모집인원이 모두 차 더이상 팀원을 받을 수 없습니다. 모집인원을 늘리거나 팀원 조정이 필요합니다."),
    //mypage
    MYPAGE_INQUIRY_NO_AUTHORITY(HttpStatus.FORBIDDEN,"403", "모집글 작성자만 가능합니다"),
    MYPAGE_REJECTION_NO_AUTHORITY(HttpStatus.FORBIDDEN,"403", "신청 거절은 작성자만 가능합니다"),
    MYPAGE_ACCEPTANCE_NO_AUTHORITY(HttpStatus.FORBIDDEN,"403", "신청 수락은 작성자만 가능합니다"),



    //sse
    NOT_EXIST_NOTIFICATION(HttpStatus.NOT_FOUND,"404","존재하지 않는 알림입니다."),
    FAIL_SUBSCRIBE(HttpStatus.NOT_FOUND,"404","알림구독을 실패하였습니다"),
    FAIL_LOAD_NOTIFICATION(HttpStatus.NOT_FOUND,"404","알림구독을 실패하였습니다"),

    FAIL_DELETE_All_NOTIFICATION(HttpStatus.INTERNAL_SERVER_ERROR,"500","알 수 없는 오류로 전체알림 삭제를 실패하였습니다."),
    FAIL_DELETE_NOTIFICATION(HttpStatus.INTERNAL_SERVER_ERROR,"500"," 알 수 없는 오류로 알림 삭제 처리를 실패하였습니다.");






    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;
}
