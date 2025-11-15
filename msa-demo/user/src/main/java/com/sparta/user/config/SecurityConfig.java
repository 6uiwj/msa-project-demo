package com.sparta.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            //CSRF 비활성화 (REST API라면 대부분 꺼둔다)
            .csrf(csrf -> csrf.disable())

            //기본 제공 로그인 페이지(formLogin) 비활성화
            .formLogin(form -> form.disable())

            //기본 제공 HTTP Basic 인증 비활성화
            .httpBasic(basic -> basic.disable())

            //세션을 사용하지 않도록 설정 (JWT 기반 서비스라면 필수)
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                )
            )

            // 🔥 URL 권한 부여 설정 (예: 모두 허용)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}

