package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ProductTypePostDto;
import uz.dizgo.erp.repository.ProductRepository;
import uz.dizgo.erp.repository.ProductTypeRepository;
import uz.dizgo.erp.service.ProductTypeService;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api/productType")
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeService service;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @CheckPermission("ADD_PRODUCT_TYPE")
    @PostMapping()
    public HttpEntity<?> add(@Valid @RequestBody ProductTypePostDto postDto) throws ParseException {
        ApiResponse apiResponse = service.addProductType(postDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_PRODUCT_TYPE")
    @GetMapping()
    public HttpEntity<?> getAll(@CurrentUser User user) {
        ApiResponse apiResponse = service.getProductType(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission("GET_PRODUCT_TYPE")
//    @GetMapping("/product_type/{id}")
//    public HttpEntity<?> getProductTypeByProductId(@Valid @PathVariable UUID id) {
//        ApiResponse apiResponse = service.getProductTypeByProductId(id);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
//    }

    @CheckPermission("GET_PRODUCT_TYPE")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getProductTypeById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("UPDATE_PRODUCT_TYPE")
    @PutMapping("/{id}")
    public HttpEntity<?> getById(@Valid @RequestBody ProductTypePostDto postDto, @PathVariable UUID id) {
        ApiResponse apiResponse = service.updateProductType(postDto, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_PRODUCT_TYPE")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.deleteProductTypeById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}
