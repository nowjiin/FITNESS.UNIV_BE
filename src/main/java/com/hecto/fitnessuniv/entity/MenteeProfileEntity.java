package com.hecto.fitnessuniv.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "mentee_profile")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenteeProfileEntity {
    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @Column(name = "exercises", columnDefinition = "TEXT")
    private String exercises;

    @Column(name = "regions", columnDefinition = "TEXT")
    private String regions;

    private String gender;
    private String rate;
    private String age;
}
