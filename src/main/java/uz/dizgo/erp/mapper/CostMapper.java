package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Cost;
import uz.dizgo.erp.payload.CostGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CostMapper {
    @Mapping(target = "costTypeId", source = "costType.id")
    @Mapping(target = "costTypeName", source = "costType.name")
    CostGetDto toDto(Cost cost);
    List<CostGetDto> toDtoList(List<Cost> costList);
}
