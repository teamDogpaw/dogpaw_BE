package com.project.dogfaw.user.controller;

import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.ExceptionResponse;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.sse.security.jwt.TokenDto;
import com.project.dogfaw.sse.security.jwt.TokenRequestDto;
import com.project.dogfaw.user.dto.KakaoUserInfo;
import com.project.dogfaw.user.dto.LoginDto;
import com.project.dogfaw.user.dto.SignupRequestDto;
import com.project.dogfaw.user.dto.UserInfo;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import com.project.dogfaw.user.service.KakaoUserService;
import com.project.dogfaw.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    private final KakaoUserService kakaoUserService;
//    private final GoogleUserService googleUserService;
    private final UserRepository userRepository;
    private final CommonService commonService;

    // 회원가입 API
    @PostMapping("/user/signup")
    public ResponseEntity<Object> registerUser(@RequestBody SignupRequestDto requestDto) {
        TokenDto tokenDto = userService.register(requestDto);
        return new ResponseEntity<>(new StatusResponseDto("회원가입 완료했습니다.", ""), HttpStatus.OK);
    }

    // 닉네임 중복검사 API
    @PostMapping("/user/nickname")
    public ResponseEntity<Object> nicknameCheck(@RequestBody SignupRequestDto requestDto){
        if(userRepository.existsByNickname(requestDto.getNickname())) {
            return new ResponseEntity<>(new ExceptionResponse(ErrorCode.SIGNUP_NICKNAME_DUPLICATE), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new StatusResponseDto("사용가능한 닉네임입니다.", ""), HttpStatus.OK);
        }
    }

    // 로그인 API
    @PostMapping("/user/login")
    public ResponseEntity<Object> login(HttpServletRequest httpServletRequest,@RequestBody LoginDto loginDto) {
        Map<String, Object> data = userService.login(loginDto);
        return new ResponseEntity<>(new StatusResponseDto("로그인에 성공하셨습니다", data), HttpStatus.OK);
    }

    // 로그아웃 API
    @PostMapping("/user/logout")
    public ResponseEntity<Object> logout(@RequestBody TokenRequestDto tokenRequestDto) {
        userService.deleteRefreshToken(tokenRequestDto);
        return new ResponseEntity<>(new StatusResponseDto("로그아웃 성공", ""), HttpStatus.OK);
    }

    // 토큰 재발행 API
    @PostMapping("/user/reissue")
    public ResponseEntity<Object> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        TokenDto tokenDto = userService.reissue(tokenRequestDto);
        return new ResponseEntity<>(new StatusResponseDto("토큰 재발급 성공", tokenDto), HttpStatus.OK);
    }

    // 유저 정보 API
    @GetMapping("/user/userinfo")
    public UserInfo Session(){
        User user = commonService.getUser();
        return new UserInfo(user.getUsername(), user.getNickname(),user.getProfileImg(), user.getStacks());
    }

    // 카카오 로그인 API
    @GetMapping("/user/kakao/login")
    public void kakaoLogin(HttpServletRequest httpServletRequest,@RequestParam String code, HttpServletResponse response) throws IOException {
        KakaoUserInfo kakaoUserInfo = kakaoUserService.kakaoLogin(code);
        TokenDto tokenDto = userService.SignupUserCheck(kakaoUserInfo.getKakaoId());
        String url = "https://dogpaw.kr/?token=" + tokenDto.getAccessToken() + "&refreshtoken=" + tokenDto.getRefreshToken();
        User kakaoUser = userRepository.findByUsername(kakaoUserInfo.getKakaoMemberId()).orElse(null);
        if (kakaoUser.getNickname().equals("default")){
            url = url + "&nickname=default" + "&userId=" + kakaoUser.getId();
        }
        else{
            url = url + "&nickname=" + kakaoUser.getNickname() + "&userId=" + kakaoUser.getId();
        }
        response.sendRedirect(url);
    }


    // 회원가입 추가 정보 API
    @PostMapping("/user/signup/addInfo")
    public ResponseEntity<Object> addInfo(HttpServletRequest httpServletRequest,@RequestBody SignupRequestDto requestDto) {
        User user = commonService.getUser();
        userService.addInfo(requestDto,user);

        return new ResponseEntity<>(new StatusResponseDto("추가 정보 등록 성공",""), HttpStatus.CREATED);
    }

    // 회원 정보 삭제 API
    @PutMapping("/user/delete")
    public ResponseEntity<Object> deleteUser() {
        userService.deleteUser();
        return new ResponseEntity<>(new StatusResponseDto("회원 정보 삭제 성공",""), HttpStatus.CREATED);
    }

    /*요청에 대한 ip추적*/
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}