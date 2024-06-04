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
                .setId(userId)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // jwt 검증 메서드
    public boolean validate(String jwt) {
        // Signature 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            System.out.println("JWT validation 성공 TRUE");
            return true;
        } catch (Exception e) {
            System.out.println("JWT validation 실패 Provider:50번째줄");
            e.printStackTrace();
            return false;
        }
    }

    // userid받아오기
    public String getUserIdFromToken(String token) {
        Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        return claims.getId();
    }
}
