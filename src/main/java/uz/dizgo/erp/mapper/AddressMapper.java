package uz.dizgo.erp.mapper;

import org.mapstruct.*;
import uz.dizgo.erp.entity.Address;
import uz.dizgo.erp.payload.AddressDto;
import uz.dizgo.erp.payload.AddressGetDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "parentId", source = "parentAddress.id")
    @Mapping(target = "parentName", source = "parentAddress.name")
    AddressGetDto toDto(Address address);

    List<AddressGetDto> toDtoList(List<Address> addresses);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AddressDto addressDto, @MappingTarget Address address);
}