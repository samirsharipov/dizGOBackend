package uz.pdp.springsecurity.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Address;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.payload.BranchDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-22T04:38:24+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class BranchMapperImpl implements BranchMapper {

    @Override
    public Branch toEntity(BranchDto branchDto) {
        if ( branchDto == null ) {
            return null;
        }

        Branch branch = new Branch();

        branch.setBusiness( branchDtoToBusiness( branchDto ) );
        branch.setAddress( branchDtoToAddress( branchDto ) );
        branch.setName( branchDto.getName() );

        return branch;
    }

    protected Business branchDtoToBusiness(BranchDto branchDto) {
        if ( branchDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( branchDto.getBusinessId() );

        return business;
    }

    protected Address branchDtoToAddress(BranchDto branchDto) {
        if ( branchDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setId( branchDto.getAddressId() );

        return address;
    }
}
