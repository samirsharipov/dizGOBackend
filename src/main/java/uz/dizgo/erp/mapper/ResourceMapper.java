package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Resource;
import uz.dizgo.erp.payload.ResourceDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    @Mapping(source = "branchId",target = "branch.id")
    Resource toResource(ResourceDto resourceDto);

    @Mapping(source = "branch.id",target = "branchId")
    ResourceDto toResourceDto(Resource resource);

    List<ResourceDto> toResourceDtoList(List<Resource> resources);
}
