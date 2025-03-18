package com.example.beingus_be.config;

import com.project.beingus_be.security.AuthenticationFilter;
import com.project.beingus_be.security.CustomAccessDeniedHandler;
import com.project.beingus_be.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)

                // 특정 경로에 인증없이 접근 허용
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/**"
                        ).permitAll()   // 경로 허용 설정
                    .requestMatchers(
                           "/api/user/admin"
                    ).hasRole("admin")
                    .anyRequest().authenticated())  // 그 외 경로는 인증 필요
                .exceptionHandling((httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                .accessDeniedHandler(new CustomAccessDeniedHandler())))
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // PasswordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}
}
