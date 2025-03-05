package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.Job;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.mapper.JobMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.JobDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.JobRepository;
import uz.dizgo.erp.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository repository;
    private final JobMapper mapper;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public ApiResponse getAll(UUID businessId) {
        List<Job> all = repository.findAllByBusinessId(businessId);
        if (all.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("all jobs", true, mapper.toDto(all));
    }

    public ApiResponse getById(UUID id) {
        Job job = repository.findById(id).orElse(null);
        if (job == null) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("found", true, mapper.toDto(job));
    }

    public ApiResponse create(JobDto jobDto) {
        if (jobDto.getBusinessId() != null) {
            Optional<Business> optionalBusiness = businessRepository.findById(jobDto.getBusinessId());
            if (optionalBusiness.isEmpty()) {
                return new ApiResponse("not found business", false);
            }
            Job job = mapper.toEntity(jobDto);
            job.setBusiness(optionalBusiness.get());

            repository.save(job);
            return new ApiResponse("successfully saved", true);
        }
        return new ApiResponse("business id is null", false);
    }

    public ApiResponse edit(UUID id, JobDto jobDto) {

        if (jobDto.getBusinessId() == null) {
            return new ApiResponse("businessId null");
        }

        Optional<Business> optionalBusiness =
                businessRepository.findById(jobDto.getBusinessId());

        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found business", false);
        }

        Job job = repository.findById(id).orElse(null);

        if (job == null) {
            return new ApiResponse("not found", false);
        }
        job.setBusiness(optionalBusiness.get());

        mapper.update(jobDto, job);
        repository.save(job);

        return new ApiResponse("successfully update", true);
    }

    public ApiResponse delete(UUID id) {
        Job job = repository.findById(id).orElse(null);
        if (job == null) {
            return new ApiResponse("not found", false);
        }
        List<User> userList = userRepository.findAllByJobId(id);
        if (userList.isEmpty()) {
            repository.delete(job);
            return new ApiResponse("successfully deleted", true);
        }
        return new ApiResponse("cannot be deleted", false);
    }
}
