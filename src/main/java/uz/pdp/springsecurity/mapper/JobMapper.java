package uz.pdp.springsecurity.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.Job;
import uz.pdp.springsecurity.payload.JobDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "id", ignore = true)
    Job toEntity(JobDto jobDto);

    @Mapping(source = "business.id", target = "businessId")
    JobDto toDto(Job job);

    List<JobDto> toDto(List<Job> jobs);

    @InheritInverseConfiguration
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "businessId", target = "business.id")
    @Mapping(target = "id", ignore = true)
    void update(JobDto dto, @MappingTarget Job job);
}
