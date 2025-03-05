package uz.dizgo.erp.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.Job;
import uz.dizgo.erp.payload.JobDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T09:58:03+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class JobMapperImpl implements JobMapper {

    @Override
    public Job toEntity(JobDto jobDto) {
        if ( jobDto == null ) {
            return null;
        }

        Job job = new Job();

        job.setName( jobDto.getName() );
        job.setDescription( jobDto.getDescription() );

        return job;
    }

    @Override
    public JobDto toDto(Job job) {
        if ( job == null ) {
            return null;
        }

        JobDto jobDto = new JobDto();

        jobDto.setBusinessId( jobBusinessId( job ) );
        jobDto.setId( job.getId() );
        jobDto.setName( job.getName() );
        jobDto.setDescription( job.getDescription() );

        return jobDto;
    }

    @Override
    public List<JobDto> toDto(List<Job> jobs) {
        if ( jobs == null ) {
            return null;
        }

        List<JobDto> list = new ArrayList<JobDto>( jobs.size() );
        for ( Job job : jobs ) {
            list.add( toDto( job ) );
        }

        return list;
    }

    @Override
    public void update(JobDto dto, Job job) {
        if ( dto == null ) {
            return;
        }

        job.setName( dto.getName() );
        job.setDescription( dto.getDescription() );
    }

    private UUID jobBusinessId(Job job) {
        if ( job == null ) {
            return null;
        }
        Business business = job.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
