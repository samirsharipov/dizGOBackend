package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.entity.FileData;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.FileDateRepository;
import uz.pdp.springsecurity.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDateRepository fileDateRepository;

    @PostMapping("/files")
    public HttpEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            long size = file.getSize();
            ApiResponse apiResponse = fileService.saveFileToDatabase(fileName, fileData, size);
            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE).build();
        }
    }

    @GetMapping("/files/{fileId}")
    public void download(@PathVariable UUID fileId, HttpServletResponse response) throws IOException {
        Optional<FileData> fileDataOptional = fileDateRepository.findById(fileId);
        if (fileDataOptional.isPresent()) {
            FileData fileData = fileDataOptional.get();
            String contentType = URLConnection.guessContentTypeFromName(fileData.getFileName());
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", fileData.getFileName() + "/:" + fileData.getSize());
            FileCopyUtils.copy(fileData.getFileData(), response.getOutputStream());
        }

    }

    @DeleteMapping("/files/{fileId}")
    public HttpEntity<?> deleteFile(@PathVariable UUID fileId) {
        ApiResponse apiResponse = fileService.deleteById(fileId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

        @GetMapping("/files/download/{fileId}")
        public ResponseEntity<byte[]> getFile(@PathVariable UUID fileId) {
            FileData fileData = fileService.getFileFromDatabase(fileId);
            if (fileData != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getFileName() + "\"")
                        .body(fileData.getFileData());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
}

