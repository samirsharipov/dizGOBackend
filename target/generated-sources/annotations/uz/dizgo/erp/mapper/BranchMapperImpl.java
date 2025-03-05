package uz.dizgo.erp.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Address;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.payload.BranchDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T09:58:03+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
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
        branch.setAddressName( branchDto.getAddressName() );
        branch.setMainBranchId( branchDto.getMainBranchId() );

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
