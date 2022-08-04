package com.project.dogfaw.common.validator;

import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.sse.security.jwt.TokenRequestDto;
import com.project.dogfaw.user.dto.LoginDto;
import com.project.dogfaw.user.dto.SignupRequestDto;

import java.util.regex.Pattern;

public class UserValidator {

    public static void validateInputUsername(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();

        //이메일 형식
        String patternUsername = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$";

        // 이메일 설정 유효성 검사
        if (username == null || !Pattern.matches(patternUsername, username)) {
            throw new CustomException(ErrorCode.SIGNUP_MEMBERID_WRONG_INPUT);
        }
    }

    public static void validateInputPassword(SignupRequestDto requestDto) {

        String password = requestDto.getPassword();
        String passwordConfirm = requestDto.getPasswordConfirm();


        // 8자에서 16자 이내, 대소문자, 숫자.
        String patternPw = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,15}$";

        // 비밀번호 설정 유효성 검사
        if (password == null || !Pattern.matches(patternPw, password)) {
            throw new CustomException(ErrorCode.SIGNUP_PASSWORD_WRONG_INPUT);
        }

        // 비밀번호 확인 유효성 검사
        if (!password.equals(passwordConfirm)) {
            throw new CustomException(ErrorCode.SIGNUP_PWCHECK_WRONG_INPUT);
        }
    }

    public static void validateUsernameEmpty(LoginDto loginDto) {

        String username = loginDto.getUsername();

        if (username.isEmpty()) {
            throw new CustomException(ErrorCode.LOGIN_MEMBERID_EMPTY);
        }
    }

    public static void validatePasswordEmpty(LoginDto loginDto) {

        String password = loginDto.getPassword();

        if (password.isEmpty()) {
            throw new CustomException(ErrorCode.LOGIN_PASSWORD_EMPTY);
        }
    }

    public static void validateRefreshTokenReissue(TokenRequestDto tokenRequestDto) {

        String accessToken = tokenRequestDto.getAccessToken();
        String refreshToken = tokenRequestDto.getRefreshToken();
        Long userId = tokenRequestDto.getUserId();

        if (accessToken == null || refreshToken == null || userId == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_REISSUE_WRONG_INPUT);
        }
    }
}
