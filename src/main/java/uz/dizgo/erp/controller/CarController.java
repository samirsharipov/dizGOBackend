package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CarDto;
import uz.dizgo.erp.payload.CarInvoiceDto;
import uz.dizgo.erp.service.CarService;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public HttpEntity<?> add(@RequestBody CarDto carDto) {
        ApiResponse apiResponse = carService.add(carDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody CarDto carDto) {
        ApiResponse apiResponse = carService.edit(id, carDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-business/{businessId}")
    public HttpEntity<?> get(@PathVariable UUID businessId) {
        ApiResponse apiResponse = carService.get(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = carService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/invoice")
    public HttpEntity<?> addCarInvoice(@RequestBody CarInvoiceDto carInvoiceDto) {
        ApiResponse apiResponse = carService.addCarInvoice(carInvoiceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/invoice/{id}")
    public HttpEntity<?> getInvoiceInfo(@PathVariable UUID id, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit) {
        ApiResponse apiResponse = carService.getInvoiceInfo(id, page, limit);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/invoice/branch/{id}")
    public HttpEntity<?> getInvoiceListByBranch(@PathVariable UUID id, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit, @RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = carService.getInvoiceListByBranch(id, page, limit, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
