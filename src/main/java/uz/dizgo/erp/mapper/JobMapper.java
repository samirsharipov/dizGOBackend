package uz.dizgo.erp.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.dizgo.erp.entity.Job;
import uz.dizgo.erp.payload.JobDto;

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
