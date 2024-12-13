package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.Lesson;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.payload.LessonGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T17:19:34+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class LessonMapperImpl implements LessonMapper {

    @Override
    public LessonGetDto toGetDto(Lesson lesson) {
        if ( lesson == null ) {
            return null;
        }

        LessonGetDto lessonGetDto = new LessonGetDto();

        lessonGetDto.setRoleId( lessonRoleId( lesson ) );
        lessonGetDto.setRoleName( lessonRoleName( lesson ) );
        lessonGetDto.setAttachmentId( lessonAttachmentId( lesson ) );
        lessonGetDto.setId( lesson.getId() );
        lessonGetDto.setName( lesson.getName() );
        lessonGetDto.setLink( lesson.getLink() );
        lessonGetDto.setView( lesson.getView() );
        lessonGetDto.setDescription( lesson.getDescription() );
        lessonGetDto.setHasTest( lesson.isHasTest() );

        return lessonGetDto;
    }

    @Override
    public List<LessonGetDto> toGetDtoList(List<Lesson> lessonList) {
        if ( lessonList == null ) {
            return null;
        }

        List<LessonGetDto> list = new ArrayList<LessonGetDto>( lessonList.size() );
        for ( Lesson lesson : lessonList ) {
            list.add( toGetDto( lesson ) );
        }

        return list;
    }

    private UUID lessonRoleId(Lesson lesson) {
        if ( lesson == null ) {
            return null;
        }
        Role role = lesson.getRole();
        if ( role == null ) {
            return null;
        }
        UUID id = role.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String lessonRoleName(Lesson lesson) {
        if ( lesson == null ) {
            return null;
        }
        Role role = lesson.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID lessonAttachmentId(Lesson lesson) {
        if ( lesson == null ) {
            return null;
        }
        Attachment attachment = lesson.getAttachment();
        if ( attachment == null ) {
            return null;
        }
        UUID id = attachment.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
