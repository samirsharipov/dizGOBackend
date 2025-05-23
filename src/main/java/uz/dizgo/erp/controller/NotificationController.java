package uz.dizgo.erp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.NotificationDto;
import uz.dizgo.erp.service.NotificationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public HttpEntity<?> getAll(@CurrentUser User user,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        ApiResponse apiResponse = notificationService.getAll(user, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = notificationService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> deleteAll(@CurrentUser User user) {
        ApiResponse apiResponse = notificationService.delete(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody NotificationDto notificationDto) {
        ApiResponse apiResponse = notificationService.create(notificationDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
