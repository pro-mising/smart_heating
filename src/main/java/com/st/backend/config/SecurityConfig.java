package com.st.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st.backend.config.filter.JwtAuthenticationTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.io.PrintWriter;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    IpAddressMatcher hasIpAddress = new IpAddressMatcher("127.0.0.1");

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // 基于token，不需要csrf
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 不需要session
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/user/login/", "/user/register/", "/user/bot/getlist/").permitAll()
                        .requestMatchers("/pk/start/game/").access((authentication, context) -> {
                            String remoteAddr = context.getRequest().getRemoteAddr();
                            System.out.println("请求来自 IP: " + remoteAddr);
                            return new AuthorizationDecision(hasIpAddress.matches(context.getRequest()));
                        })
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler()) // 👈 重点：权限不足时返回 JSON
                )
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 自定义权限不足处理器
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> responseBody = Map.of(
                    "error_message", "权限不足：" + accessDeniedException.getMessage(),
                    "status", 403
            );

            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(responseBody));
            out.flush();
            out.close();
        };
    }
}

//    //这个方式是 直接让 Spring Security 完全不拦截该路径，适合静态资源或 WebSocket。
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/websocket/**");
//    }

