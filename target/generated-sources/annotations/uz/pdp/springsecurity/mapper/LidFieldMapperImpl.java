package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.LidField;
import uz.pdp.springsecurity.enums.ValueType;
import uz.pdp.springsecurity.payload.LidFieldDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-23T15:59:09+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class LidFieldMapperImpl implements LidFieldMapper {

    @Override
    public LidFieldDto toDto(LidField lidField) {
        if ( lidField == null ) {
            return null;
        }

        LidFieldDto lidFieldDto = new LidFieldDto();

        lidFieldDto.setBusinessId( lidFieldBusinessId( lidField ) );
        lidFieldDto.setId( lidField.getId() );
        lidFieldDto.setName( lidField.getName() );
        if ( lidField.getValueType() != null ) {
            lidFieldDto.setValueType( lidField.getValueType().name() );
        }
        lidFieldDto.setTanlangan( lidField.getTanlangan() );

        return lidFieldDto;
    }

    @Override
    public List<LidFieldDto> toDto(List<LidField> lidFields) {
        if ( lidFields == null ) {
            return null;
        }

        List<LidFieldDto> list = new ArrayList<LidFieldDto>( lidFields.size() );
        for ( LidField lidField : lidFields ) {
            list.add( toDto( lidField ) );
        }

        return list;
    }

    @Override
    public LidField toEntity(LidFieldDto lidFieldDto) {
        if ( lidFieldDto == null ) {
            return null;
        }

        LidField lidField = new LidField();

        lidField.setBusiness( lidFieldDtoToBusiness( lidFieldDto ) );
        lidField.setName( lidFieldDto.getName() );
        lidField.setTanlangan( lidFieldDto.getTanlangan() );
        if ( lidFieldDto.getValueType() != null ) {
            lidField.setValueType( Enum.valueOf( ValueType.class, lidFieldDto.getValueType() ) );
        }

        return lidField;
    }

    @Override
    public List<LidField> toEntity(List<LidFieldDto> lidFieldDtoList) {
        if ( lidFieldDtoList == null ) {
            return null;
        }

        List<LidField> list = new ArrayList<LidField>( lidFieldDtoList.size() );
        for ( LidFieldDto lidFieldDto : lidFieldDtoList ) {
            list.add( toEntity( lidFieldDto ) );
        }

        return list;
    }

    @Override
    public void update(LidFieldDto lidFieldDto, LidField lidField) {
        if ( lidFieldDto == null ) {
            return;
        }

        if ( lidField.getBusiness() == null ) {
            lidField.setBusiness( new Business() );
        }
        lidFieldDtoToBusiness1( lidFieldDto, lidField.getBusiness() );
        lidField.setName( lidFieldDto.getName() );
        lidField.setTanlangan( lidFieldDto.getTanlangan() );
        if ( lidFieldDto.getValueType() != null ) {
            lidField.setValueType( Enum.valueOf( ValueType.class, lidFieldDto.getValueType() ) );
        }
        else {
            lidField.setValueType( null );
        }
    }

    private UUID lidFieldBusinessId(LidField lidField) {
        if ( lidField == null ) {
            return null;
        }
        Business business = lidField.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Business lidFieldDtoToBusiness(LidFieldDto lidFieldDto) {
        if ( lidFieldDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( lidFieldDto.getBusinessId() );

        return business;
    }

    protected void lidFieldDtoToBusiness1(LidFieldDto lidFieldDto, Business mappingTarget) {
        if ( lidFieldDto == null ) {
            return;
        }

        mappingTarget.setId( lidFieldDto.getBusinessId() );
    }
}
