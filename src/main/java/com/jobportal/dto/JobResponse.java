package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String requiredSkills;
    private Double experienceRequired;

    private String company;
    private String recruiterEmail;
}
