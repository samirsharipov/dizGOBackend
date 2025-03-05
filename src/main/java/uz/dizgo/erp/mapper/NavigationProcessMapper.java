package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import uz.dizgo.erp.entity.NavigationProcess;
import uz.dizgo.erp.payload.NavigationProcessDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NavigationProcessMapper {
    NavigationProcessDto toDto(NavigationProcess navigationProcess);
    List<NavigationProcessDto> toDtoList(List<NavigationProcess> navigationProcessList);
}
