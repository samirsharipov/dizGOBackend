package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.TestResulDto;
import uz.dizgo.erp.service.LessonUserService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/lessonUser")
@RequiredArgsConstructor
public class LessonUserController {
    private final LessonUserService lessonUserService;

    @CheckPermission("VIEW_LESSON_ROLE")
    @PutMapping("/{lessonId}")
    public HttpEntity<?> edit(@PathVariable UUID lessonId, @RequestParam UUID userId) {
        ApiResponse apiResponse = lessonUserService.edit(lessonId, userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON_ROLE")
    @GetMapping("/by-lesson/{lessonId}")
    public HttpEntity<?> getByLesson(@PathVariable UUID lessonId) {
        ApiResponse apiResponse = lessonUserService.getByLesson(lessonId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON_ROLE")
    @GetMapping("/by-user/{userId}")
    public HttpEntity<?> getAllByUser(@PathVariable UUID userId) {
        ApiResponse apiResponse = lessonUserService.getAllByUser(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON_ROLE")
    @GetMapping("/by-user-and-lesson/{lessonId}")
    public HttpEntity<?> getOne(@PathVariable UUID lessonId, @RequestParam UUID userId) {
        ApiResponse apiResponse = lessonUserService.getOne(lessonId, userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON_ROLE")
    @PostMapping("/test-result")
    public HttpEntity<?> testResult(@RequestBody TestResulDto testResulDto) {
        ApiResponse apiResponse = lessonUserService.testResult(testResulDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
