package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BranchDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.service.BranchService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
    @Autowired
    BranchRepository branchRepository;

    @Autowired
    BranchService branchService;

    /**
     * YANGI BRANCH QO'SHISH
     * @param branchDto
     * @return ApiResponse(success->true message->ADDED)
     */
    @CheckPermission("ADD_BRANCH")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody BranchDto branchDto) {
        ApiResponse apiResponse = branchService.addBranch(branchDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BITTA BRANCHNI MALUMOTLARINI TAXRIRLASH
     * @param id
     * @return ApiResponse(success->true message->EDITED)
     */
    @CheckPermission("EDIT_BRANCH")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody BranchDto branchDto) {
        ApiResponse apiResponse = branchService.editBranch(id, branchDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ID ORQALI BITTA BRANCHNI OLISH
     * @param id
     * @return ApiResponse(success->true object->value)
     */
    @CheckPermission("VIEW_BRANCH")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = branchService.getBranch(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     *  BUSINESSGA TEGISHLI BRANCHNNI O'CHIRISH
     * @param id
     * @return ApiResponse(success->true message->DELETED)
     */

    @CheckPermission("DELETE_BRANCH")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = branchService.deleteBranch(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BUSINESSGA TEGISHLI BARCHA BRANCHLARNI OLIB CHIQISH
     * @param business_id
     * @return ApiResponse(success->true object->value)
     */
//    @CheckPermission("VIEW_BRANCH_ADMIN")
    @GetMapping("get-all-by-business-id/{business_id}")
    public HttpEntity<?> getByBusinessId(@PathVariable UUID business_id) {
        ApiResponse apiResponse = branchService.getByBusinessId(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
