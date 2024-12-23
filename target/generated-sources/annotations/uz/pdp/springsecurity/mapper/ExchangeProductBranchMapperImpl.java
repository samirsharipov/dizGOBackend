package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.ExchangeProductBranch;
import uz.pdp.springsecurity.entity.ExchangeStatus;
import uz.pdp.springsecurity.payload.ExchangeProductBranchDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-23T15:59:09+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ExchangeProductBranchMapperImpl implements ExchangeProductBranchMapper {

    @Override
    public ExchangeProductBranch toEntity(ExchangeProductBranchDTO exchangeProductBranchDTO) {
        if ( exchangeProductBranchDTO == null ) {
            return null;
        }

        ExchangeProductBranch exchangeProductBranch = new ExchangeProductBranch();

        exchangeProductBranch.setShippedBranch( exchangeProductBranchDTOToBranch( exchangeProductBranchDTO ) );
        exchangeProductBranch.setReceivedBranch( exchangeProductBranchDTOToBranch1( exchangeProductBranchDTO ) );
        exchangeProductBranch.setExchangeStatus( exchangeProductBranchDTOToExchangeStatus( exchangeProductBranchDTO ) );
        exchangeProductBranch.setBusiness( exchangeProductBranchDTOToBusiness( exchangeProductBranchDTO ) );
        exchangeProductBranch.setExchangeDate( exchangeProductBranchDTO.getExchangeDate() );
        exchangeProductBranch.setDescription( exchangeProductBranchDTO.getDescription() );

        return exchangeProductBranch;
    }

    @Override
    public ExchangeProductBranchDTO toDto(ExchangeProductBranch branch) {
        if ( branch == null ) {
            return null;
        }

        ExchangeProductBranchDTO exchangeProductBranchDTO = new ExchangeProductBranchDTO();

        exchangeProductBranchDTO.setShippedBranchId( branchShippedBranchId( branch ) );
        exchangeProductBranchDTO.setReceivedBranchId( branchReceivedBranchId( branch ) );
        exchangeProductBranchDTO.setExchangeStatusId( branchExchangeStatusId( branch ) );
        exchangeProductBranchDTO.setBusinessId( branchBusinessId( branch ) );
        exchangeProductBranchDTO.setExchangeDate( branch.getExchangeDate() );
        exchangeProductBranchDTO.setDescription( branch.getDescription() );

        return exchangeProductBranchDTO;
    }

    @Override
    public List<ExchangeProductBranchDTO> toDtoList(List<ExchangeProductBranch> branchList) {
        if ( branchList == null ) {
            return null;
        }

        List<ExchangeProductBranchDTO> list = new ArrayList<ExchangeProductBranchDTO>( branchList.size() );
        for ( ExchangeProductBranch exchangeProductBranch : branchList ) {
            list.add( toDto( exchangeProductBranch ) );
        }

        return list;
    }

    @Override
    public void update(ExchangeProductBranchDTO exchangeProductBranchDTO, ExchangeProductBranch exchangeProductBranch) {
        if ( exchangeProductBranchDTO == null ) {
            return;
        }

        if ( exchangeProductBranch.getShippedBranch() == null ) {
            exchangeProductBranch.setShippedBranch( new Branch() );
        }
        exchangeProductBranchDTOToBranch2( exchangeProductBranchDTO, exchangeProductBranch.getShippedBranch() );
        if ( exchangeProductBranch.getReceivedBranch() == null ) {
            exchangeProductBranch.setReceivedBranch( new Branch() );
        }
        exchangeProductBranchDTOToBranch3( exchangeProductBranchDTO, exchangeProductBranch.getReceivedBranch() );
        if ( exchangeProductBranch.getExchangeStatus() == null ) {
            exchangeProductBranch.setExchangeStatus( new ExchangeStatus() );
        }
        exchangeProductBranchDTOToExchangeStatus1( exchangeProductBranchDTO, exchangeProductBranch.getExchangeStatus() );
        if ( exchangeProductBranch.getBusiness() == null ) {
            exchangeProductBranch.setBusiness( new Business() );
        }
        exchangeProductBranchDTOToBusiness1( exchangeProductBranchDTO, exchangeProductBranch.getBusiness() );
        exchangeProductBranch.setExchangeDate( exchangeProductBranchDTO.getExchangeDate() );
        exchangeProductBranch.setDescription( exchangeProductBranchDTO.getDescription() );
    }

    protected Branch exchangeProductBranchDTOToBranch(ExchangeProductBranchDTO exchangeProductBranchDTO) {
        if ( exchangeProductBranchDTO == null ) {
            return null;
        }

        Branch branch = new Branch();

        branch.setId( exchangeProductBranchDTO.getShippedBranchId() );

        return branch;
    }

    protected Branch exchangeProductBranchDTOToBranch1(ExchangeProductBranchDTO exchangeProductBranchDTO) {
        if ( exchangeProductBranchDTO == null ) {
            return null;
        }

        Branch branch = new Branch();

        branch.setId( exchangeProductBranchDTO.getReceivedBranchId() );

        return branch;
    }

    protected ExchangeStatus exchangeProductBranchDTOToExchangeStatus(ExchangeProductBranchDTO exchangeProductBranchDTO) {
        if ( exchangeProductBranchDTO == null ) {
            return null;
        }

        ExchangeStatus exchangeStatus = new ExchangeStatus();

        exchangeStatus.setId( exchangeProductBranchDTO.getExchangeStatusId() );

        return exchangeStatus;
    }

    protected Business exchangeProductBranchDTOToBusiness(ExchangeProductBranchDTO exchangeProductBranchDTO) {
        if ( exchangeProductBranchDTO == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( exchangeProductBranchDTO.getBusinessId() );

        return business;
    }

    private UUID branchShippedBranchId(ExchangeProductBranch exchangeProductBranch) {
        if ( exchangeProductBranch == null ) {
            return null;
        }
        Branch shippedBranch = exchangeProductBranch.getShippedBranch();
        if ( shippedBranch == null ) {
            return null;
        }
        UUID id = shippedBranch.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID branchReceivedBranchId(ExchangeProductBranch exchangeProductBranch) {
        if ( exchangeProductBranch == null ) {
            return null;
        }
        Branch receivedBranch = exchangeProductBranch.getReceivedBranch();
        if ( receivedBranch == null ) {
            return null;
        }
        UUID id = receivedBranch.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID branchExchangeStatusId(ExchangeProductBranch exchangeProductBranch) {
        if ( exchangeProductBranch == null ) {
            return null;
        }
        ExchangeStatus exchangeStatus = exchangeProductBranch.getExchangeStatus();
        if ( exchangeStatus == null ) {
            return null;
        }
        UUID id = exchangeStatus.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID branchBusinessId(ExchangeProductBranch exchangeProductBranch) {
        if ( exchangeProductBranch == null ) {
            return null;
        }
        Business business = exchangeProductBranch.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected void exchangeProductBranchDTOToBranch2(ExchangeProductBranchDTO exchangeProductBranchDTO, Branch mappingTarget) {
        if ( exchangeProductBranchDTO == null ) {
            return;
        }

        mappingTarget.setId( exchangeProductBranchDTO.getShippedBranchId() );
    }

    protected void exchangeProductBranchDTOToBranch3(ExchangeProductBranchDTO exchangeProductBranchDTO, Branch mappingTarget) {
        if ( exchangeProductBranchDTO == null ) {
            return;
        }

        mappingTarget.setId( exchangeProductBranchDTO.getReceivedBranchId() );
    }

    protected void exchangeProductBranchDTOToExchangeStatus1(ExchangeProductBranchDTO exchangeProductBranchDTO, ExchangeStatus mappingTarget) {
        if ( exchangeProductBranchDTO == null ) {
            return;
        }

        mappingTarget.setId( exchangeProductBranchDTO.getExchangeStatusId() );
    }

    protected void exchangeProductBranchDTOToBusiness1(ExchangeProductBranchDTO exchangeProductBranchDTO, Business mappingTarget) {
        if ( exchangeProductBranchDTO == null ) {
            return;
        }

        mappingTarget.setId( exchangeProductBranchDTO.getBusinessId() );
    }
}
