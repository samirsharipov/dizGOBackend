package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.History;
import uz.pdp.springsecurity.payload.HistoryGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "branchName", source = "branch.name")
    HistoryGetDto toDto(History history);
    List<HistoryGetDto> toDtoList(List<History> historyList);
}
