package com.hecto.fitnessuniv.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hecto.fitnessuniv.filter.JwtAuthenticationFilter;
import com.hecto.fitnessuniv.handler.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티를 사용하면 default 로 로그인 안된 사용자의
// 모든 접근을 팅겨냄 로그인을 들어가져야지 홈이랑 스웨거는 들어가져야지 열어주자
@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    protected SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // csrf 인증 꺼주기
                .csrf(CsrfConfigurer::disable)
                // Bearer 방식을 사용할건데 스프링 시큐리티는 httpBasic 이 잡혀있음
                .httpBasic(HttpBasicConfigurer::disable)
                // Session 방식은 사용안할거여서 끔(stateless)
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))
                // 경로별 인가
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers(
                                                "/",
                                                "/api/v1/oauth2/*",
                                                "/login/oauth2/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui.html",
                                                "/swagger-ui/**",
                                                "/api/role")
                                        .permitAll()
                                        .requestMatchers("/api/v1/user/*")
                                        .hasRole("USER")
                                        .requestMatchers("/api/v1/admin/*")
                                        .hasRole("ADMIN")
                                        // 열어준거 외 나머지는 모두 인증 필요하게
                                        .anyRequest()
                                        .authenticated())
                // 테스트용 코드
                .oauth2Login(
                        oauth2 ->
                                oauth2.authorizationEndpoint( // 요청 보낼 커스텀 리다이렉트 uri
                                                // 원래는
                                                // http://localhost:8080/oauth2/authorization/naver
                                                // 커스텀
                                                // http://localhost:8080/api/v1/oauth2/authorization/naver
                                                endpoint ->
                                                        endpoint.baseUri(
                                                                "/api/v1/oauth2/authorization"))
                                        .redirectionEndpoint(
                                                endpoint ->
                                                        endpoint.baseUri("/login/oauth2/code/*"))
                                        .userInfoEndpoint(
                                                endpoint ->
                                                        endpoint.userService(
                                                                defaultOAuth2UserService))
                                        .successHandler(oAuth2SuccessHandler))
                // sout 은 defaultOAuth2UserService 에 있음
                // 여기까지 테스트 콘솔에 값들어오는거 확인 완 ~
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling.authenticationEntryPoint(
                                        new FailedAuthenticationEntryPoint()))
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // cors 설정
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
