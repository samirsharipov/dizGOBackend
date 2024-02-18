package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CourseDto;
import uz.pdp.springsecurity.service.CourseService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @CheckPermission("CREATE_COURSE")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody CourseDto courseDto) {
        ApiResponse apiResponse = courseService.add(courseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_COURSE")
    @PutMapping("/{courseId}")
    public HttpEntity<?> edit(@PathVariable UUID courseId, @Valid @RequestBody CourseDto courseDto) {
        ApiResponse apiResponse = courseService.edit(courseId, courseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_COURSE")
    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = courseService.getAll();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_COURSE")
    @GetMapping("/{courseId}")
    public HttpEntity<?> getOne(@PathVariable UUID courseId) {
        ApiResponse apiResponse = courseService.getOne(courseId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_COURSE")
    @DeleteMapping("/{courseId}")
    public HttpEntity<?> deleteOne(@PathVariable UUID courseId) {
        ApiResponse apiResponse = courseService.deleteOne(courseId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
