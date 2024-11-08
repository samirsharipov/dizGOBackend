package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ReturnProductDto;
import uz.pdp.springsecurity.service.ReturnProductService;


import java.util.List;

@RestController
@RequestMapping("/api/return-product")
@RequiredArgsConstructor
public class ReturnProductController {

    private final ReturnProductService returnProductService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<ReturnProductDto> returnProductDtoList) {
        ApiResponse apiResponse = returnProductService.create(returnProductDtoList);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
