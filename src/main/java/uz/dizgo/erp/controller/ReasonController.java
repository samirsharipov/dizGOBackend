package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ReasonDto;
import uz.dizgo.erp.service.ReasonService;

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
    public ResponseEntity<ApiResponse> getAllReasons(@RequestParam UUID businessId) {
        ApiResponse response = reasonService.getAllReasons(businessId);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
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