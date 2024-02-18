package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Lesson;
import uz.pdp.springsecurity.entity.LessonUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.mapper.LessonUserMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.TestResulDto;
import uz.pdp.springsecurity.repository.LessonRepository;
import uz.pdp.springsecurity.repository.LessonUserRepository;
import uz.pdp.springsecurity.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LessonUserService {
    private final LessonUserRepository lessonUserRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final LessonUserMapper lessonUserMapper;

    public void connectToUser(Lesson lesson) {
        List<User> userList = userRepository.findAllByRole_Id(lesson.getRole().getId());
        List<LessonUser> lessonUserList = new ArrayList<>();
        for (User user : userList) {
            lessonUserList.add(new LessonUser(
                    lesson,
                    user,
                    lesson.getView()
            ));
        }
        if (!lessonUserList.isEmpty()) lessonUserRepository.saveAll(lessonUserList);
    }

    public void connectToUserEdit(Lesson lesson) {
        List<LessonUser> lessonUserList = lessonUserRepository.findAllByLessonIdOrderByCreatedAtDesc(lesson.getId());
        for (LessonUser lessonUser : lessonUserList) {
            lessonUser.setLessonView(lesson.getView());
            if (lessonUser.getView() < lesson.getView())lessonUser.setFinish(false);
        }
        if (!lessonUserList.isEmpty()) lessonUserRepository.saveAll(lessonUserList);
    }

    public ApiResponse edit(UUID lessonId, UUID userId) {
        Optional<LessonUser> optionalLessonUser = lessonUserRepository.findByUserIdAndLessonId(userId, lessonId);
        if (optionalLessonUser.isPresent()) {
            LessonUser lessonUser = optionalLessonUser.get();
            lessonUser.setView(lessonUser.getView() + 1);
            if (!lessonUser.isFinish() && Objects.equals(lessonUser.getView(), lessonUser.getLessonView())){
                lessonUser.setFinish(true);
            }
            lessonUserRepository.save(lessonUser);
            return new ApiResponse("SUCCESS", true);
        }
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) return new ApiResponse("LESSON NOT FOUND", false);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return new ApiResponse("USER NOT FOUND", false);
        User user = optionalUser.get();
        Lesson lesson = optionalLesson.get();
        if (user.getRole().equals(lesson.getRole())) {
            lessonUserRepository.save(
                    new LessonUser(
                            lesson,
                            user,
                            lesson.getView()
                    ));
            return edit(lessonId, userId);
        }
        return new ApiResponse("USER ROLE DOES NOT MATCH", false);
    }

    public ApiResponse getByLesson(UUID lessonId) {
        if (!lessonRepository.existsById(lessonId))return new ApiResponse("LESSON NOT FOUND", false);
        List<LessonUser> lessonUserList = lessonUserRepository.findAllByLessonIdOrderByCreatedAtDesc(lessonId);
        if (lessonUserList.isEmpty())return new ApiResponse("USERS WITH LESSON NOT FOUND", false);
        return new ApiResponse(true, lessonUserMapper.toDtoList(lessonUserList));
    }

    public ApiResponse getAllByUser(UUID userId) {
        if (!userRepository.existsById(userId))return new ApiResponse("USER NOT FOUND", false);
        List<LessonUser> lessonUserList = lessonUserRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        if (lessonUserList.isEmpty())return new ApiResponse("LESSONS WITH USER NOT FOUND", false);
        return new ApiResponse(true, lessonUserMapper.toDtoList(lessonUserList));
    }

    public ApiResponse getOne(UUID lessonId, UUID userId) {
        Optional<LessonUser> optionalLessonUser = lessonUserRepository.findByUserIdAndLessonId(userId, lessonId);
        if (optionalLessonUser.isPresent())
            return new ApiResponse("SUCCESS", true, lessonUserMapper.toDto(optionalLessonUser.get()));
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) return new ApiResponse("LESSON NOT FOUND", false);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return new ApiResponse("USER NOT FOUND", false);
        User user = optionalUser.get();
        Lesson lesson = optionalLesson.get();
        if (user.getRole().equals(lesson.getRole())) {
            lessonUserRepository.save(
                    new LessonUser(
                            lesson,
                            user,
                            lesson.getView()
                    ));
            return getOne(lessonId, userId);
        }
        return new ApiResponse("USER ROLE DOES NOT MATCH", false);
    }

    public ApiResponse testResult(TestResulDto testResulDto) {
        Optional<LessonUser> optionalLessonUser = lessonUserRepository.findByUserIdAndLessonId(testResulDto.getUserId(), testResulDto.getLessonId());
        if (optionalLessonUser.isPresent()) {
            LessonUser lessonUser = optionalLessonUser.get();
            lessonUser.setSolveTest(true);
            lessonUser.setTestResult(testResulDto.getTestResult());
            lessonUserRepository.save(lessonUser);
            return new ApiResponse("SUCCESS", true);
        }
        Optional<Lesson> optionalLesson = lessonRepository.findById(testResulDto.getLessonId());
        if (optionalLesson.isEmpty()) return new ApiResponse("LESSON NOT FOUND", false);
        Optional<User> optionalUser = userRepository.findById(testResulDto.getUserId());
        if (optionalUser.isEmpty()) return new ApiResponse("USER NOT FOUND", false);
        User user = optionalUser.get();
        Lesson lesson = optionalLesson.get();
        if (user.getRole().equals(lesson.getRole())) {
            lessonUserRepository.save(
                    new LessonUser(
                            lesson,
                            user,
                            lesson.getView()
                    ));
            return testResult(testResulDto);
        }
        return new ApiResponse("USER ROLE DOES NOT MATCH", false);
    }
}
