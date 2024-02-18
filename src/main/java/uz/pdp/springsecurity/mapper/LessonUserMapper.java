package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.payload.LessonUserDto;
import uz.pdp.springsecurity.entity.LessonUser;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonUserMapper {
    List<LessonUserDto> toDtoList(List<LessonUser> lessonUserList);

    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "lessonName", source = "lesson.name")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    LessonUserDto toDto(LessonUser lessonUser);
}
