package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.LidStatus;
import uz.pdp.springsecurity.payload.LidStatusDto;
import uz.pdp.springsecurity.payload.LidStatusPostDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-09T10:56:15+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class LidStatusMapperImpl implements LidStatusMapper {

    @Override
    public LidStatusDto toDto(LidStatus lidStatus) {
        if ( lidStatus == null ) {
            return null;
        }

        LidStatusDto lidStatusDto = new LidStatusDto();

        lidStatusDto.setBusinessId( lidStatusBusinessId( lidStatus ) );
        lidStatusDto.setOrginalName( lidStatus.getOrginalName() );
        lidStatusDto.setId( lidStatus.getId() );
        lidStatusDto.setName( lidStatus.getName() );
        lidStatusDto.setColor( lidStatus.getColor() );
        lidStatusDto.setSort( lidStatus.getSort() );
        lidStatusDto.setIncrease( lidStatus.isIncrease() );

        return lidStatusDto;
    }

    @Override
    public List<LidStatusDto> toDto(List<LidStatus> lidStatusList) {
        if ( lidStatusList == null ) {
            return null;
        }

        List<LidStatusDto> list = new ArrayList<LidStatusDto>( lidStatusList.size() );
        for ( LidStatus lidStatus : lidStatusList ) {
            list.add( toDto( lidStatus ) );
        }

        return list;
    }

    @Override
    public LidStatus toEntity(LidStatusPostDto lidStatusPostDto) {
        if ( lidStatusPostDto == null ) {
            return null;
        }

        LidStatus lidStatus = new LidStatus();

        lidStatus.setBusiness( lidStatusPostDtoToBusiness( lidStatusPostDto ) );
        lidStatus.setName( lidStatusPostDto.getName() );
        lidStatus.setColor( lidStatusPostDto.getColor() );
        lidStatus.setSort( lidStatusPostDto.getSort() );
        lidStatus.setIncrease( lidStatusPostDto.isIncrease() );
        lidStatus.setSaleStatus( lidStatusPostDto.isSaleStatus() );

        return lidStatus;
    }

    @Override
    public void update(LidStatusPostDto lidStatusPostDto, LidStatus lidStatus) {
        if ( lidStatusPostDto == null ) {
            return;
        }

        if ( lidStatus.getBusiness() == null ) {
            lidStatus.setBusiness( new Business() );
        }
        lidStatusPostDtoToBusiness1( lidStatusPostDto, lidStatus.getBusiness() );
        lidStatus.setName( lidStatusPostDto.getName() );
        lidStatus.setColor( lidStatusPostDto.getColor() );
        lidStatus.setSort( lidStatusPostDto.getSort() );
        lidStatus.setIncrease( lidStatusPostDto.isIncrease() );
    }

    private UUID lidStatusBusinessId(LidStatus lidStatus) {
        if ( lidStatus == null ) {
            return null;
        }
        Business business = lidStatus.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Business lidStatusPostDtoToBusiness(LidStatusPostDto lidStatusPostDto) {
        if ( lidStatusPostDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( lidStatusPostDto.getBusinessId() );

        return business;
    }

    protected void lidStatusPostDtoToBusiness1(LidStatusPostDto lidStatusPostDto, Business mappingTarget) {
        if ( lidStatusPostDto == null ) {
            return;
        }

        mappingTarget.setId( lidStatusPostDto.getBusinessId() );
    }
}
