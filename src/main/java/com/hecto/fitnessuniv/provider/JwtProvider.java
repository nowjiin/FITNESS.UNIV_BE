package com.hecto.fitnessuniv.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {
    @Value("${secret-key}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String userId, String userName) {
        Date expireTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        Key key = getSigningKey();
        return Jwts.builder()
                .setId(userId)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String userId) {
        Date expireTime = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));
        Key key = getSigningKey();
        return Jwts.builder()
                .setId(userId)
                .setIssuedAt(new Date())
                .setExpiration(expireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String refreshAccessToken(String refreshToken) {
        Claims claims = getAllClaimsFromToken(refreshToken);
        String userId = claims.getId();
        String userName = claims.getSubject();
        log.info("AccessToken 재발급 for user: {}", userId);
        return createAccessToken(userId, userName);
    }

    // jwt 검증 메서드
    public boolean validate(String jwt) {
        // Signature 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            log.info("JWT validation 성공");
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return false;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return false;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            return false;
        } catch (Exception e) {
            log.info("기타 오류 발생");
            return false;
        }
    }

    // Claims에서 모든 정보 추출
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // userid받아오기
    public String getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).getId();
    }

    public String getUserNameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
}
