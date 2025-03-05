package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Customer;
import uz.dizgo.erp.payload.CustomerDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "customerGroupId", source = "customerGroup.id")
    @Mapping(target = "customerGroupName", source = "customerGroup.name")
    @Mapping(target = "customerGroupPercent", source = "customerGroup.percent")
    @Mapping(target = "branches", ignore = true)
    CustomerDto toDto(Customer customer);

    List<CustomerDto> toDtoList(List<Customer> customers);
}
