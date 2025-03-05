package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.FormLidHistory;
import uz.dizgo.erp.payload.FormLidHistoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormLidHistoryMapper {
    @Mapping(source = "business.id", target = "businessId")
    FormLidHistoryDto toDto(FormLidHistory formLidHistory);

    List<FormLidHistoryDto> toDto(List<FormLidHistory> formLidHistories);
}
