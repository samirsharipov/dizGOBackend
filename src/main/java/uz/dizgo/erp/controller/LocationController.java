package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.LocationDTO;
import uz.dizgo.erp.service.LocationService;
import uz.dizgo.erp.helpers.ResponseEntityHelper;

import java.util.UUID;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final ResponseEntityHelper responseEntityHelper;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestBody LocationDTO locationDTO) {
        ApiResponse response = locationService.create(locationDTO);
        return responseEntityHelper.buildResponse(response);
    }

    // READ (Single location by ID)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        ApiResponse response = locationService.getById(id);
        return responseEntityHelper.buildResponse(response);
    }

    // READ (All locations)
    @GetMapping("/get-by-branch-id/{branchId}")
    public ResponseEntity<ApiResponse> getByBranchId(@PathVariable UUID branchId) {
        ApiResponse response = locationService.getByBranchId(branchId);
        return responseEntityHelper.buildResponse(response);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable UUID id, @RequestBody LocationDTO locationDTO) {
        ApiResponse response = locationService.update(id, locationDTO);
        return responseEntityHelper.buildResponse(response);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        ApiResponse response = locationService.delete(id);
        return responseEntityHelper.buildResponse(response);
    }
}