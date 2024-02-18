package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PrizeDto;
import uz.pdp.springsecurity.service.PrizeService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/prize")
@RequiredArgsConstructor
public class PrizeController {
    private final PrizeService prizeService;

    @CheckPermission("ADD_PRIZE")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody PrizeDto prizeDto) {
        ApiResponse apiResponse = prizeService.add(prizeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /*@CheckPermission("ADD_PRIZE")
    @PutMapping("/{prizeId}")
    public HttpEntity<?> edit(@PathVariable UUID prizeId, @Valid @RequestBody PrizeDto prizeDto) {
        ApiResponse apiResponse = prizeService.edit(prizeId, prizeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }*/

    @CheckPermission("VIEW_PRIZE")
    @GetMapping("/by-branch/{branchId}")
    public HttpEntity<?> getAll(@PathVariable UUID branchId) {
        ApiResponse apiResponse = prizeService.getAll(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRIZE")
    @GetMapping("/{prizeId}")
    public HttpEntity<?> getOne(@PathVariable UUID prizeId) {
        ApiResponse apiResponse = prizeService.getOne(prizeId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRIZE")
    @GetMapping("/by-user-all/{userId}")
    public HttpEntity<?> getByUserAll(@PathVariable UUID userId, @RequestParam UUID branchId) {
        ApiResponse apiResponse = prizeService.getByUserAll(userId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRIZE")
    @GetMapping("/by-user-last-month/{userId}")
    public HttpEntity<?> getByUserLastMonth(@PathVariable UUID userId, @RequestParam UUID branchId) {
        ApiResponse apiResponse = prizeService.getByUserLastMonth(userId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
