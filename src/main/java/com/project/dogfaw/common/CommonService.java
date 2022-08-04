package com.project.dogfaw.common;

import com.project.dogfaw.sse.security.UserDetailsImpl;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonService {
    private final UserRepository userRepository;

    public User getUser() {
        log.info("===========================1-1고승유=====================================================");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();
        log.info("===========================1-2고승유=====================================================");

        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 유저입니다")
        );
    }
}


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
//        String username = principal.getUser().getUsername();
//        com.project.dogfaw.user.model.User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new UsernameNotFoundException("존재하지 않는 유저입니다")
//        );
