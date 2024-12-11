package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.LidField;
import uz.pdp.springsecurity.entity.SelectForLid;
import uz.pdp.springsecurity.payload.SelectForLidDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T14:21:47+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class SelectForLidMapperImpl implements SelectForLidMapper {

    @Override
    public SelectForLid toEntity(SelectForLidDto selectForLidDto) {
        if ( selectForLidDto == null ) {
            return null;
        }

        SelectForLid selectForLid = new SelectForLid();

        selectForLid.setLidField( selectForLidDtoToLidField( selectForLidDto ) );
        selectForLid.setId( selectForLidDto.getId() );
        selectForLid.setName( selectForLidDto.getName() );

        return selectForLid;
    }

    @Override
    public List<SelectForLid> toEntity(List<SelectForLidDto> selectForLids) {
        if ( selectForLids == null ) {
            return null;
        }

        List<SelectForLid> list = new ArrayList<SelectForLid>( selectForLids.size() );
        for ( SelectForLidDto selectForLidDto : selectForLids ) {
            list.add( toEntity( selectForLidDto ) );
        }

        return list;
    }

    @Override
    public SelectForLidDto toDto(SelectForLid selectForLid) {
        if ( selectForLid == null ) {
            return null;
        }

        SelectForLidDto selectForLidDto = new SelectForLidDto();

        selectForLidDto.setLidFieldId( selectForLidLidFieldId( selectForLid ) );
        selectForLidDto.setId( selectForLid.getId() );
        selectForLidDto.setName( selectForLid.getName() );

        return selectForLidDto;
    }

    @Override
    public List<SelectForLidDto> toDto(List<SelectForLid> selectForLids) {
        if ( selectForLids == null ) {
            return null;
        }

        List<SelectForLidDto> list = new ArrayList<SelectForLidDto>( selectForLids.size() );
        for ( SelectForLid selectForLid : selectForLids ) {
            list.add( toDto( selectForLid ) );
        }

        return list;
    }

    @Override
    public void update(SelectForLidDto selectForLidDto, SelectForLid selectForLid) {
        if ( selectForLidDto == null ) {
            return;
        }

        if ( selectForLid.getLidField() == null ) {
            selectForLid.setLidField( new LidField() );
        }
        selectForLidDtoToLidField1( selectForLidDto, selectForLid.getLidField() );
        selectForLid.setId( selectForLidDto.getId() );
        selectForLid.setName( selectForLidDto.getName() );
    }

    protected LidField selectForLidDtoToLidField(SelectForLidDto selectForLidDto) {
        if ( selectForLidDto == null ) {
            return null;
        }

        LidField lidField = new LidField();

        lidField.setId( selectForLidDto.getLidFieldId() );

        return lidField;
    }

    private UUID selectForLidLidFieldId(SelectForLid selectForLid) {
        if ( selectForLid == null ) {
            return null;
        }
        LidField lidField = selectForLid.getLidField();
        if ( lidField == null ) {
            return null;
        }
        UUID id = lidField.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected void selectForLidDtoToLidField1(SelectForLidDto selectForLidDto, LidField mappingTarget) {
        if ( selectForLidDto == null ) {
            return;
        }

        mappingTarget.setId( selectForLidDto.getLidFieldId() );
    }
}
