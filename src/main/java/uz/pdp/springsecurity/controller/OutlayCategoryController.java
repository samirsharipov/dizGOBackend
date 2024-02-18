package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.OutlayCategoryDto;
import uz.pdp.springsecurity.repository.OutlayCategoryRepository;
import uz.pdp.springsecurity.service.OutlayCategoryService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/outlayCategory")
public class OutlayCategoryController {
    @Autowired
    OutlayCategoryRepository outlayCategoryRepository;

    @Autowired
    OutlayCategoryService outlayCategoryService;

    /**
     * YANGI CHIQIM CATEGORY QO'SHISH
     *
     * @param outlayCategoryDto
     * @return ApiResponse(success - > true message - > ADDED)
     */
    @CheckPermission("ADD_OUTLAY")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody OutlayCategoryDto outlayCategoryDto) {
        ApiResponse apiResponse = outlayCategoryService.add(outlayCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI CHIQIM CATEGORYNI O'CHIRISH
     *
     * @param id
     * @param outlayCategoryDto
     * @return ApiResponse(success - > true message - > EDITED)
     */
    @CheckPermission("EDIT_OUTLAY")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody OutlayCategoryDto outlayCategoryDto) {
        ApiResponse apiResponse = outlayCategoryService.edit(id, outlayCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BITTA CHIQIM CATEGORYNI OLIB CHIQISH
     *
     * @param id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_OUTLAY")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = outlayCategoryService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BITTA CHIQIM CATEGORYNI DELETE QILISH
     *
     * @param id
     * @return ApiResponse(success - > true message - > DELETED)
     */
    @CheckPermission("DELETE_OUTLAY")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = outlayCategoryService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BITTA BRANCHGA YANI FILILAGA TEGISHLI BARCHA CHIQIM CATEGORYLAR RO'YXATINI OLIB CHIQISH
     *
     * @param branch_id
     * @return ApiResponse(success - > true message - > VALUE)
     */
    @CheckPermission("VIEW_OUTLAY")
    @GetMapping("/get-by-branchId/{branch_id}")
    public HttpEntity<?> getAllByBranchId(@PathVariable UUID branch_id) {
        ApiResponse apiResponse = outlayCategoryService.getAllByBranchId(branch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BUSINESSGA TEGISHLI BARCHA CHIQIM CATEGORYLAR RO'YHATINI OLIB CHIQISH
     *
     * @param businessId
     * @return ApiResponse(success - > true message - > VALUE)
     */
    @CheckPermission("VIEW_OUTLAY_ADMIN")
    @GetMapping("/get-by-businessId/{businessId}")
    public HttpEntity<?> getAllByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = outlayCategoryService.getAllByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
