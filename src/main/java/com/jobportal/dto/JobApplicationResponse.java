package com.jobportal.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponse {

    private Long applicationId;
    private Long jobId;

    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private String candidateLocation;
    private Double candidateExperience;

    private LocalDateTime appliedAt;
}
