package com.hecto.fitnessuniv.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hecto.fitnessuniv.entity.UserEntity;
import com.hecto.fitnessuniv.provider.JwtProvider;
import com.hecto.fitnessuniv.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// 필터로 등록하기 위해서 OncePerRequestFilter 를 받아와야함.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    // Repo 를 거쳐 User 를 꺼내오기
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            if (token != null) {
                // 검증
                String userId = jwtProvider.validate(token);
                // 밑 validate 에서 값이 없으면 null 을 리턴하기 때문에
                // 널이면 다음 필터 진행
                if (userId == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                // 이 다음 userRepository 를 거쳐서 User 정보를 꺼내올거임
                // findById를 유저 레포에 만들고오기
                // findByUserId 로 User 의 Id 가져온 후
                UserEntity userEntity = userRepository.findByUserId(userId);
                // User 의 role 권한 가져옴
                // userEntity 에 getUserRole()이 없는데?! 이거 spring 이 해주는거
                String role = userEntity.getUserRole(); // role = "ROLE_USER" or "ROLE_ADMIN"
                // 권한 설정 SimpleGrantedAuthority의 예시의 규칙이있음
                // ROLE_DEVELOPER 등등 ..
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));

                // context bean 으로 등록할 컨텍스트
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                // 컨텍스트 안에 담을 토큰을 만들어줘야함
                AbstractAuthenticationToken authenticationToken =
                        // userId와 비밀번호(여기서는 없게 해놔서 널), 인증정보
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                // request details 에 추가를 해줘야함
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                // 이제 context 에 토큰 값을 담아줌
                securityContext.setAuthentication(authenticationToken);
                // 만든 컨텍스트를 등록
                SecurityContextHolder.setContext(securityContext);
            } else { // authorization 이 없거나 아닐경우 다음 필터로 바로 넘어가라
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    //
    private String parseBearerToken(HttpServletRequest request) {
        // 1. header 에 있는 Authorization 가져옴
        String authorization = request.getHeader("Authorization");
        // 가져온 authorization 이 널이아니고 즉 존재하고
        // Bearer 로 시작하면 authorization 에서 bearer 문자열 짜르고 값 리턴
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        } else {
            return null;
        }
    }
}
