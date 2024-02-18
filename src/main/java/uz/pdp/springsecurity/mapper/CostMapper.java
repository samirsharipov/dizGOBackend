package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Cost;
import uz.pdp.springsecurity.payload.CostGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CostMapper {
    @Mapping(target = "costTypeId", source = "costType.id")
    @Mapping(target = "costTypeName", source = "costType.name")
    CostGetDto toDto(Cost cost);
    List<CostGetDto> toDtoList(List<Cost> costList);
}
