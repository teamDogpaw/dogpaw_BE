package com.project.dogfaw.security;

import com.project.dogfaw.security.jwt.JwtAuthenticationFilter;
import com.project.dogfaw.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {

        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web

                .ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers(HttpMethod.GET,"/")
                .antMatchers(HttpMethod.POST,"/user/signup/**")
                .antMatchers(HttpMethod.POST,"/user/nickname/**")
                .antMatchers(HttpMethod.POST,"/user/login/**")
                .antMatchers(HttpMethod.GET,"/user/userInfo/**")
                .antMatchers(HttpMethod.GET,"/user/kakao/login/**")
                .antMatchers(HttpMethod.GET,"/api/all/**")
                .antMatchers(HttpMethod.GET,"/api/bookMark/rank");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //formlogin사용 x
        http.httpBasic().disable();

        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http.authorizeRequests();
        // 회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시

        http.authorizeRequests()
                // login 없이 허용
                .antMatchers(HttpMethod.POST,"/user/signup/**").permitAll()
                .antMatchers(HttpMethod.POST,"/user/nickname/**").permitAll()
                .antMatchers(HttpMethod.POST,"/user/login/**").permitAll()
                .antMatchers(HttpMethod.GET,"/user/userInfo/**").permitAll()
                .antMatchers(HttpMethod.GET,"/user/kakao/login/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/all/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/bookMark/rank").permitAll()


//                .antMatchers("/api/posts").permitAll()
//                .antMatchers("/api/preview").permitAll()
//                .antMatchers("/post/filter/**").permitAll()
//                .antMatchers("/webSocket/**").permitAll()
//                .antMatchers("/search/**").permitAll()
//                .antMatchers("/subscribe/**").permitAll()
//                .antMatchers("/v2/api-docs","/v3/api-docs", "/swagger-resources/**", "**/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger*/**","/swagger-ui","/swagger-ui/**").permitAll()

                //추가 - 메인 페이지 접근 허용
//                .antMatchers("/").permitAll()

                // 그 외 어떤 요청이든 '인증'과정 필요
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/403").and().httpBasic()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://d2yxbwsc3za48s.cloudfront.net"); //https 주소
        configuration.addAllowedOrigin("http://scatch.s3-website.ap-northeast-2.amazonaws.com"); //http 주소
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        //configuration.addAllowedHeader("/"); //502에러때문에 추가
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
