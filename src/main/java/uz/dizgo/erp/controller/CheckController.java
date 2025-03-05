package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.CheckService;

import java.util.UUID;

@RestController
@RequestMapping("/api/check")
@RequiredArgsConstructor
public class CheckController {

    private final CheckService checkService;

    @GetMapping("check-plu-code/{businessId}/{pluCode}")
    public HttpEntity<?> check(@PathVariable UUID businessId, @PathVariable String pluCode) {
        ApiResponse apiResponse = checkService.checkPluCode(businessId, pluCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
