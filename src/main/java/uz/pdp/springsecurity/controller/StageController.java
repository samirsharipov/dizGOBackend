package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.StageDto;
import uz.pdp.springsecurity.service.StageService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/stage")
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @CheckPermission("ADD_PROJECT")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody StageDto stageDto) {
        ApiResponse apiResponse = stageService.add(stageDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_PROJECT")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody StageDto stageDto) {
        ApiResponse apiResponse = stageService.edit(id,stageDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_PROJECT")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = stageService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_PROJECT")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = stageService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_PROJECT', 'GET_OWN_PROJECT')")
    @GetMapping("/get-by-branch/{branchId}")
    public HttpEntity<?> getAllByBranch(@PathVariable UUID branchId) {
        ApiResponse apiResponse = stageService.getAllByBranch(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_PROJECT', 'GET_OWN_PROJECT')")
    @GetMapping("/get-by-branchPageable/{branchId}")
    public HttpEntity<?> getAllByBranch(@PathVariable UUID branchId,
                                          @RequestParam(defaultValue = "0", required = false) int page,
                                          @RequestParam(defaultValue = "10", required = false) int size) {
        ApiResponse apiResponse = stageService.getAllByBranchPageable(branchId,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
