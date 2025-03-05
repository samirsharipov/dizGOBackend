package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.payload.LessonUserDto;
import uz.dizgo.erp.entity.LessonUser;

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
