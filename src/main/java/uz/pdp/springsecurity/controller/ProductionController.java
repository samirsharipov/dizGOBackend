package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ProductionDto;
import uz.pdp.springsecurity.payload.ProductionTaskDto;
import uz.pdp.springsecurity.service.ProductionService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/production")
@RequiredArgsConstructor
public class ProductionController {
    private final ProductionService productionService;

    @CheckPermission("CREATE_PRODUCTION")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody ProductionDto productionDto) {
        ApiResponse apiResponse = productionService.add(productionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_TASK', 'GET_OWN_TASK')")
    @PostMapping("/task-production")
    public HttpEntity<?> addProductionForTask(@Valid @RequestBody ProductionTaskDto productionTaskDto) {
        ApiResponse apiResponse = productionService.addProductionForTask(productionTaskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_PRODUCTION")
    @GetMapping("/by-branch/{branchId}")
    public HttpEntity<?> getAll(@PathVariable UUID branchId,
                                @RequestParam int page,
                                @RequestParam int size,
                                @RequestParam(required = false) String name) {
        ApiResponse apiResponse = productionService.getAll(branchId, page, size, name);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_PRODUCTION")
    @GetMapping("/{productionId}")
    public HttpEntity<?> getOne(@PathVariable UUID productionId) {
        ApiResponse apiResponse = productionService.getOne(productionId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_PRODUCTION")
    @GetMapping("loss-production/{branchId}")
    public HttpEntity<?> getLossProduction(@PathVariable UUID branchId) {
        ApiResponse apiResponse = productionService.getLossProduction(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
