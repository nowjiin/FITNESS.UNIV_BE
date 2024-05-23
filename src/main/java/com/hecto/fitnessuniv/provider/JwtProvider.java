package com.hecto.fitnessuniv.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    @Value("${secret-key}")
    private String secretKey;

    public String createToken(String userId) {
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        // Key 설정
        // Key 는 java 의 key Keys 는 jjwt 의 Keys 임호화해서 Java key 에 저장
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        // jwt 만들고 반환.
        return Jwts.builder()
                // JWT 생성
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .compact();
    }

    // jwt 검증 메서드
    public String validate(String jwt) {
        String subject;
        // Signature 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            subject =
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(jwt)
                            .getBody()
                            .getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return subject;
    }
}
