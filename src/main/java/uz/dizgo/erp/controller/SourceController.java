package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.SourceDto;
import uz.dizgo.erp.service.SourceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/source")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService service;

    @GetMapping("/byBusinessId/{businessId}")
    HttpEntity<?> getAll(@PathVariable UUID businessId){
        ApiResponse apiResponse = service.getAll(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping
    HttpEntity<?> create(@RequestBody SourceDto sourceDto){
        ApiResponse apiResponse = service.create(sourceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    HttpEntity<?> edit(@PathVariable UUID id,@RequestBody SourceDto sourceDto){
        ApiResponse apiResponse = service.edit(id,sourceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    HttpEntity<?> delete(@PathVariable UUID id){
        ApiResponse apiResponse = service.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
