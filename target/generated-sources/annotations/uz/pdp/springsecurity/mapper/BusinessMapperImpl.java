package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.payload.BusinessGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-02T16:18:49+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class BusinessMapperImpl implements BusinessMapper {

    @Override
    public BusinessGetDto toDto(Business business) {
        if ( business == null ) {
            return null;
        }

        BusinessGetDto businessGetDto = new BusinessGetDto();

        businessGetDto.setId( business.getId() );
        businessGetDto.setName( business.getName() );
        businessGetDto.setDescription( business.getDescription() );
        businessGetDto.setActive( business.isActive() );
        businessGetDto.setDelete( business.isDelete() );

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
