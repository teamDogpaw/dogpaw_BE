package com.project.dogfaw.user.service;

import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.common.validator.UserValidator;
import com.project.dogfaw.mypage.dto.MypageResponseDto;
import com.project.dogfaw.security.UserDetailsImpl;
import com.project.dogfaw.security.jwt.JwtReturn;
import com.project.dogfaw.security.jwt.JwtTokenProvider;
import com.project.dogfaw.security.jwt.TokenDto;
import com.project.dogfaw.security.jwt.TokenRequestDto;
import com.project.dogfaw.user.dto.*;
import com.project.dogfaw.user.model.RefreshToken;
import com.project.dogfaw.user.model.Stack;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.model.UserRoleEnum;
import com.project.dogfaw.user.repository.RefreshTokenRepository;
import com.project.dogfaw.user.repository.StackRepository;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final StackRepository stackRepository;



    // 일반 회원가입
    @Transactional
    public TokenDto register(SignupRequestDto requestDto) {

        // 회원 아이디 중복 확인
        String username = requestDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.SIGNUP_MEMBERID_DUPLICATE_CHECK);
        }

        // 닉네임 중복 확인
        String nickname = requestDto.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.SIGNUP_NICKNAME_DUPLICATE_CHECK);
        }

        // 회원 비밀번호 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());


        User user = userRepository.save(
                User.builder()
                        .username(requestDto.getUsername())
                        .password(password)
                        .nickname(requestDto.getNickname())
                        .role(UserRoleEnum.USER)
                        .build()
        );

        List<Stack> stack = stackRepository.saveAll(tostackByUserId(requestDto.getStacks(),user));

        user.updateStack(stack);

        TokenDto tokenDto = jwtTokenProvider.createToken(user);

        RefreshToken refreshToken = new RefreshToken(user.getUsername(), tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }


    // 로그인
    @Transactional
    public Map<String, Object> login(LoginDto loginDto) {
        UserValidator.validateUsernameEmpty(loginDto);
        UserValidator.validatePasswordEmpty(loginDto);

        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.LOGIN_NOT_FOUNT_MEMBERID)
        );

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_PASSWORD_NOT_MATCH);
        }

        Long userId = user.getId();

        TokenDto tokenDto = jwtTokenProvider.createToken(user);

        RefreshToken refreshToken = new RefreshToken(loginDto.getUsername(), tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("token", tokenDto);

        return data;
    }

    // 로그아웃
    @Transactional
    public void deleteRefreshToken(TokenRequestDto tokenRequestDto) {
        User user = userRepository.findById(tokenRequestDto.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER_INFO)
        );
        String username = user.getUsername();

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshKey(username).orElseThrow(
                () -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );
        refreshTokenRepository.deleteById(refreshToken.getRefreshKey());
    }
    

    // reissue(Token 재발급)
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        log.info("Refresh Token : " + tokenRequestDto.getRefreshToken());

        UserValidator.validateRefreshTokenReissue(tokenRequestDto);

        // RefreshToken 만료됐을 경우
        if (jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken()) != JwtReturn.SUCCESS) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        User user = userRepository.findById(tokenRequestDto.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER_INFO)
        );

        // RefreshToken DB에 없을 경우
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshKey(user.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        // RefreshToken 일치하지 않는 경우
        if (!refreshToken.getRefreshValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        // Access Token, Refresh Token 재발급
        TokenDto tokenDto = jwtTokenProvider.createToken(user);
        RefreshToken updateRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(updateRefreshToken);

        return tokenDto;
    }

    // 카카오 로그인 유저 상태 확인
    public StatusResponseDto SignupUserCheck(Long kakaoId) {

        User loginUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (loginUser.getNickname().equals("default")) {
            KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                    .userId(loginUser.getId())
                    .kakaoId(kakaoId)
                    .build();
            return new StatusResponseDto("추가 정보 작성이 필요한 유저입니다", kakaoUserInfo);
        } else {
            TokenDto tokenDto = jwtTokenProvider.createToken(loginUser);
            return new StatusResponseDto("로그인 성공", tokenDto);
        }
    }

    // 구글 로그인 유저 상태 확인
//    public StatusResponseDto SignupUserCheck(String Id) {
//
//        User loginUser = userRepository.findByGoogleId(Id).orElse(null);
//
//        if (loginUser.getNickname().equals("default")) {
//            GoogleUserInfo kakaoUserInfo = GoogleUserInfo.builder()
//                    .id(Id)
//                    .email(loginUser.getUsername())
//                    .build();
//            return new StatusResponseDto("추가 정보 작성이 필요한 유저입니다", kakaoUserInfo);
//        } else {
//            TokenDto tokenDto = jwtTokenProvider.createToken(loginUser);
//            return new StatusResponseDto("로그인 성공", tokenDto);
//        }
//    }

    // 회원가입 추가 정보 등록
    @Transactional
    public TokenDto addInfo(SignupRequestDto requestDto) {

        // 닉네임 중복 확인
        String nickname = requestDto.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.SIGNUP_NICKNAME_DUPLICATE_CHECK);
        }

        // DB에서 유저 정보를 찾음
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.SIGNUP_USERID_NOT_FOUND)
        );

        user.addInfo(requestDto);

        List<Stack> stack = stackRepository.saveAll(tostackByUserId(requestDto.getStacks(),user));

        user.updateStack(stack);

        TokenDto tokenDto = jwtTokenProvider.createToken(user);

        RefreshToken refreshToken = new RefreshToken(user.getUsername(), tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }


//    public List<Stack> tostack(List<StackDto> stackDtoList)  {
//        List<Stack> stacks = new ArrayList<>();
//        for(StackDto stackdto : stackDtoList){
//            stacks.add(new Stack(stackdto));
//        }
//        return stacks;
//    }


    private List<Stack> tostackByUserId(List<StackDto> requestDto, User user) {
        List<Stack> stackList = new ArrayList<>();
        for(StackDto stackdto : requestDto){
            stackList.add(new Stack(stackdto, user));
        }
        return stackList;
    }
}