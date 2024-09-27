package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BusinessDto;
import uz.pdp.springsecurity.payload.BusinessEditDto;
import uz.pdp.springsecurity.payload.CheckDto;
import uz.pdp.springsecurity.service.BusinessService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping("/create")
    public HttpEntity<?> add(@Valid @RequestBody BusinessDto businessDto) {
        ApiResponse apiResponse = businessService.add(businessDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_BUSINESS")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody BusinessEditDto businessEditDto) {
        ApiResponse apiResponse = businessService.edit(id, businessEditDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission("VIEW_BUSINESS")
//    @PreAuthorize(value = "hasAnyAuthority('VIEW_BUSINESS', 'ADD_TRADE')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id) {
        ApiResponse apiResponse = businessService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_BUSINESS")
    @GetMapping("/partners")
    public HttpEntity<?> getAllPartners() {
        ApiResponse apiResponse = businessService.getAllPartners();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_BUSINESS")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOne(@PathVariable UUID id) {
        ApiResponse apiResponse = businessService.deleteOne(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_BUSINESS")
    @GetMapping("/all")
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = businessService.getAll();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @GetMapping("/myAllBusiness")
    public HttpEntity<?> getMyAllBusiness(@RequestParam UUID userId) {
        ApiResponse apiResponse = businessService.getMyAllBusiness(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_BUSINESS")
    @PutMapping("/de-active/{businessId}")
    public HttpEntity<?> deActive(@PathVariable UUID businessId) {
        ApiResponse apiResponse = businessService.deActive(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_MY_BUSINESS")
    @PutMapping("/sale-minus/{businessId}")
    public HttpEntity<?> saleMinus(@PathVariable UUID businessId) {
        ApiResponse apiResponse = businessService.saleMinus(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_MY_BUSINESS")
    @PutMapping("/turn-exchange-product/{businessId}")
    public HttpEntity<?> turnExchangeProduct(@PathVariable UUID businessId,
                                             @RequestParam boolean isTurn) {
        ApiResponse apiResponse = businessService.turnExchangeProduct(businessId,isTurn);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_BUSINESS")
    @GetMapping("/info")
    public HttpEntity<?> getInfo(@RequestParam String time) {
        ApiResponse apiResponse = businessService.getInfo(time);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/checkBusinessName")
    public HttpEntity<?> checkBusinessName(@RequestBody CheckDto checkDto) {
        ApiResponse apiResponse = businessService.checkBusinessName(checkDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/checkUsername")
    public HttpEntity<?> checkUsername(@RequestBody CheckDto checkDto) {
        ApiResponse apiResponse = businessService.checkUsername(checkDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @PutMapping("/cheapSellingPrice/{cheapSellingPrice}")
    public HttpEntity<?> changeProductCheapSellingPrice(@PathVariable UUID cheapSellingPrice, @RequestParam Boolean checked) {
        ApiResponse apiResponse = businessService.changeProductCheapSellingPrice(cheapSellingPrice, checked);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @PutMapping("/changeGrossSell/{businessId}")
    public HttpEntity<?> changeGrossSellFunction(@PathVariable UUID businessId, @RequestParam Boolean checked) {
        ApiResponse apiResponse = businessService.changeGrossSell(businessId, checked);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{businessId}/edit-days")
    public ResponseEntity<?> updateEditDays(@PathVariable UUID businessId, @RequestBody int editDays) {
        businessService.updateEditDays(businessId, editDays);
        return ResponseEntity.ok(new ApiResponse("Edit days updated successfully", true));
    }

    @GetMapping("/{businessId}/edit-days")
    public ResponseEntity<?> getEditDays(@PathVariable UUID businessId) {
        int editDays = businessService.getEditDays(businessId);
        return ResponseEntity.ok(editDays);
    }
}
