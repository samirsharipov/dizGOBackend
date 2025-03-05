package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Attachment;
import uz.dizgo.erp.entity.Course;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CourseDto;
import uz.dizgo.erp.repository.AttachmentRepository;
import uz.dizgo.erp.repository.CourseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final AttachmentRepository attachmentRepository;
    public ApiResponse add(CourseDto courseDto) {
        Course course = new Course();
        return createEdit(course, courseDto);
    }

    public ApiResponse edit(UUID courseId, CourseDto courseDto) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.map(course -> createEdit(course, courseDto)).orElseGet(() -> new ApiResponse("COURSE NOT FOUND", false));
    }

    private ApiResponse createEdit(Course course, CourseDto courseDto) {
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setLink(courseDto.getLink());
        if (courseDto.getPhotoId() != null) {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(courseDto.getPhotoId());
            optionalAttachment.ifPresent(course::setPhoto);
        }
        courseRepository.save(course);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getAll() {
        List<Course> courseList = courseRepository.findAll();
        if (courseList.isEmpty())return new ApiResponse("COURSE NOT FOUND", false);
        return new ApiResponse(true, courseList);
    }

    public ApiResponse getOne(UUID courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.map(course -> new ApiResponse(true, course)).orElseGet(() -> new ApiResponse("COURSE NOT FOUND", false));
    }

    public ApiResponse deleteOne(UUID courseId) {
        if (!courseRepository.existsById(courseId)) return new ApiResponse("COURSE NOT FOUND", false);
        try {
            courseRepository.deleteById(courseId);
            return new ApiResponse("SUCCESS", true);
        } catch (Exception e) {
            return new ApiResponse("COURSE NOT FOUND", false);
        }
    }
}
