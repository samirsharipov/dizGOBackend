package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.WorkScheduleDto;
import uz.pdp.springsecurity.service.WorkScheduleService;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;

import java.util.UUID;

@RestController
@RequestMapping("/api/work-schedule")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;
    private final ResponseEntityHelper responseEntityHelper;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody WorkScheduleDto workScheduleDto) {
        ApiResponse response = workScheduleService.create(workScheduleDto);
        return responseEntityHelper.buildResponse(response);
    }

    // READ (single schedule by ID)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        ApiResponse response = workScheduleService.getById(id);
        return responseEntityHelper.buildResponse(response);
    }

    // READ (all schedules)
    @GetMapping("/get-by-user-id/{userId}")
    public ResponseEntity<ApiResponse> getByUserId(@PathVariable UUID userId) {
        ApiResponse response = workScheduleService.getByUserId(userId);
        return responseEntityHelper.buildResponse(response);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable UUID id, @RequestBody WorkScheduleDto workScheduleDto) {
        ApiResponse response = workScheduleService.update(id, workScheduleDto);
        return responseEntityHelper.buildResponse(response);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        ApiResponse response = workScheduleService.delete(id);
        return responseEntityHelper.buildResponse(response);
    }
}