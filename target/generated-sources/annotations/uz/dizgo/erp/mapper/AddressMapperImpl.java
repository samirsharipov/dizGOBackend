package uz.dizgo.erp.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Address;
import uz.dizgo.erp.payload.AddressDto;
import uz.dizgo.erp.payload.AddressGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T09:58:03+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressGetDto toDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressGetDto addressGetDto = new AddressGetDto();

        addressGetDto.setParentId( addressParentAddressId( address ) );
        addressGetDto.setParentName( addressParentAddressName( address ) );
        addressGetDto.setId( address.getId() );
        addressGetDto.setName( address.getName() );

        return addressGetDto;
    }

    @Override
    public List<AddressGetDto> toDtoList(List<Address> addresses) {
        if ( addresses == null ) {
            return null;
        }

        List<AddressGetDto> list = new ArrayList<AddressGetDto>( addresses.size() );
        for ( Address address : addresses ) {
            list.add( toDto( address ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(AddressDto addressDto, Address address) {
        if ( addressDto == null ) {
            return;
        }

        if ( addressDto.getName() != null ) {
            address.setName( addressDto.getName() );
        }
    }

    private UUID addressParentAddressId(Address address) {
        if ( address == null ) {
            return null;
        }
        Address parentAddress = address.getParentAddress();
        if ( parentAddress == null ) {
            return null;
        }
        UUID id = parentAddress.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String addressParentAddressName(Address address) {
        if ( address == null ) {
            return null;
        }
        Address parentAddress = address.getParentAddress();
        if ( parentAddress == null ) {
            return null;
        }
        String name = parentAddress.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
