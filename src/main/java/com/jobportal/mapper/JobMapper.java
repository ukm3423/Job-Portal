package com.jobportal.mapper;

import com.jobportal.auth.models.User;
import com.jobportal.dto.JobCreateRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.Job;

public final class JobMapper {

    private JobMapper() {}

    public static JobResponse toResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getRequiredSkills(),
                job.getExperienceRequired(),
                job.getUser().getCompany(),
                job.getUser().getEmail()
        );
    }

    public static Job toEntity(
            JobCreateRequest request,
            User recruiter
    ) {

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setExperienceRequired(request.getExperienceRequired());
        job.setUser(recruiter);

        return job;
    }
}

