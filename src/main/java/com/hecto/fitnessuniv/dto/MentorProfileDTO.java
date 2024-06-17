package com.hecto.fitnessuniv.dto;

import java.util.List;

import lombok.Data;

@Data
public class MentorProfileDTO {
    private List<String> exercises;
    private List<String> regions;
    private String rate;
    private String gender;
}
