package com.jobportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {

    private Long id;
    
    private String name;

    private String email;

    private String password; // This will be stored as a BCrypt hash

    private Double experience; // Storing years of experience (e.g., 2.5)

    private String skills; // Stored as comma-separated values (e.g., "Java, Spring, Docker")

    private String location;
}
