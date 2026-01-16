package com.jobportal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfileResponse {

    private Long id;
    private String email;
    private String fullName;
    private Double experience;
    private String skills;
    private String location;
}