package com.project.dogfaw.user.service;

import com.project.dogfaw.acceptance.model.Acceptance;
import com.project.dogfaw.acceptance.repository.AcceptanceRepository;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.validator.UserValidator;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.sse.security.jwt.JwtReturn;
import com.project.dogfaw.sse.security.jwt.JwtTokenProvider;
import com.project.dogfaw.sse.security.jwt.TokenDto;
import com.project.dogfaw.sse.security.jwt.TokenRequestDto;
import com.project.dogfaw.user.dto.LoginDto;
import com.project.dogfaw.user.dto.SignupRequestDto;
import com.project.dogfaw.user.dto.StackDto;
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
    private final CommonService commonService;
    private final UserApplicationRepository userApplicationRepository;
    private final AcceptanceRepository acceptanceRepository;


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

        // 유효성 검사
//        UserValidator.validateInputUsername(requestDto);
        UserValidator.validateInputPassword(requestDto);


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
            if (user.getKakaoId()!=null){
                throw new CustomException(ErrorCode.LOGIN_NOT_KAKAOUSER);
            }else {
                throw new CustomException(ErrorCode.LOGIN_PASSWORD_NOT_MATCH);
            }
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
    public TokenDto SignupUserCheck(Long kakaoId) {

        User loginUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        TokenDto tokenDto = jwtTokenProvider.createToken(loginUser);
        RefreshToken refreshToken = new RefreshToken(loginUser.getUsername(), tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
        return TokenDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(refreshToken.getRefreshValue())
                .build();
    }

    //추가 정보 기입
    @Transactional
    public TokenDto addInfo(SignupRequestDto requestDto, User user1) {
        // 닉네임 중복 확인
        String nickname = requestDto.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.SIGNUP_NICKNAME_DUPLICATE_CHECK);
        }

        User user = userRepository.findById(user1.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_NOT_FOUNT_MEMBERID));

        user.updateNickname(requestDto);
        List<Stack> stack = stackRepository.saveAll(tostackByUserId(requestDto.getStacks(),user));
        user.updateStack(stack);

        TokenDto tokenDto = jwtTokenProvider.createToken(user);

        RefreshToken refreshToken = new RefreshToken(user.getUsername(), tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    // 회원 탈퇴 메소드 (회원삭제가 아니라 회원정보를 삭제)
    @Transactional
    public void deleteUser() {
        User foundUser = commonService.getUser();

        if (foundUser != null) {
            String username = "deleteUser_"+foundUser.getId();
            String password = passwordEncoder.encode(UUID.randomUUID().toString());
            String nickname = "알수없음";

            //회원탈퇴시 참여한 프로젝트에서 삭제하기전 참여한 프로젝트 현재 모집인원 -1
            List<Acceptance> acceptances =acceptanceRepository.findAllByUser(foundUser);
            for(Acceptance acceptance: acceptances){
                Post post = acceptance.getPost();
                post.decreaseCnt();
                //모집인원 수 체크후 최대모집인원보다 현재모집인원이 적을경우 모집 중 으로 변경
                Boolean deadline = false;
                if(post.getCurrentMember()<post.getMaxCapacity()){
                    post.updateDeadline(deadline);
                }
            }


            userApplicationRepository.deleteByUserId(foundUser.getId());
            acceptanceRepository.deleteByUserId(foundUser.getId());
            List<Stack> stacks =stackRepository.findByUserId(foundUser.getId());
            for (Stack stack : stacks){
                stack.setStack(null);
                stack.setUserId(0L);
            }
            foundUser.setUsername(username);
            foundUser.setNickname(nickname);
            foundUser.setPassword(password);
            foundUser.setProfileImg(null);
            foundUser.setImgkey(null);
            foundUser.setRole(UserRoleEnum.USER);
            foundUser.setStacks(null);
            foundUser.setKakaoId(null);
//            userRepository.save(foundUser);
        }
    }

    private List<Stack> tostackByUserId(List<StackDto> requestDto, User user) {
        List<Stack> stackList = new ArrayList<>();
        for(StackDto stackdto : requestDto){
            stackList.add(new Stack(stackdto, user));
        }
        return stackList;
    }
}