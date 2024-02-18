package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.LidStatus;
import uz.pdp.springsecurity.payload.LidStatusDto;
import uz.pdp.springsecurity.payload.LidStatusPostDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LidStatusMapper {
    @Mapping(target = "numberOfLids", ignore = true)
    @Mapping(target = "businessId", source = "business.id")
    @Mapping(target = "orginalName", source = "orginalName")
    @Mapping(target = "id", source = "id")
    LidStatusDto toDto(LidStatus lidStatus);

    List<LidStatusDto> toDto(List<LidStatus> lidStatusList);

    @Mapping(target = "business", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    LidStatus toEntity(LidStatusPostDto lidStatusPostDto);

    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "saleStatus", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    void update(LidStatusPostDto lidStatusPostDto, @MappingTarget LidStatus lidStatus);


}
