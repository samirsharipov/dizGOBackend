package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.WorkTime;
import uz.pdp.springsecurity.payload.WorkTimeDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T14:21:47+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class WorkTimeMapperImpl implements WorkTimeMapper {

    @Override
    public WorkTimeDto toDto(WorkTime workTime) {
        if ( workTime == null ) {
            return null;
        }

        WorkTimeDto workTimeDto = new WorkTimeDto();

        workTimeDto.setArrivalTime( workTime.getArrivalTime() );
        workTimeDto.setLeaveTime( workTime.getLeaveTime() );
        workTimeDto.setMinute( workTime.getMinute() );
        workTimeDto.setActive( workTime.isActive() );

        return workTimeDto;
    }

    @Override
    public List<WorkTimeDto> toDtoList(List<WorkTime> workTimeList) {
        if ( workTimeList == null ) {
            return null;
        }

        List<WorkTimeDto> list = new ArrayList<WorkTimeDto>( workTimeList.size() );
        for ( WorkTime workTime : workTimeList ) {
            list.add( toDto( workTime ) );
        }

        return list;
    }
}
