package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PenaltyTemplateDto;
import uz.pdp.springsecurity.service.PenaltyTemplateService;

import java.util.UUID;

@RestController
@RequestMapping("/api/penalty-template")
@RequiredArgsConstructor
public class PenaltyTemplateController {

    private final PenaltyTemplateService service;
    private final ResponseEntityHelper responseEntityHelper;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody PenaltyTemplateDto penaltyTemplateDto) {
        return responseEntityHelper.buildResponse(service.create(penaltyTemplateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> edit(@PathVariable UUID id,
                                            @RequestBody PenaltyTemplateDto penaltyTemplateDto) {
        return responseEntityHelper.buildResponse(service.edit(id, penaltyTemplateDto));
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<ApiResponse> get(@PathVariable UUID branchId) {
        return responseEntityHelper.buildResponse(service.get(branchId));
    }
}
