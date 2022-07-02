package com.project.dogfaw.security;

import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UserDetailsImpl loadUserByUsername(String userPk) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(userPk))
                .orElseThrow(() -> new UsernameNotFoundException(userPk + "은 존재하지 않는 아이디입니다."));

        return new UserDetailsImpl(user);
    }
}