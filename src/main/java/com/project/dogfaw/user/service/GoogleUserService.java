//package com.project.dogfaw.user.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.dogfaw.user.dto.GoogleUserInfo;
//import com.project.dogfaw.user.model.User;
//import com.project.dogfaw.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class GoogleUserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Value("${google.client-id}")
//    private String client_id;
//
//    @Value("${google.secret-key}")
//    private String secret;
//
//    @Value("${google.redirect-uri}")
//    private String redirect_uri;
//
//    public GoogleUserInfo googleLogin(String code) throws JsonProcessingException {
//        // "인가 코드"로 AccessToken 요청
//        String accessToken = getAccessToken(code);
//
//        GoogleUserInfo googleUserInfo = getGoogleUserInfo(accessToken);
//
//        User googleUser = userRepository.findByUsername(googleUserInfo.getEmail()).orElse(null);
//        if (googleUser == null) {
//            registerGoogleUser(googleUserInfo);
//        }
//        return getGoogleUserInfo(accessToken);
//    }
//
//    private String getAccessToken(String code) throws JsonProcessingException {
//
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP Body 생성
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("google_client-id", client_id);
//        body.add("google_client-secret", secret);
//        body.add("code", code);
//        body.add("google-redirect_uri", redirect_uri);
//        body.add("grant_type", "authorization_code");
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(body, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://oauth2.googleapis.com/token",
//                HttpMethod.POST, googleTokenRequest,
//                String.class
//        );
//
//        // HTTP 응답 (JSON) -> AccessToken 파싱
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();    // Json 형식 java에서 사용하기 위해 objectMapper 사용
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
//    }
//
//    // AccessToken 으로 카카오 사용자 정보 가져오기
//    private GoogleUserInfo getGoogleUserInfo(String accessToken) throws JsonProcessingException {
//
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://openidconnect.googleapis.com/v1/userinfo",
//                HttpMethod.POST,
//                googleUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        String id = jsonNode.get("id").asText();
//        String email = jsonNode.get("google_account").get("email").asText();
//        return GoogleUserInfo.builder()
//                .id(id)
//                .email(email)
//                .build();
//    }
//
//    // 첫 소셜로그인일 경우 DB 저장
//    private void registerGoogleUser(GoogleUserInfo googleUserInfo) {
//
//        String password = UUID.randomUUID().toString();
//        String encodedPassword = passwordEncoder.encode(password);
//
//        User googleUser = User.builder()
//                .googleId(googleUserInfo.getId())
//                .username(googleUserInfo.getEmail())
//                .password(encodedPassword)
//                .build();
//        userRepository.save(googleUser);
//    }
//}
