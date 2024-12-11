package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Lesson;
import uz.pdp.springsecurity.entity.LessonUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.LessonUserDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T14:21:47+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class LessonUserMapperImpl implements LessonUserMapper {

    @Override
    public List<LessonUserDto> toDtoList(List<LessonUser> lessonUserList) {
        if ( lessonUserList == null ) {
            return null;
        }

        List<LessonUserDto> list = new ArrayList<LessonUserDto>( lessonUserList.size() );
        for ( LessonUser lessonUser : lessonUserList ) {
            list.add( toDto( lessonUser ) );
        }

        return list;
    }

    @Override
    public LessonUserDto toDto(LessonUser lessonUser) {
        if ( lessonUser == null ) {
            return null;
        }

        LessonUserDto lessonUserDto = new LessonUserDto();

        lessonUserDto.setLessonId( lessonUserLessonId( lessonUser ) );
        lessonUserDto.setLessonName( lessonUserLessonName( lessonUser ) );
        lessonUserDto.setUserId( lessonUserUserId( lessonUser ) );
        lessonUserDto.setFirstName( lessonUserUserFirstName( lessonUser ) );
        lessonUserDto.setLastName( lessonUserUserLastName( lessonUser ) );
        lessonUserDto.setId( lessonUser.getId() );
        lessonUserDto.setLessonView( lessonUser.getLessonView() );
        lessonUserDto.setView( lessonUser.getView() );
        lessonUserDto.setFinish( lessonUser.isFinish() );
        lessonUserDto.setSolveTest( lessonUser.isSolveTest() );
        lessonUserDto.setTestResult( lessonUser.getTestResult() );

        return lessonUserDto;
    }

    private UUID lessonUserLessonId(LessonUser lessonUser) {
        if ( lessonUser == null ) {
            return null;
        }
        Lesson lesson = lessonUser.getLesson();
        if ( lesson == null ) {
            return null;
        }
        UUID id = lesson.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String lessonUserLessonName(LessonUser lessonUser) {
        if ( lessonUser == null ) {
            return null;
        }
        Lesson lesson = lessonUser.getLesson();
        if ( lesson == null ) {
            return null;
        }
        String name = lesson.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID lessonUserUserId(LessonUser lessonUser) {
        if ( lessonUser == null ) {
            return null;
        }
        User user = lessonUser.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String lessonUserUserFirstName(LessonUser lessonUser) {
        if ( lessonUser == null ) {
            return null;
        }
        User user = lessonUser.getUser();
        if ( user == null ) {
            return null;
        }
        String firstName = user.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String lessonUserUserLastName(LessonUser lessonUser) {
        if ( lessonUser == null ) {
            return null;
        }
        User user = lessonUser.getUser();
        if ( user == null ) {
            return null;
        }
        String lastName = user.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }
}
