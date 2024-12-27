package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.FormLidHistory;
import uz.pdp.springsecurity.payload.FormLidHistoryDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-27T16:17:53+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class FormLidHistoryMapperImpl implements FormLidHistoryMapper {

    @Override
    public FormLidHistoryDto toDto(FormLidHistory formLidHistory) {
        if ( formLidHistory == null ) {
            return null;
        }

        FormLidHistoryDto formLidHistoryDto = new FormLidHistoryDto();

        formLidHistoryDto.setBusinessId( formLidHistoryBusinessId( formLidHistory ) );
        formLidHistoryDto.setName( formLidHistory.getName() );
        formLidHistoryDto.setTotalSumma( formLidHistory.getTotalSumma() );
        formLidHistoryDto.setTotalLid( formLidHistory.getTotalLid() );
        formLidHistoryDto.setAverage( formLidHistory.getAverage() );
        formLidHistoryDto.setActive( formLidHistory.isActive() );

        return formLidHistoryDto;
    }

    @Override
    public List<FormLidHistoryDto> toDto(List<FormLidHistory> formLidHistories) {
        if ( formLidHistories == null ) {
            return null;
        }

        List<FormLidHistoryDto> list = new ArrayList<FormLidHistoryDto>( formLidHistories.size() );
        for ( FormLidHistory formLidHistory : formLidHistories ) {
            list.add( toDto( formLidHistory ) );
        }

        return list;
    }

    private UUID formLidHistoryBusinessId(FormLidHistory formLidHistory) {
        if ( formLidHistory == null ) {
            return null;
        }
        Business business = formLidHistory.getBusiness();
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
