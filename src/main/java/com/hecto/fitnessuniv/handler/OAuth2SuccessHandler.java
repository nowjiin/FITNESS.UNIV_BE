package com.hecto.fitnessuniv.handler;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;
import com.hecto.fitnessuniv.service.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${front-url}")
    private String frontBaseUrl;

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // OAuth2UserService 에서 반환한 값을 받아옴
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 액세스 토큰 및 리프레시 토큰 생성
        String accessToken =
                jwtProvider.createAccessToken(oAuth2User.getId(), oAuth2User.getName());
        String refreshToken = jwtProvider.createRefreshToken(oAuth2User.getId());

        // 사용자 역할 확인
        UserEntity user =
                userRepository
                        .findByUserId(oAuth2User.getId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
        String role = user.getRole();

        // 네이버 구글 로직 따로 처리하기 위해 클라이언트 이름 추출
        String oauthClientName = request.getRequestURI().contains("naver") ? "naver" : "google";

        if (oauthClientName.equals("google")) {
            handleGoogleLogin(response, attributes, accessToken, refreshToken, role);
        }
        if (oauthClientName.equals("naver")) {
            handleNaverLogin(response, attributes, accessToken, refreshToken, role);
        }
    }

    private void handleGoogleLogin(
            HttpServletResponse response,
            Map<String, Object> attributes,
            String accessToken,
            String refreshToken,
            String role)
            throws IOException {
        if (attributes != null && attributes.containsKey("email")) {
            // 응답 헤더에 JWT 토큰 추가
            response.addHeader("Authorization", "Bearer " + accessToken);
            response.addHeader("Refresh-Token", refreshToken);
            // 로그인 성공 후 처리할 로직 작성
            if (role != null && !role.isEmpty()) {
                response.sendRedirect(frontBaseUrl + "/");
            } else {
                response.sendRedirect(
                        frontBaseUrl
                                + "/role?accessToken="
                                + accessToken
                                + "&refreshToken="
                                + refreshToken);
            }
        } else {
            System.out.println("Google Login - FAIL");
            response.sendRedirect("/error");
        }
    }

    private void handleNaverLogin(
            HttpServletResponse response,
            Map<String, Object> attributes,
            String accessToken,
            String refreshToken,
            String role)
            throws IOException {
        Object responseObject = attributes.get("response");
        if (responseObject instanceof Map<?, ?> responseMap) {
            if (responseMap.containsKey("email")) {
                // 디버깅을 위한 로그 추가
                System.out.println("Naver Login - SUCCESS");

                // 응답 헤더에 JWT 토큰 추가
                response.addHeader("Authorization", "Bearer " + accessToken);
                response.addHeader("Refresh-Token", refreshToken);

                // 로그인 성공 후 처리할 로직 작성
                if (role != null && !role.isEmpty()) {
                    response.sendRedirect(frontBaseUrl + "/");
                } else {
                    response.sendRedirect(
                            frontBaseUrl
                                    + "/role?accessToken="
                                    + accessToken
                                    + "&refreshToken="
                                    + refreshToken);
                }
            } else {
                System.out.println("Naver Login - FAIL");
                response.sendRedirect("/error");
            }
        } else {
            response.sendRedirect("/error");
        }
    }
}
