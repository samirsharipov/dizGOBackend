package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PayMethodDto;
import uz.pdp.springsecurity.service.PayMethodService;

import java.util.UUID;

@RestController
@RequestMapping("/api/paymethod")
public class PaymentMethodController {
    @Autowired
    PayMethodService payMethodService;

    /**
     * YANGI TO'LASH USULINI KIRITISH
     *
     * @RequesBody  payMethodDto
     * @return ApiResponse(success - > true, message - > ADDED)
     */
    @CheckPermission("ADD_PAY_METHOD")
    @PostMapping
    public HttpEntity<?> add(@RequestBody PayMethodDto payMethodDto) {
        ApiResponse apiResponse = payMethodService.add(payMethodDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * TO'LASH USULINI TAXRIRLASH
     *
     * @Id id
     * @RequesBody  payMethodDto
     * @return ApiResponse(success - > true, message - > EDITED)
     */
    @CheckPermission("EDIT_PAY_METHOD")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody PayMethodDto payMethodDto) {
        ApiResponse apiResponse = payMethodService.edit(id, payMethodDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BITTA TO'LASH USULINI OLIB CHIQISH
     *
     * @Id id
     * @return ApiResponse(success - > true, object - > value)
     */
    @CheckPermission("VIEW_PAY_METHOD")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = payMethodService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BITTA TOLOV USULINI DELETE QILISH
     *
     * @Id  id
     * @return ApiResponse(success - > true, message - > DELETED)
     */
    @CheckPermission("DELETE_PAY_METHOD")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = payMethodService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BUSINESSGA TEGISHLI BARCHA TO'LASH USULLLARNI OLIB CHIQISH
     * @Id  business_id
     * @return  ApiResponse(success - > true, object - > value)
     */
    @CheckPermission("VIEW_PAY_METHOD_ADMIN")
    @GetMapping("/get-by-business/{business_id}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID business_id) {
        ApiResponse apiResponse = payMethodService.getAllByBusiness(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
