package uz.pdp.springsecurity.mapper;

import org.mapstruct.*;
import uz.pdp.springsecurity.entity.Address;
import uz.pdp.springsecurity.payload.AddressDto;
import uz.pdp.springsecurity.payload.AddressGetDto;

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