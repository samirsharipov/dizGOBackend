package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ReturnProductDto;
import uz.pdp.springsecurity.service.ReturnProductService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/return-product")
@RequiredArgsConstructor
public class ReturnProductController {

    private final ReturnProductService returnProductService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<ReturnProductDto> returnProductDtoList) {
        ApiResponse apiResponse = returnProductService.create(returnProductDtoList);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }


    @GetMapping("/{businessId}")
    public ResponseEntity<?> getAll(@PathVariable UUID businessId, @RequestParam int page, @RequestParam int size) {
        ApiResponse apiResponse = returnProductService.getAll(businessId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

}
