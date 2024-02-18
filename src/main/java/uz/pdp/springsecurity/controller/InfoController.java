package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.InfoService;

import java.sql.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/info")
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;

    @CheckPermission("VIEW_INFO_ADMIN")
    @GetMapping("/get-info-by-business/{businessId}")
    public HttpEntity<?> getInfoByBusiness(@PathVariable UUID businessId,
                                         @RequestParam(required = false) String date,
                                         @RequestParam(required = false) java.util.Date startDate,
                                         @RequestParam(required = false) java.util.Date endDate) {
        ApiResponse apiResponse = infoService.getInfoByBusiness(businessId,date,startDate,endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @CheckPermission("VIEW_INFO")
    @GetMapping("/get-info-by-branch/{branchId}")
    public HttpEntity<?> getInfoByBranch(@PathVariable UUID branchId,
                                         @RequestParam(required = false) String date,
                                         @RequestParam(required = false) java.util.Date startDate,
                                         @RequestParam(required = false) java.util.Date endDate) {
        ApiResponse apiResponse = infoService.getInfoByBranch(branchId,date,startDate,endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_INFO")
    @GetMapping("/get-info-by-outlay-trade/{branchId}")
    public HttpEntity<?> getInfoByOutlayTrade(@PathVariable UUID branchId) {
        ApiResponse apiResponse = infoService.getInfoByOutlayAndTrade(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_INFO")
    @GetMapping("/get-info-by-outlay-trade-business/{businessId}")
    public HttpEntity<?> getInfoByOutlayTradeByBusiness(@PathVariable UUID businessId) {
        ApiResponse apiResponse = infoService.getInfoByOutlayTradeByBusiness(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
