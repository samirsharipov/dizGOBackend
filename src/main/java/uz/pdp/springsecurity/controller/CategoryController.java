package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CategoryDto;
import uz.pdp.springsecurity.repository.CategoryRepository;
import uz.pdp.springsecurity.service.CategoryService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    /**
     * YANGI MAHSULOTLAR CATEGORYSINI QO'SHISH
     *
     * @param categoryDto
     * @return ApiResponse(success - > true message - > ADDED)
     */
    @CheckPermission("ADD_CATEGORY")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody CategoryDto categoryDto) {
        ApiResponse apiResponse = categoryService.add(categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * CATEGORYNI EDIT QILSIH
     *
     * @param id
     * @param categoryDto
     * @return ApiResponse(success - > true message - > EDITED)
     */
    @CheckPermission("EDIT_CATEGORY")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody CategoryDto categoryDto) {
        ApiResponse apiResponse = categoryService.edit(id, categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BITTA CATEGORYNI OLIB CHIQISH
     *
     * @param id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_CATEGORY")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = categoryService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI CATEGORYNI O'CHIRISH
     *
     * @param id
     * @return ApiResponse(success - > true message - > DELETED)
     */
    @CheckPermission("DELETE_CATEGORY")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = categoryService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BUSINESS ID ORQALI CATEGORYI OLIB CHIQISH
     *
     * @param businessId
     * @return ApiResponse(success - > true object - > value)
     */

    @CheckPermission("VIEW_CATEGORY_ADMIN")
    @GetMapping("/get-by-businessId/{businessId}")
    public HttpEntity<?> getAllByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = categoryService.getAllByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * Yangi kichik bo'lim qo'shish
     * @return ApiResponse(success - > true message - > ADDED)
     */
    @CheckPermission("ADD_CHILD_CATEGORY")
    @PostMapping("/addChildCategory")
    public HttpEntity<?> addChildCategory(@Valid @RequestBody CategoryDto categoryDto){
        ApiResponse apiResponse = categoryService.addChildCategory(categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * business idsi berilsa shunga tegishli bo'limlarni berib yuborish.
     * @param id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_CATEGORY")
    @GetMapping("/get-child-category/{id}")
    public HttpEntity<?> getAllChildCategoryById(@PathVariable UUID id){
        ApiResponse response = categoryService.getAllChildCategoryById(id);
        return ResponseEntity.status(response.isSuccess()? 200: 409).body(response);
    }

    @GetMapping("/get-child-category-by-parentId/{id}")
    public HttpEntity<?> getAllChildCategory(@PathVariable UUID id){
        ApiResponse response = categoryService.getAllChildcategoryByParentId(id);
        return ResponseEntity.status(response.isSuccess()? 200 : 409).body(response);
    }

    @GetMapping("/get-parent_category_by_business_id/{id}")
    public HttpEntity<?> getAllParentCategory(@PathVariable UUID id){
        ApiResponse response = categoryService.getAllParentCategory(id);
        return ResponseEntity.status(response.isSuccess()? 200 : 409).body(response);
    }
    
}
