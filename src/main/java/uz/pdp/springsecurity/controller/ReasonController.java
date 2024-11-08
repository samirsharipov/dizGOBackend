package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ReasonDto;
import uz.pdp.springsecurity.service.ReasonService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reasons")
@RequiredArgsConstructor
public class ReasonController {

    private final ReasonService reasonService;

    @PostMapping
    public ResponseEntity<ApiResponse> createReason(@RequestBody ReasonDto reasonDto) {
        ApiResponse response = reasonService.createReason(reasonDto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReasonDto>> getAllReasons(@RequestParam UUID businessId) {
        List<ReasonDto> reasons = reasonService.getAllReasons(businessId);
        return ResponseEntity.ok(reasons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReasonById(@PathVariable UUID id) {
        ApiResponse response = reasonService.getReasonById(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateReason(@PathVariable UUID id, @RequestBody ReasonDto reasonDto) {
        ApiResponse response = reasonService.updateReason(id, reasonDto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReason(@PathVariable UUID id) {
        ApiResponse response = reasonService.deleteReason(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }
}