package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CostTypeDto;
import uz.pdp.springsecurity.service.CostTypeService;
import uz.pdp.springsecurity.utils.AppConstant;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/cost-type")
@RequiredArgsConstructor
public class CostTypeController {
    private final CostTypeService costTypeService;

    @CheckPermission("CREATE_PRODUCTION")
    @PostMapping
    public HttpEntity<?> create(@Valid @RequestBody CostTypeDto costTypeDto) {
        ApiResponse apiResponse = costTypeService.create(costTypeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("CREATE_PRODUCTION")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody CostTypeDto costTypeDto) {
        ApiResponse apiResponse = costTypeService.edit(id, costTypeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("CREATE_PRODUCTION")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = costTypeService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-branch/{branchId}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID branchId) {
        ApiResponse apiResponse = costTypeService.getAllByBranch(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-branch-pageable/{branchId}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID branchId,
                                          @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = costTypeService.getAllBranchPageable(branchId,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
