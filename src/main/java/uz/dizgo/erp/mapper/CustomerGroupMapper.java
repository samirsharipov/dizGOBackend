package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.CustomerGroup;
import uz.dizgo.erp.payload.CustomerGroupDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerGroupMapper {

    @Mapping(target = "businessId", source = "business.id")
    CustomerGroupDto toDto(CustomerGroup customerGroup);

    List<CustomerGroupDto> toDtoList(List<CustomerGroup> customerGroups);

    @Mapping(target = "business", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    CustomerGroup toEntity(CustomerGroupDto customerGroupDto);
}
