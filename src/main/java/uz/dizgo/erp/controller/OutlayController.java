package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.enums.OUTLAY_STATUS;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.OutlayDto;
import uz.dizgo.erp.payload.OutlayTypeDto;
import uz.dizgo.erp.service.OutlayService;

import javax.validation.Valid;
import java.sql.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/outlay")
@RequiredArgsConstructor
public class OutlayController {

    private final OutlayService outlayService;

    @CheckPermission("ADD_OUTLAY")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody OutlayDto outlayDto) {
        ApiResponse apiResponse = outlayService.add(outlayDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_OUTLAY")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody OutlayDto outlayDto) {
        ApiResponse apiResponse = outlayService.edit(id, outlayDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_OUTLAY")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = outlayService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_OUTLAY")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = outlayService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_OUTLAY")
    @GetMapping("/get-by-date/{date}/{branch_id}")
    public HttpEntity<?> getByDate(@PathVariable Date date, @PathVariable UUID branch_id) {
        ApiResponse apiResponse = outlayService.getByDate(date, branch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("VIEW_OUTLAY")
    @GetMapping("/get-all-by-date/{business_id}/{date}")
    public HttpEntity<?> getByAllDate(@PathVariable UUID business_id, @PathVariable Date date) {
        ApiResponse apiResponse = outlayService.getAllByDate(date, business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_OUTLAY")
    @GetMapping("/get-by-branchId/{branch_id}")
    public HttpEntity<?> getAllByBranchId(@PathVariable UUID branch_id) {
        ApiResponse apiResponse = outlayService.getAllByBranchId(branch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_OUTLAY_ADMIN")
    @GetMapping("/get-by-businessId/{businessId}")
    public HttpEntity<?> getAllByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = outlayService.getAllByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_OUTLAY_ADMIN")
    @GetMapping("/copyOutlay")
    public HttpEntity<?> copyOutlay(@RequestParam UUID outlayId) {
        ApiResponse apiResponse = outlayService.copyOutlay(outlayId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/add-type")
    public HttpEntity<?> addOutlayType(@RequestBody OutlayTypeDto outlayTypeDto, @CurrentUser User user) {
        ApiResponse apiResponse = outlayService.addOutlayType(outlayTypeDto, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/type/{id}")
    public HttpEntity<?> editOutlayType(@RequestBody OutlayTypeDto outlayTypeDto, @CurrentUser User user, @PathVariable UUID id) {
        ApiResponse apiResponse = outlayService.editOutlayType(outlayTypeDto, user,id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/type")
    public HttpEntity<?> getOutlaysListByType(@RequestParam OUTLAY_STATUS type, @RequestParam UUID branchId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer limit, @RequestParam(required = false) java.util.Date startDate, @RequestParam(required = false)java.util.Date endDate) {
        ApiResponse apiResponse = outlayService.getOutlaysListByType(type, branchId, page, limit,startDate,endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/type/info/{outlayId}")
    public HttpEntity<?> getOutlayProducts(@PathVariable UUID outlayId) {
        ApiResponse apiResponse = outlayService.getOutlayProducts(outlayId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
