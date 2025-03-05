package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.OutlayCategoryDto;
import uz.dizgo.erp.service.OutlayCategoryService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/outlayCategory")
@RequiredArgsConstructor
public class OutlayCategoryController {

    private final OutlayCategoryService outlayCategoryService;

    @CheckPermission("ADD_OUTLAY")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody OutlayCategoryDto outlayCategoryDto) {
        ApiResponse apiResponse = outlayCategoryService.add(outlayCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_OUTLAY")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody OutlayCategoryDto outlayCategoryDto) {
        ApiResponse apiResponse = outlayCategoryService.edit(id, outlayCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_OUTLAY")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = outlayCategoryService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_OUTLAY")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = outlayCategoryService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("VIEW_OUTLAY_ADMIN")
    @GetMapping("/get-by-businessId/{businessId}")
    public HttpEntity<?> getAllByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = outlayCategoryService.getAllByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
