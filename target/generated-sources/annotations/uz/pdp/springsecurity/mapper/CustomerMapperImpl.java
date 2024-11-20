package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.entity.CustomerGroup;
import uz.pdp.springsecurity.payload.CustomerDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-20T14:57:47+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDto toDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();

        customerDto.setCustomerGroupId( customerCustomerGroupId( customer ) );
        customerDto.setCustomerGroupName( customerCustomerGroupName( customer ) );
        customerDto.setCustomerGroupPercent( customerCustomerGroupPercent( customer ) );
        customerDto.setId( customer.getId() );
        customerDto.setName( customer.getName() );
        customerDto.setPhoneNumber( customer.getPhoneNumber() );
        customerDto.setTelegram( customer.getTelegram() );
        customerDto.setDescription( customer.getDescription() );
        customerDto.setDebt( customer.getDebt() );
        customerDto.setPayDate( customer.getPayDate() );
        customerDto.setBirthday( customer.getBirthday() );
        customerDto.setLatitude( customer.getLatitude() );
        customerDto.setLongitude( customer.getLongitude() );
        customerDto.setAddress( customer.getAddress() );
        customerDto.setLidCustomer( customer.getLidCustomer() );

        return customerDto;
    }

    @Override
    public List<CustomerDto> toDtoList(List<Customer> customers) {
        if ( customers == null ) {
            return null;
        }

        List<CustomerDto> list = new ArrayList<CustomerDto>( customers.size() );
        for ( Customer customer : customers ) {
            list.add( toDto( customer ) );
        }

        return list;
    }

    private UUID customerCustomerGroupId(Customer customer) {
        if ( customer == null ) {
            return null;
        }
        CustomerGroup customerGroup = customer.getCustomerGroup();
        if ( customerGroup == null ) {
            return null;
        }
        UUID id = customerGroup.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String customerCustomerGroupName(Customer customer) {
        if ( customer == null ) {
            return null;
        }
        CustomerGroup customerGroup = customer.getCustomerGroup();
        if ( customerGroup == null ) {
            return null;
        }
        String name = customerGroup.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private double customerCustomerGroupPercent(Customer customer) {
        if ( customer == null ) {
            return 0.0d;
        }
        CustomerGroup customerGroup = customer.getCustomerGroup();
        if ( customerGroup == null ) {
            return 0.0d;
        }
        double percent = customerGroup.getPercent();
        return percent;
    }
}
