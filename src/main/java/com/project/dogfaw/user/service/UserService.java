package com.project.dogfaw.user.service;

import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.validator.UserValidator;
import com.project.dogfaw.security.jwt.JwtTokenProvider;
import com.project.dogfaw.user.dto.SignupRequestDto;
import com.project.dogfaw.user.dto.UserInfo;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.RefreshTokenRepository;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // 일반 회원가입
    @Transactional
    public UserInfo register(SignupRequestDto requestDto) {

        // 회원 아이디 중복 확인
        String username = requestDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.SIGNUP_MEMBERID_DUPLICATE_CHECK);
        }

        // 회원 비밀번호 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = userRepository.save(
                User.builder()
                        .username(requestDto.getUsername())
                        .password(password)
                        .nickname(requestDto.getNickname())
                        .stack(requestDto.getStack())
                        .build()
        );

        return UserInfo.builder()
                .userId(user.getId())
                .isProfileSet(false)
                .build();
    }






}
