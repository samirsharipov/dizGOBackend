package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.MeasurementDto;
import uz.pdp.springsecurity.service.MeasurementService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/measurement")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @CheckPermission("ADD_MEASUREMENT")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody MeasurementDto measurementDto) {
        ApiResponse apiResponse = measurementService.add(measurementDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_MEASUREMENT")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody MeasurementDto measurementDto) {
        ApiResponse apiResponse = measurementService.edit(id, measurementDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_MEASUREMENT")
    @GetMapping("/{id}/{languageCode}")
    public HttpEntity<?> get(@PathVariable UUID id, @PathVariable String languageCode) {
        ApiResponse apiResponse = measurementService.get(id,languageCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("VIEW_MEASUREMENT")
    @GetMapping("/get-by-business/{business_id}/{languageCode}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID business_id, @PathVariable String languageCode) {
        ApiResponse apiResponse = measurementService.getByBusiness(business_id,languageCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("DELETE_MEASUREMENT")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = measurementService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
