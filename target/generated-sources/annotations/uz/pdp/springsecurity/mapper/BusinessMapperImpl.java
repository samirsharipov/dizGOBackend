package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.payload.BusinessEditDto;
import uz.pdp.springsecurity.payload.BusinessGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-16T10:44:09+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class BusinessMapperImpl implements BusinessMapper {

    @Override
    public BusinessEditDto toGetOneDto(Business business) {
        if ( business == null ) {
            return null;
        }

        BusinessEditDto businessEditDto = new BusinessEditDto();

        businessEditDto.setId( business.getId() );
        businessEditDto.setName( business.getName() );
        businessEditDto.setDescription( business.getDescription() );
        businessEditDto.setActive( business.isActive() );
        businessEditDto.setDeleted( business.isDeleted() );
        businessEditDto.setBusinessNumber( business.getBusinessNumber() );
        businessEditDto.setStatus( business.getStatus() );

        return businessEditDto;
    }

    @Override
    public List<BusinessEditDto> toGetDtoList(List<Business> businesses) {
        if ( businesses == null ) {
            return null;
        }

        List<BusinessEditDto> list = new ArrayList<BusinessEditDto>( businesses.size() );
        for ( Business business : businesses ) {
            list.add( toGetOneDto( business ) );
        }

        return list;
    }

    @Override
    public BusinessGetDto toDto(Business business) {
        if ( business == null ) {
            return null;
        }

        BusinessGetDto businessGetDto = new BusinessGetDto();

        businessGetDto.setCreatedAt( business.getCreatedAt() );
        businessGetDto.setUpdateAt( business.getUpdateAt() );
        businessGetDto.setId( business.getId() );
        businessGetDto.setName( business.getName() );
        businessGetDto.setDescription( business.getDescription() );
        businessGetDto.setActive( business.isActive() );
        businessGetDto.setDeleted( business.isDeleted() );

        return businessGetDto;
    }

    @Override
    public List<BusinessGetDto> toDtoList(List<Business> business) {
        if ( business == null ) {
            return null;
        }

        List<BusinessGetDto> list = new ArrayList<BusinessGetDto>( business.size() );
        for ( Business business1 : business ) {
            list.add( toDto( business1 ) );
        }

        return list;
    }
}
