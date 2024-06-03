package com.hecto.fitnessuniv.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    @Value("${secret-key}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String userId, String userName) {
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        // Key 설정
        // Key 는 java 의 key Keys 는 jjwt 의 Keys 임호화해서 Java key 에 저장
        Key key = getSigningKey();
        // jwt 만들고 반환.
        return Jwts.builder()
                // JWT 생성
                .claim("userId", userId)
                .claim("username", userName)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // jwt 검증 메서드
    public boolean validate(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt);
            System.out.println("JWT validation 성공 TRUE");
            return true;
        } catch (Exception e) {
            System.out.println("JWT validation 실패 Provider:50번째줄");
            e.printStackTrace();
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 id추출
    public String getUserIdFromToken(String jwt) {
        return getAllClaimsFromToken(jwt).get("userId", String.class);
    }

    // 토큰에서 username추출
    public String getUsernameFromToken(String jwt) {
        return getAllClaimsFromToken(jwt).get("username", String.class);
    }
}
