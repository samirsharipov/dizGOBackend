package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.mapper.LessonMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LessonDto;
import uz.pdp.springsecurity.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final AttachmentRepository attachmentRepository;
    private final BusinessRepository businessRepository;
    private final LessonRepository lessonRepository;
    private final RoleRepository roleRepository;
    private final LessonMapper lessonMapper;
    private final LessonUserService lessonUserService;

    public ApiResponse add(LessonDto lessonDto) {
        return addEdit(new Lesson(), lessonDto, false);
    }

    public ApiResponse edit(UUID lessonId, LessonDto lessonDto) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        return optionalLesson.map(lesson -> addEdit(lesson, lessonDto, true)).orElseGet(() -> new ApiResponse("LESSON NOT FOUND", false));
    }

    private ApiResponse addEdit(Lesson lesson, LessonDto lessonDto, boolean edit) {
        Optional<Role> optionalRole = roleRepository.findById(lessonDto.getRoleId());
        if (optionalRole.isEmpty()) return new ApiResponse("ROLE NOT FOUND", false);
        lesson.setRole(optionalRole.get());
        lesson.setName(lessonDto.getName());
        lesson.setView(lessonDto.getView());
        lesson.setLink(lessonDto.getLink());
        lesson.setDescription(lessonDto.getDescription());
        if (lessonDto.getAttachmentId() != null){
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(lessonDto.getAttachmentId());
            optionalAttachment.ifPresent(lesson::setAttachment);
        }
        lessonRepository.save(lesson);
        if (!edit)lessonUserService.connectToUser(lesson);
        else lessonUserService.connectToUserEdit(lesson);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getAll(UUID businessId) {
        if (!businessRepository.existsById(businessId)) new ApiResponse("BUSINESS NOT FOUND", false);
        List<Lesson> lessonList = lessonRepository.findAllByRole_BusinessId(businessId);
        if (lessonList.isEmpty()) return new ApiResponse("LESSON NOT FOUND", false);
        return new ApiResponse( true, lessonMapper.toGetDtoList(lessonList));
    }

    public ApiResponse getAllByRole(UUID roleId) {
        if (!roleRepository.existsById(roleId)) new ApiResponse("ROLE NOT FOUND", false);
        List<Lesson> lessonList = lessonRepository.findAllByRoleId(roleId);
        if (lessonList.isEmpty()) return new ApiResponse("LESSON NOT FOUND", false);
        return new ApiResponse( true, lessonMapper.toGetDtoList(lessonList));
    }

    public ApiResponse getOne(UUID lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        return optionalLesson.map(lesson -> new ApiResponse(true, lessonMapper.toGetDto(lesson))).orElseGet(() -> new ApiResponse("LESSON NOT FOUND", false));
    }
    public ApiResponse delete(UUID lessonId) {
        if (!lessonRepository.existsById(lessonId)) return new ApiResponse("LESSON NOT FOUND", false);
        try {
            lessonRepository.deleteById(lessonId);
            return new ApiResponse("SUCCESS", true);
        } catch (Exception e) {
            return new ApiResponse("ERROR", false);
        }
    }
}
