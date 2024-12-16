package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Shablon;
import uz.pdp.springsecurity.payload.ShablonDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-16T10:44:09+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ShablonMapperImpl implements ShablonMapper {

    @Override
    public Shablon toEntity(ShablonDto shablonDto) {
        if ( shablonDto == null ) {
            return null;
        }

        Shablon shablon = new Shablon();

        shablon.setBusiness( shablonDtoToBusiness( shablonDto ) );
        shablon.setId( shablonDto.getId() );
        shablon.setName( shablonDto.getName() );
        shablon.setMessage( shablonDto.getMessage() );

        return shablon;
    }

    @Override
    public ShablonDto toDto(Shablon shablon) {
        if ( shablon == null ) {
            return null;
        }

        ShablonDto shablonDto = new ShablonDto();

        shablonDto.setBusinessId( shablonBusinessId( shablon ) );
        shablonDto.setId( shablon.getId() );
        shablonDto.setName( shablon.getName() );
        shablonDto.setMessage( shablon.getMessage() );
        shablonDto.setOriginalName( shablon.getOriginalName() );

        return shablonDto;
    }

    @Override
    public List<ShablonDto> toDto(List<Shablon> shablonList) {
        if ( shablonList == null ) {
            return null;
        }

        List<ShablonDto> list = new ArrayList<ShablonDto>( shablonList.size() );
        for ( Shablon shablon : shablonList ) {
            list.add( toDto( shablon ) );
        }

        return list;
    }

    @Override
    public void update(ShablonDto shablonDto, Shablon shablon) {
        if ( shablonDto == null ) {
            return;
        }

        shablon.setId( shablonDto.getId() );
        shablon.setName( shablonDto.getName() );
        shablon.setMessage( shablonDto.getMessage() );
    }

    protected Business shablonDtoToBusiness(ShablonDto shablonDto) {
        if ( shablonDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( shablonDto.getBusinessId() );

        return business;
    }

    private UUID shablonBusinessId(Shablon shablon) {
        if ( shablon == null ) {
            return null;
        }
        Business business = shablon.getBusiness();
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
