package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Lid;
import uz.pdp.springsecurity.entity.LidStatus;
import uz.pdp.springsecurity.payload.LidDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-02T16:18:49+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class LidMapperImpl implements LidMapper {

    @Override
    public Lid toEntity(LidDto lidDto) {
        if ( lidDto == null ) {
            return null;
        }

        Lid lid = new Lid();

        lid.setBusiness( lidDtoToBusiness( lidDto ) );
        lid.setLidStatus( lidDtoToLidStatus( lidDto ) );

        return lid;
    }

    @Override
    public LidDto toDto(Lid lid) {
        if ( lid == null ) {
            return null;
        }

        LidDto lidDto = new LidDto();

        lidDto.setLidStatusId( lidLidStatusId( lid ) );
        lidDto.setBusinessId( lidBusinessId( lid ) );
        lidDto.setId( lid.getId() );

        return lidDto;
    }

    @Override
    public List<LidDto> toDto(List<Lid> lidList) {
        if ( lidList == null ) {
            return null;
        }

        List<LidDto> list = new ArrayList<LidDto>( lidList.size() );
        for ( Lid lid : lidList ) {
            list.add( toDto( lid ) );
        }

        return list;
    }

    protected Business lidDtoToBusiness(LidDto lidDto) {
        if ( lidDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( lidDto.getBusinessId() );

        return business;
    }

    protected LidStatus lidDtoToLidStatus(LidDto lidDto) {
        if ( lidDto == null ) {
            return null;
        }

        LidStatus lidStatus = new LidStatus();

        lidStatus.setId( lidDto.getLidStatusId() );

        return lidStatus;
    }

    private UUID lidLidStatusId(Lid lid) {
        if ( lid == null ) {
            return null;
        }
        LidStatus lidStatus = lid.getLidStatus();
        if ( lidStatus == null ) {
            return null;
        }
        UUID id = lidStatus.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID lidBusinessId(Lid lid) {
        if ( lid == null ) {
            return null;
        }
        Business business = lid.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
