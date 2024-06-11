package com.hecto.fitnessuniv.entity;

import java.util.Arrays;
import java.util.List;

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

    // Setters that convert List<String> to comma-separated String
    public void setExercises(List<String> exercises) {
        this.exercises = String.join(",", exercises);
    }

    public void setRegions(List<String> regions) {
        this.regions = String.join(",", regions);
    }

    // Getters that convert comma-separated String to List<String>
    public List<String> getExercises() {
        if (this.exercises == null || this.exercises.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(exercises.split(","));
    }

    public List<String> getRegions() {
        return Arrays.asList(regions.split(","));
    }
}
