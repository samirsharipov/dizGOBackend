package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.HistoryService;
import uz.dizgo.erp.utils.AppConstant;

import java.util.UUID;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id,
                             @RequestParam(required = false) String name,
                             @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                             @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = historyService.get(id, name, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
