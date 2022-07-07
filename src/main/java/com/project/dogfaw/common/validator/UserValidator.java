package com.project.dogfaw.common.validator;

import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.security.jwt.TokenRequestDto;
import com.project.dogfaw.user.dto.LoginDto;
import com.project.dogfaw.user.dto.SignupRequestDto;

import java.util.regex.Pattern;

public class UserValidator {

    public static void validateInputNickname(SignupRequestDto requestDto) {

        String nickname = requestDto.getNickname();

        String patternNickname = "^[A-Za-z0-9가-힣]{2,6}$";

        // 닉네임 설정 유효성 검사
        if (nickname == null || !Pattern.matches(patternNickname, nickname)) {
            throw new CustomException(ErrorCode.SIGNUP_NICKNAME_WRONG_INPUT);
        }
    }

    public static void validateInputStack(SignupRequestDto requestDto) {

        if (requestDto.getStacks().size()==0) {
            throw new CustomException(ErrorCode.SIGNUP_MAJOR_WRONG_INPUT);
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
