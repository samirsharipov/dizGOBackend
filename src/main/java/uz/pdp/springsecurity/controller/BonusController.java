package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BonusDto;
import uz.pdp.springsecurity.service.BonusService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/bonus")
@RequiredArgsConstructor
public class BonusController {
    private final BonusService bonusService;

    @CheckPermission("ADD_BONUS")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody BonusDto bonusDto) {
        ApiResponse apiResponse = bonusService.add(bonusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_BONUS")
    @PutMapping("/{bonusId}")
    public HttpEntity<?> edit(@PathVariable UUID bonusId, @Valid @RequestBody BonusDto bonusDto) {
        ApiResponse apiResponse = bonusService.edit(bonusId, bonusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_BONUS")
    @PutMapping("/set-active/{bonusId}")
    public HttpEntity<?> setActive(@PathVariable UUID bonusId) {
        ApiResponse apiResponse = bonusService.setActive(bonusId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_BONUS")
    @GetMapping("/by-business/{businessId}")
    public HttpEntity<?> getAll(@PathVariable UUID businessId) {
        ApiResponse apiResponse = bonusService.getAll(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_BONUS")
    @GetMapping("/{bonusId}")
    public HttpEntity<?> getOne(@PathVariable UUID bonusId) {
        ApiResponse apiResponse = bonusService.getOne(bonusId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_BONUS")
    @DeleteMapping("/{bonusId}")
    public HttpEntity<?> delete(@PathVariable UUID bonusId) {
        ApiResponse apiResponse = bonusService.delete(bonusId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
