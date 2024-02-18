package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;
import uz.pdp.springsecurity.entity.Source;
import uz.pdp.springsecurity.payload.SourceDto;

import java.lang.annotation.Target;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SourceMapper {
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    Source toEntity(SourceDto sourceDto);

    List<Source> toEntity(List<SourceDto> sourceDtoList);

    @Mapping(target = "businessId", source = "business.id")
    SourceDto toDto(Source source);

    List<SourceDto> toDto(List<Source> sourceList);

    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    void update(SourceDto sourceDto, @MappingTarget Source source);
}
