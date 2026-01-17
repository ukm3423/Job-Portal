package com.jobportal.mapper;

import com.jobportal.dto.JobApplicationResponse;
import com.jobportal.model.JobApplication;
import com.jobportal.auth.models.User;

public final class JobApplicationMapper {

    private JobApplicationMapper() {}

    public static JobApplicationResponse toResponse(JobApplication application) {

        User candidate = application.getUser();

        return new JobApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                candidate.getId(),
                candidate.getFullname(),
                candidate.getEmail(),
                candidate.getLocation(),
                candidate.getExperience(),
                application.getAppliedAt()
        );
    }
}
