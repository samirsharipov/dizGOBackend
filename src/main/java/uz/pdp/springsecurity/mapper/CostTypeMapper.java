package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.CostType;
import uz.pdp.springsecurity.payload.CostTypeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CostTypeMapper {
    @Mapping(target = "branchId", source = "branch.id")
    CostTypeDto toDto(CostType costType);

    List<CostTypeDto> toDtoList(List<CostType> costTypeList);
}
