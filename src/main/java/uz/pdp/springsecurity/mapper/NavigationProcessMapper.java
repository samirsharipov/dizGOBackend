package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import uz.pdp.springsecurity.entity.NavigationProcess;
import uz.pdp.springsecurity.payload.NavigationProcessDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NavigationProcessMapper {
    NavigationProcessDto toDto(NavigationProcess navigationProcess);
    List<NavigationProcessDto> toDtoList(List<NavigationProcess> navigationProcessList);
}
