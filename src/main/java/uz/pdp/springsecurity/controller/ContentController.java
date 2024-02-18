package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ContentDto;
import uz.pdp.springsecurity.service.ContentService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @CheckPermission("CREATE_CONTENT")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody ContentDto contentDto) {
        ApiResponse apiResponse = contentService.add(contentDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_CONTENT")
    @PutMapping("/{contentId}")
    public HttpEntity<?> edit(@PathVariable UUID contentId, @Valid @RequestBody ContentDto contentDto) {
        ApiResponse apiResponse = contentService.edit(contentId, contentDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_CONTENT")
    @GetMapping("/by-branch/{branchId}")
    public HttpEntity<?> getAll(@PathVariable UUID branchId) {
        ApiResponse apiResponse = contentService.getAll(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_CONTENT")
    @GetMapping("/by-branch-pageable/{branchId}")
    public HttpEntity<?> getAllPageable(@PathVariable UUID branchId,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(defaultValue = "0", required = false) int page,
                                        @RequestParam(defaultValue = "10", required = false) int size) {
        ApiResponse apiResponse = contentService.getAllPageable(branchId,name,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("GET_CONTENT")
    @GetMapping("/{contentId}")
    public HttpEntity<?> getOne(@PathVariable UUID contentId) {
        ApiResponse apiResponse = contentService.getOne(contentId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_CONTENT")
    @DeleteMapping("/{contentId}")
    public HttpEntity<?> deleteOne(@PathVariable UUID contentId) {
        ApiResponse apiResponse = contentService.deleteOne(contentId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
