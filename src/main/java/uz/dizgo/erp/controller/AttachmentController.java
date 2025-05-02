package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final ResponseEntityHelper helper;

    @PostMapping("/upload")
    public HttpEntity<ApiResponse> uploadFile(MultipartHttpServletRequest request) throws IOException {
        ApiResponse apiResponse = attachmentService.upload(request);
        return helper.buildResponse(apiResponse);
    }

    @PostMapping("/uploadAnyFile")
    public HttpEntity<ApiResponse> uploadAnyFiles(MultipartHttpServletRequest request) throws IOException {
        ApiResponse apiResponse = attachmentService.uploadAnyFiles(request);
        return helper.buildResponse(apiResponse);
    }

    @CheckPermission("VIEW_MEDIA_INFO")
    @GetMapping("/info/{id}")
    public HttpEntity<ApiResponse> getInfo(@PathVariable UUID id) {
        ApiResponse apiResponse = attachmentService.getById(id);
        return helper.buildResponse(apiResponse);
    }

    @GetMapping("/download/{id}")
    public void download(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        attachmentService.download(id, response);
    }

    @GetMapping("/downloadWithName")
    public void downloadWithName(@RequestBody String name, HttpServletResponse response) throws IOException {
        attachmentService.download(name, response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteMedia(@PathVariable UUID id) {
        ApiResponse apiResponse = attachmentService.delete(id);
        return helper.buildResponse(apiResponse);
    }
}
