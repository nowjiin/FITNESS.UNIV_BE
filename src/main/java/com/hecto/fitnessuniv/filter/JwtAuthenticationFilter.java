package com.hecto.fitnessuniv.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            // 토근이 널이 아니고 jwtProvider 에서 jwt 검증이 유효할 경우 실행
            if (token != null && jwtProvider.validate(token)) {
                // 토큰에서 userID 추출
                String userId = jwtProvider.getUserIdFromToken(token);
                // 밑 validate 에서 값이 없으면 null 을 리턴하기 때문에
                // 널이면 다음 필터 진행
                if (userId != null) {
                    // 이 다음 userRepository 를 거쳐서 User 정보를 꺼내올거임
                    // findById를 유저 레포에 만들고오기  findByUserId 로 User 의 Id 가져온 후 처리
                    Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userId);
                    if (userEntityOptional.isPresent()) {
                        UserEntity userEntity = userEntityOptional.get();
                        //                        String role = userEntity.getUserRole(); // 실제
                        // UserEntity 객체에서 getUserRole 호출

                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(userEntity.getUserRole()));

                        SecurityContext securityContext =
                                SecurityContextHolder.createEmptyContext();
                        AbstractAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userId, null, authorities);
                        authenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));
                        securityContext.setAuthentication(authenticationToken);
                        SecurityContextHolder.setContext(securityContext);
                    } else {
                        filterChain.doFilter(request, response);
                        return;
                    }
                } else {
                    filterChain.doFilter(request, response);
                    return;
                }
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
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
