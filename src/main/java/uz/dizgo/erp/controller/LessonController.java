package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.LessonDto;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.LessonService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @CheckPermission("ADD_LESSON")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody LessonDto lessonDto) {
        ApiResponse apiResponse = lessonService.add(lessonDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_LESSON")
    @PutMapping("/{lessonId}")
    public HttpEntity<?> edit(@PathVariable UUID lessonId, @Valid @RequestBody LessonDto lessonDto) {
        ApiResponse apiResponse = lessonService.edit(lessonId, lessonDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON")
    @GetMapping("/by-business/{businessId}")
    public HttpEntity<?> getAll(@PathVariable UUID businessId) {
        ApiResponse apiResponse = lessonService.getAll(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON_ROLE")
    @GetMapping("/by-role/{roleId}")
    public HttpEntity<?> getAllByRole(@PathVariable UUID roleId) {
        ApiResponse apiResponse = lessonService.getAllByRole(roleId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LESSON_ROLE")
    @GetMapping("/{lessonId}")
    public HttpEntity<?> getOne(@PathVariable UUID lessonId) {
        ApiResponse apiResponse = lessonService.getOne(lessonId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_LESSON")
    @DeleteMapping("/{lessonId}")
    public HttpEntity<?> deleteOne(@PathVariable UUID lessonId) {
        ApiResponse apiResponse = lessonService.delete(lessonId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
