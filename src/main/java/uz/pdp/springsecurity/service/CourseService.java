package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.Course;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CourseDto;
import uz.pdp.springsecurity.repository.AttachmentRepository;
import uz.pdp.springsecurity.repository.CourseRepository;

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
