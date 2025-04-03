package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.payload.BusinessEditDto;
import uz.dizgo.erp.payload.BusinessGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    BusinessEditDto toGetOneDto(Business business);


    List<BusinessGetDto> toDtoList(List<Business> business);
}
