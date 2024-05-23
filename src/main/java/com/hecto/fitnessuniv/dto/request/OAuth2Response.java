package com.hecto.fitnessuniv.dto.request;

public interface OAuth2Response {
    // 구글 네이버에서 발급해주는 아이디
    String getProvider();

    String getName();

    String getEmail();

    // 로그인 타입 구글, 네이버
    String getLoginType();
}
