package uz.pdp.springsecurity.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.AttendanceService;

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

}