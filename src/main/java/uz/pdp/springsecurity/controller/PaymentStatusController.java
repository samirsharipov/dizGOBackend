package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PayStatusDto;
import uz.pdp.springsecurity.service.PayStatusService;

import java.util.UUID;

@RestController
@RequestMapping("/api/paystatus")
public class PaymentStatusController {
    @Autowired
    PayStatusService payStatusService;

    /**
     * YANGI TO'LANGANLIK STATUSINI QO'SHISH
     *
     * @RequaestBody payStatusDto
     * @return ApiResponse(success - > true message - > ADDED)
     */
    @CheckPermission("ADD_PAY_STATUS")
    @PostMapping
    public HttpEntity<?> add(@RequestBody PayStatusDto payStatusDto) {
        ApiResponse apiResponse = payStatusService.add(payStatusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * TO'LANGANLIK STATUSINI TAXRIRLASH
     *
     * @PathVariable id
     * @RequaestBody payStatusDto
     * @return ApiResponse(success - > true message - > EDITED)
     */
    @CheckPermission("EDIT_PAY_STATUS")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody PayStatusDto payStatusDto) {
        ApiResponse apiResponse = payStatusService.edit(id, payStatusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQLI BITTA TO'LANGANLIK STATUSINI QO'SHISH
     *
     * @param id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_PAY_STATUS")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = payStatusService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQLI BARCHA TO'LANGANLIK STATUSINI QO'SHISH
     *
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_PAY_STATUS")
    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = payStatusService.getAll();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI DELETE QILSIH
     *
     * @param id
     * @return ApiResponse(success - > true message - > DELETED)
     */
    @CheckPermission("DELETE_PAY_STATUS")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = payStatusService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}
