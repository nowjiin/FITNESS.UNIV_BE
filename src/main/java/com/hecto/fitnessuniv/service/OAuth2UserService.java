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
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    // 유저 반환
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 테스트출력
        System.out.println(oAuth2User);

        // 레지스터 아이디 저장할 변수
        String oauthClientName = userRequest.getClientRegistration().getClientName();

        try {
            // 콘솔에 정보 찍는거 값들어오는거 테스트
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 값
        UserEntity userEntity = null;
        String userId = null;
        String userEmail = "";
        String userName = "";

        if (oauthClientName.equals("naver")) {
            Object response = oAuth2User.getAttributes().get("response");

            if (response instanceof Map<?, ?> responseMap) {
                userId =
                        "naver_"
                                + ((String) responseMap.get("id"))
                                        .substring(
                                                0, ((String) responseMap.get("id")).length() - 1);
                userEmail = (String) responseMap.get("email");
                userName = (String) responseMap.get("name");
                userEntity = new UserEntity(userId, userName, userEmail, "naver", "ROLE_USER");
            }
        } else if (oauthClientName.equals("google")) {
            userId = "google_" + oAuth2User.getAttribute("sub");
            userEmail = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("name");
            userEntity = new UserEntity(userId, userName, userEmail, "google", "ROLE_USER");
        }

        if (userEntity != null) {
            userRepository.save(userEntity);
        }

        Map<String, String> stringAttributes = new HashMap<>();
        stringAttributes.put("id", userId);
        stringAttributes.put("name", userName);
        stringAttributes.put("email", userEmail);

        return new CustomOAuth2User(oAuth2User, stringAttributes);
    }
}
