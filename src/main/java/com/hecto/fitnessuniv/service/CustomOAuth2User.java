package com.hecto.fitnessuniv.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomOAuth2User implements OAuth2User {
    private OAuth2User oAuth2User;
    @Getter private Map<String, String> stringAttributes;
    private String accessToken;
    private String refreshToken;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User != null ? oAuth2User.getAttributes() : Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User != null ? oAuth2User.getAuthorities() : Collections.emptyList();
    }

    @Override
    public String getName() {
        return stringAttributes != null && stringAttributes.containsKey("name")
                ? stringAttributes.get("name")
                : oAuth2User != null ? oAuth2User.getAttribute("name") : null;
    }

    public String getEmail() {
        return stringAttributes != null && stringAttributes.containsKey("email")
                ? stringAttributes.get("email")
                : oAuth2User != null ? oAuth2User.getAttribute("email") : null;
    }

    public String getId() {
        return stringAttributes != null && stringAttributes.containsKey("id")
                ? stringAttributes.get("id")
                : oAuth2User != null ? oAuth2User.getAttribute("id") : null;
    }
}
