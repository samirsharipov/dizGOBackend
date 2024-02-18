package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.TestService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @CheckPermission("ADD_LESSON")
    @PostMapping("/{lessonId}")
    public HttpEntity<?> add(@RequestParam MultipartFile file, @PathVariable UUID lessonId) {
        ApiResponse apiResponse = testService.add(file, lessonId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON")
    @GetMapping("/all-by-lesson/{lessonId}")
    public HttpEntity<?> getAll(@PathVariable UUID lessonId) {
        ApiResponse apiResponse = testService.getAll(lessonId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON_ROLE")
    @GetMapping("/generate/{lessonId}")
    public HttpEntity<?> generate(@PathVariable UUID lessonId, @RequestParam UUID userId) {
        ApiResponse apiResponse = testService.generate(lessonId, userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_LESSON")
    @GetMapping("/sample")
    public HttpEntity<?> getSample(HttpServletResponse response) {
        ApiResponse apiResponse = testService.getSample(response);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_LESSON")
    @DeleteMapping("/{lessonId}")
    public HttpEntity<?> delete(@PathVariable UUID lessonId) {
        ApiResponse apiResponse = testService.deleteAll(lessonId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_LESSON")
    @DeleteMapping
    public HttpEntity<?> deleteAll(@RequestBody List<UUID> idList) {
        ApiResponse apiResponse = testService.deleteById(idList);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
