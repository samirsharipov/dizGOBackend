package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.CustomerGroup;
import uz.pdp.springsecurity.payload.CustomerGroupDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-20T14:57:47+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class CustomerGroupMapperImpl implements CustomerGroupMapper {

    @Override
    public CustomerGroupDto toDto(CustomerGroup customerGroup) {
        if ( customerGroup == null ) {
            return null;
        }

        CustomerGroupDto customerGroupDto = new CustomerGroupDto();

        customerGroupDto.setBusinessId( customerGroupBusinessId( customerGroup ) );
        customerGroupDto.setId( customerGroup.getId() );
        customerGroupDto.setName( customerGroup.getName() );
        customerGroupDto.setPercent( customerGroup.getPercent() );

        return customerGroupDto;
    }

    @Override
    public List<CustomerGroupDto> toDtoList(List<CustomerGroup> customerGroups) {
        if ( customerGroups == null ) {
            return null;
        }

        List<CustomerGroupDto> list = new ArrayList<CustomerGroupDto>( customerGroups.size() );
        for ( CustomerGroup customerGroup : customerGroups ) {
            list.add( toDto( customerGroup ) );
        }

        return list;
    }

    @Override
    public CustomerGroup toEntity(CustomerGroupDto customerGroupDto) {
        if ( customerGroupDto == null ) {
            return null;
        }

        CustomerGroup customerGroup = new CustomerGroup();

        customerGroup.setBusiness( customerGroupDtoToBusiness( customerGroupDto ) );
        customerGroup.setId( customerGroupDto.getId() );
        customerGroup.setName( customerGroupDto.getName() );
        if ( customerGroupDto.getPercent() != null ) {
            customerGroup.setPercent( customerGroupDto.getPercent() );
        }

        return customerGroup;
    }

    private UUID customerGroupBusinessId(CustomerGroup customerGroup) {
        if ( customerGroup == null ) {
            return null;
        }
        Business business = customerGroup.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Business customerGroupDtoToBusiness(CustomerGroupDto customerGroupDto) {
        if ( customerGroupDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( customerGroupDto.getBusinessId() );

        return business;
    }
}
