package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.payload.BusinessEditDto;
import uz.pdp.springsecurity.payload.BusinessGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    BusinessEditDto toGetOneDto(Business business);

    List<BusinessEditDto> toGetDtoList(List<Business> businesses);

    BusinessGetDto toDto(Business business);

    List<BusinessGetDto> toDtoList(List<Business> business);
}
