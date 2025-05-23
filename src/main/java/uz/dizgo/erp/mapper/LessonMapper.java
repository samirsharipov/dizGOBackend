package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Lesson;
import uz.dizgo.erp.payload.LessonGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {//ss

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "attachmentId", source = "attachment.id")
    LessonGetDto toGetDto(Lesson lesson);

    List<LessonGetDto> toGetDtoList(List<Lesson> lessonList);
}
