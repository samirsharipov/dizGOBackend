package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Source;
import uz.pdp.springsecurity.payload.SourceDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-09T15:35:04+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class SourceMapperImpl implements SourceMapper {

    @Override
    public Source toEntity(SourceDto sourceDto) {
        if ( sourceDto == null ) {
            return null;
        }

        Source source = new Source();

        source.setBusiness( sourceDtoToBusiness( sourceDto ) );
        source.setId( sourceDto.getId() );
        source.setName( sourceDto.getName() );
        source.setIcon( sourceDto.getIcon() );

        return source;
    }

    @Override
    public List<Source> toEntity(List<SourceDto> sourceDtoList) {
        if ( sourceDtoList == null ) {
            return null;
        }

        List<Source> list = new ArrayList<Source>( sourceDtoList.size() );
        for ( SourceDto sourceDto : sourceDtoList ) {
            list.add( toEntity( sourceDto ) );
        }

        return list;
    }

    @Override
    public SourceDto toDto(Source source) {
        if ( source == null ) {
            return null;
        }

        SourceDto sourceDto = new SourceDto();

        sourceDto.setBusinessId( sourceBusinessId( source ) );
        sourceDto.setId( source.getId() );
        sourceDto.setName( source.getName() );
        sourceDto.setIcon( source.getIcon() );

        return sourceDto;
    }

    @Override
    public List<SourceDto> toDto(List<Source> sourceList) {
        if ( sourceList == null ) {
            return null;
        }

        List<SourceDto> list = new ArrayList<SourceDto>( sourceList.size() );
        for ( Source source : sourceList ) {
            list.add( toDto( source ) );
        }

        return list;
    }

    @Override
    public void update(SourceDto sourceDto, Source source) {
        if ( sourceDto == null ) {
            return;
        }

        if ( source.getBusiness() == null ) {
            source.setBusiness( new Business() );
        }
        sourceDtoToBusiness1( sourceDto, source.getBusiness() );
        source.setName( sourceDto.getName() );
        source.setIcon( sourceDto.getIcon() );
    }

    protected Business sourceDtoToBusiness(SourceDto sourceDto) {
        if ( sourceDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( sourceDto.getBusinessId() );

        return business;
    }

    private UUID sourceBusinessId(Source source) {
        if ( source == null ) {
            return null;
        }
        Business business = source.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected void sourceDtoToBusiness1(SourceDto sourceDto, Business mappingTarget) {
        if ( sourceDto == null ) {
            return;
        }

        mappingTarget.setId( sourceDto.getBusinessId() );
    }
}
