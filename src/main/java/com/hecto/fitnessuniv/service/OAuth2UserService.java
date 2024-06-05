package com.hecto.fitnessuniv.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    // 유저 반환
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthClientName = userRequest.getClientRegistration().getClientName();
        UserEntity userEntity = null;
        String userId = null;
        String userEmail = "";
        String userName = "";

        try {
            // 콘솔에 정보 찍는거 값들어오는거 테스트
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            log.info("Error while loading user: {}", e.getMessage());
        }

        if (oauthClientName.equals("naver")) {
            Object response = oAuth2User.getAttributes().get("response");
            if (response instanceof Map<?, ?> responseMap) {
                userId =
                        ((String) responseMap.get("id"))
                                .substring(0, ((String) responseMap.get("id")).length() - 1);
                userEmail = (String) responseMap.get("email");
                userName = (String) responseMap.get("name");
                userEntity = new UserEntity(userId, userName, userEmail, "naver", "ROLE_USER");
            }
        } else if (oauthClientName.equals("google")) {
            userId = oAuth2User.getAttribute("sub");
            userEmail = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("name");
            userEntity = new UserEntity(userId, userName, userEmail, "google", "ROLE_USER");
        }

        if (userEntity != null) {
            // 리프레시 토큰 생성 및 저장
            String refreshToken = jwtProvider.createRefreshToken(userId);
            userEntity.setRefreshToken(refreshToken);
            userRepository.save(userEntity);
        }
        userRepository.save(userEntity);

        Map<String, String> stringAttributes = new HashMap<>();
        stringAttributes.put("id", userId);
        stringAttributes.put("name", userName);
        stringAttributes.put("email", userEmail);

        return new CustomOAuth2User(oAuth2User, stringAttributes);
    }
}
