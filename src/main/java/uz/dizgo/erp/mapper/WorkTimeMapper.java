package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import uz.dizgo.erp.entity.WorkTime;
import uz.dizgo.erp.payload.WorkTimeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkTimeMapper {
    WorkTimeDto toDto(WorkTime workTime);

    List<WorkTimeDto> toDtoList(List<WorkTime> workTimeList);
}
