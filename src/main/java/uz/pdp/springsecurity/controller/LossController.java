package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LossDTO;
import uz.pdp.springsecurity.service.LossService;
import uz.pdp.springsecurity.utils.AppConstant;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/loss")
@RequiredArgsConstructor
public class LossController {
    private final LossService lossService;

    @CheckPermission("ADD_LOSS")
    @PostMapping
    public HttpEntity<?> create(@RequestBody LossDTO lossDTO) {
        ApiResponse apiResponse = lossService.create(lossDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_LOSS")
    @GetMapping("/by-branch/{branchId}")
    public HttpEntity<?> get(@PathVariable UUID branchId,
                             @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                             @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = lossService.get(branchId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_LOSS")
    @GetMapping("/{lossId}/{branchId}")
    public HttpEntity<?> getOne(@PathVariable UUID lossId, @PathVariable UUID branchId) {
        ApiResponse apiResponse = lossService.getOne(lossId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_LOSS")
    @GetMapping("/filteredDate/{branchId}")
    public HttpEntity<?> getSearchByDate(@PathVariable UUID branchId, @RequestParam Date startDate, @RequestParam Date endDate, @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page, @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = lossService.getSearchByDate(branchId, startDate, endDate, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/total")
    public HttpEntity<?> getTotalSumLos(@RequestParam UUID branchId, @RequestParam Date startDate, @RequestParam Date endDate) {
        ApiResponse apiResponse = lossService.getTotalSumLos(branchId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/products")
    public HttpEntity<?> getProductsByBranch(@RequestParam UUID branchId, @RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit) {
        ApiResponse apiResponse = lossService.getProductsByBranch(branchId, startDate, endDate,page,limit);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}