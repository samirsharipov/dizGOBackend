package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.dizgo.erp.entity.Source;
import uz.dizgo.erp.payload.SourceDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SourceMapper {
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    Source toEntity(SourceDto sourceDto);

    List<Source> toEntity(List<SourceDto> sourceDtoList);

    @Mapping(target = "businessId", source = "business.id")
    SourceDto toDto(Source source);

    List<SourceDto> toDto(List<Source> sourceList);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    void update(SourceDto sourceDto, @MappingTarget Source source);
}
