package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.dizgo.erp.entity.LidStatus;
import uz.dizgo.erp.payload.LidStatusDto;
import uz.dizgo.erp.payload.LidStatusPostDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LidStatusMapper {
    @Mapping(target = "numberOfLids", ignore = true)
    @Mapping(target = "businessId", source = "business.id")
    @Mapping(target = "orginalName", source = "orginalName")
    @Mapping(target = "id", source = "id")
    LidStatusDto toDto(LidStatus lidStatus);

    List<LidStatusDto> toDto(List<LidStatus> lidStatusList);

    @Mapping(target = "orginalName", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    LidStatus toEntity(LidStatusPostDto lidStatusPostDto);

    @Mapping(target = "orginalName", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "saleStatus", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    void update(LidStatusPostDto lidStatusPostDto, @MappingTarget LidStatus lidStatus);


}
