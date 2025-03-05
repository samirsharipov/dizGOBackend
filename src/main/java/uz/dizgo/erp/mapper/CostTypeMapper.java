package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.CostType;
import uz.dizgo.erp.payload.CostTypeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CostTypeMapper {
    @Mapping(target = "branchId", source = "branch.id")
    CostTypeDto toDto(CostType costType);

    List<CostTypeDto> toDtoList(List<CostType> costTypeList);
}
