package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Lid;
import uz.dizgo.erp.payload.LidDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LidMapper {
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    @Mapping(target = "lidStatus", ignore = true)
    @Mapping(target = "lidStatus.id", source = "lidStatusId")
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "values", ignore = true)
    @Mapping(target = "source", ignore = true)
    Lid toEntity(LidDto lidDto);

    @Mapping(target = "lidStatusId", source = "lidStatus.id")
    @Mapping(target = "businessId", source = "business.id")
    @Mapping(target = "values", ignore = true)
    @Mapping(target = "formId", ignore = true)
    LidDto toDto(Lid lid);

    List<LidDto> toDto(List<Lid> lidList);

}
