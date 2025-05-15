package uz.dizgo.erp.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.CashService;

@RestController
@RequestMapping("api/cash")
@RequiredArgsConstructor
public class CashController {

	private final CashService cashService;

	@PostMapping
	public HttpEntity<?> addCash(@RequestParam UUID branchId) {
		ApiResponse apiResponse = cashService.addCash(branchId);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}

	@GetMapping("get-all-by-branchId/{branchId}")
	public HttpEntity<?> getAllCash(@PathVariable UUID branchId) {
		ApiResponse apiResponse = cashService.getAllCashes(branchId);
		return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
	}
}