package com.hecto.fitnessuniv.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user") // jakarta.persistence
// DB의 어떤 테이블과 매핑 시킬건지
// 만약 클래스 이름이 UserEntity가 아니고 User면 Table 어노테이션 필요 없음
@Table(name = "user")
// 날짜 자동 삽입 어노테이션
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_login_type")
    private String userLoginType;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "created_at", updatable = false)
    @CreatedDate // 시간 자동으로 넣어주는 @
    private LocalDateTime createdAt;

    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    // 프론트에서 유저가 멘토인지 멘티인지 구분
    @Setter
    @Column(name = "role")
    private String role;

    public UserEntity(
            String userId,
            String userName,
            String userEmail,
            String userLoginType,
            String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userLoginType = userLoginType;
        this.userRole = userRole;
    }
}
