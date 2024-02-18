package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import uz.pdp.springsecurity.entity.WorkTime;
import uz.pdp.springsecurity.payload.WorkTimeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkTimeMapper {
    WorkTimeDto toDto(WorkTime workTime);

    List<WorkTimeDto> toDtoList(List<WorkTime> workTimeList);
}
