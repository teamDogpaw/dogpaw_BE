package com.project.dogfaw.user.repository;

import com.project.dogfaw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일 중복 확인
    boolean existsByUsername(String memberId);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);

    // 로그인
    Optional<User> findByUsername(String username);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByNickname(String nickname);

//    Optional<User> findByKakaoId(Long kakaoId);
}
