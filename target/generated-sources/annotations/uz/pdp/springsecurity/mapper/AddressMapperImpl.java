package uz.pdp.springsecurity.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Address;
import uz.pdp.springsecurity.payload.AddressDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-22T04:38:24+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address toEntity(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setCity( addressDto.getCity() );
        address.setDistrict( addressDto.getDistrict() );
        address.setStreet( addressDto.getStreet() );
        address.setHome( addressDto.getHome() );

        return address;
    }
}
