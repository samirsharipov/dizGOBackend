package uz.pdp.springsecurity.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.AttendanceService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final ResponseEntityHelper responseEntityHelper;

    // QR kod orqali keldi-ketdi tasdiqlash
    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse> checkInWithQRCode(@RequestParam UUID employeeId, @RequestParam UUID branchId, @RequestParam String qrCodeData,boolean input) {
        return responseEntityHelper.buildResponse(attendanceService.checkInWithQRCode(branchId, employeeId, qrCodeData,input));
    }

    @GetMapping("/get-userId/{userId}")
    public ResponseEntity<ApiResponse> getUserId(@PathVariable UUID userId,
                                                 @RequestParam int size,
                                                 @RequestParam int page) {
        return responseEntityHelper.buildResponse(attendanceService.getUserId(userId,size,page));
    }

    @GetMapping("/get-userId-diagram/{userId}")
    public ResponseEntity<ApiResponse> getUserIdDiagram(@PathVariable UUID userId,
                                                 @RequestParam(required = false) Timestamp startDate,
                                                 @RequestParam(required = false) Timestamp endDate) {
        return responseEntityHelper.buildResponse(attendanceService.getUserIdDiagram(userId,startDate,endDate));
    }

    @GetMapping("/get-by-businessId/{businessId}")
    public ResponseEntity<ApiResponse> findByBusinessId(@PathVariable UUID businessId,
                                                        @RequestParam(required = false) UUID branchId,
                                                        @RequestParam(required = false) Timestamp startDate,
                                                        @RequestParam(required = false) Timestamp endDate) {
        return responseEntityHelper.buildResponse(attendanceService.getByBusinessId(businessId,branchId,startDate,endDate));
    }

}