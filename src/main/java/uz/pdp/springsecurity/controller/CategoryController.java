package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CategoryDto;
import uz.pdp.springsecurity.service.CategoryService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @CheckPermission("ADD_CATEGORY")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody CategoryDto categoryDto) {
        ApiResponse apiResponse = categoryService.add(categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_CATEGORY")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody CategoryDto categoryDto) {
        ApiResponse apiResponse = categoryService.edit(id, categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_CATEGORY")
    @GetMapping("/{id}/{languageCode}")
    public HttpEntity<?> get(@PathVariable UUID id, @PathVariable String languageCode) {
        ApiResponse apiResponse = categoryService.get(id, languageCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_CATEGORY")
    @GetMapping("get-translate/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = categoryService.getAllTranslations(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_CATEGORY_ADMIN")
    @GetMapping("/get-by-businessId/{businessId}/{languageCode}")
    public HttpEntity<?> getAllByBusinessId(@PathVariable UUID businessId, @PathVariable String languageCode) {
        ApiResponse apiResponse = categoryService.getCategoryTreeByBusiness(businessId, languageCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("VIEW_CATEGORY")
    @GetMapping("/get-child-category/{id}/{languageCode}")
    public HttpEntity<?> getAllChildCategoryById(@PathVariable UUID id, @PathVariable String languageCode) {
        ApiResponse response = categoryService.getCategoryTree(id,languageCode);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @CheckPermission("DELETE_CATEGORY")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = categoryService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}