package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.MeasurementDto;
import uz.pdp.springsecurity.repository.MeasurementRepository;
import uz.pdp.springsecurity.service.MeasurementService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/measurement")
public class MeasurementController {
    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    MeasurementService measurementService;

    /**
     * YANGI BIRLIK QO'SHISH
     *
     * @param measurementDto
     * @return ApiResponse(success - > true message - > ADDED)
     */
    @CheckPermission("ADD_MEASUREMENT")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody MeasurementDto measurementDto) {
        ApiResponse apiResponse = measurementService.add(measurementDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BIRLIKNI ID ORQALI EDIT QILSIH
     *
     * @param id
     * @param measurementDto
     * @return ApiResponse(success - > true message - > EDITED)
     */
    @CheckPermission("EDIT_MEASUREMENT")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody MeasurementDto measurementDto) {
        ApiResponse apiResponse = measurementService.edit(id, measurementDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BITTA BIRLIKNI OLIB CHIQISH ID ORQALI
     *
     * @param id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_MEASUREMENT")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = measurementService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI DELETE QILISH
     *
     * @param id
     * @return ApiResponse(success - > true message - > DELETED)
     */
    @CheckPermission("DELETE_MEASUREMENT")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = measurementService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BUSINESSGA TEGISHLI BARCHA BIRLIKLARNI OLIB CHIQISH
     *
     * @param business_id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_MEASUREMENT")
    @GetMapping("/get-by-business/{business_id}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID business_id) {
        ApiResponse apiResponse = measurementService.getByBusiness(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
