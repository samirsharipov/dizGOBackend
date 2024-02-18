package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonUserDto {
    private UUID id;
    private UUID lessonId;
    private String lessonName;

    private UUID userId;
    private String firstName;
    private String lastName;

    private Integer lessonView;
    private Integer view;
    private boolean finish;

    private boolean solveTest;
    private Integer testResult;
}
