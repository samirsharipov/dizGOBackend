package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BrandDto;
import uz.pdp.springsecurity.repository.BrandRepository;
import uz.pdp.springsecurity.service.BrandService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/brand")
public class BrandController {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    BrandService brandService;

    /**
     * YANGI BRAND QO'SHISH
     *
     * @param brandDto
     * @return ApiResponse(success - > true message - > ADDED)
     */


    @CheckPermission("ADD_BRAND")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody BrandDto brandDto) {
        ApiResponse apiResponse = brandService.addBrand(brandDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BRANDNI EDIT QILISH
     *
     * @param id
     * @param brandDto
     * @return ApiResponse(success - > true message - > EDITED)
     */
    @CheckPermission("EDIT_BRAND")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody BrandDto brandDto) {
        ApiResponse apiResponse = brandService.editBrand(id, brandDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * IDSI ORQALI BITTA BRAND OLISH
     *
     * @param id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_BRAND")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = brandService.getBrand(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BRANDNI DELETE QILISH
     *
     * @param id
     * @return ApiResponse(success - > true message - > DELETED)
     */
    @CheckPermission("DELETE_BRAND")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = brandService.deleteBrand(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BUSINESS ID ORQALI BITTA BUSINESSDAGI BARCHA BRANDLARNI OLIB CHIQISH
     *
     * @param business_id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_BRAND")
    @GetMapping("by-businessId/{business_id}")
    public HttpEntity<?> getAllByBranchId(@PathVariable UUID business_id) {
        ApiResponse apiResponse = brandService.getAllByBusiness(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
