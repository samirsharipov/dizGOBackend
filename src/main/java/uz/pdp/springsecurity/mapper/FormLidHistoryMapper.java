package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.FormLidHistory;
import uz.pdp.springsecurity.payload.FormLidHistoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormLidHistoryMapper {
    @Mapping(source = "business.id", target = "businessId")
    FormLidHistoryDto toDto(FormLidHistory formLidHistory);

    List<FormLidHistoryDto> toDto(List<FormLidHistory> formLidHistories);
}
