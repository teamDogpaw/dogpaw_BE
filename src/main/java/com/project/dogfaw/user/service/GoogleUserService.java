package com.project.dogfaw.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.project.dogfaw.user.dto.GoogleUserInfoDto;
import com.project.dogfaw.user.dto.KakaoUserInfo;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client-id}")
    private String client_id;

    @Value("${google.secret-key}")
    private String secret;

    @Value("${google.redirect-uri}")
    private String redirect_uri;


}
