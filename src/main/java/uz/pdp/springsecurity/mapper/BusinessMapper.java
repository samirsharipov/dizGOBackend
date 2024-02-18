package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.payload.BusinessGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessMapper {


    BusinessGetDto toDto(Business business);


    List<BusinessGetDto> toDtoList(List<Business> business);
}
