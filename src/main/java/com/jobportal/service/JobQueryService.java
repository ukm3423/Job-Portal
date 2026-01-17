package com.jobportal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jobportal.common.dto.ErrorCode;
import com.jobportal.common.exceptions.BusinessException;
import com.jobportal.dto.JobResponse;
import com.jobportal.mapper.JobMapper;
import com.jobportal.model.Job;
import com.jobportal.repository.JobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobQueryService {

    private final JobRepository jobRepo;

    public List<JobResponse> getAllJobs() {
        return jobRepo.findAll()
                .stream()
                .map(JobMapper::toResponse)
                .toList();
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.JOB_NOT_FOUND,
                                "Job not found"
                        ));

        return JobMapper.toResponse(job);
    }

    public List<JobResponse> searchJobs(String skill, String location) {
        return jobRepo
                .findByRequiredSkillsContainingIgnoreCaseAndLocationContainingIgnoreCase(
                        skill, location
                )
                .stream()
                .map(JobMapper::toResponse)
                .toList();
    }
}
