package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.AttachmentContent;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.AttachmentContentRepository;
import uz.pdp.springsecurity.repository.AttachmentRepository;
import uz.pdp.springsecurity.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final AttachmentService attachmentService;

    /**
     * YANGI FILE YOKI RASM QO'SHISH
     *
     * @param request FILE KELADI
     * @return ApiResponse(message - > FILE SUCCESSFULLY SAVED, success - > true)
     */
    @PostMapping("/upload")
    public ApiResponse uploadFile(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();
            Attachment attachment = new Attachment();
            attachment.setFileOriginalName(originalFilename);
            attachment.setSize(size);
            attachment.setContentType(contentType);
            Attachment savedAttachment = attachmentRepository.save(attachment);
            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setId(attachment.getId());
            attachmentContent.setMainContent(file.getBytes());
            attachmentContent.setAttachment(savedAttachment);
            attachmentContentRepository.save(attachmentContent);
            return new ApiResponse("FILE SUCCESSFULLY SAVED", true, attachment);

        }
        return new ApiResponse("Error", false);
    }

    /**
     * FILE INFOLARINI KO'RISH
     */
    @GetMapping("/info")
    public List<Attachment> getInfo(HttpServletResponse response) {
        return attachmentRepository.findAll();
    }

    /**
     * ID ORQALI FILE MALUMOTLAARINI KO'RISH
     *
     * @param id
     * @return
     */
    @CheckPermission("VIEW_MEDIA_INFO")
    @GetMapping("/info/{id}")
    public Attachment getInfo(@PathVariable UUID id, HttpServletResponse response) {
        Optional<Attachment> byId = attachmentRepository.findById(id);
        return byId.orElse(null);
    }

    /**
     * ID ORQALI RASMNI YUKLASH
     *
     * @param id
     * @param response
     * @throws IOException
     */

    @GetMapping("/download/{id}")
    public void download(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        attachmentService.download(id,response);
    }

    /**
     * NAME ORQALI FILENI YUKLASH
     *
     * @param name
     * @param response
     * @throws IOException
     */

    @GetMapping("/downloadWithName")
    public void downloadWithName(@RequestBody String name, HttpServletResponse response) throws IOException {
        attachmentService.download(name,response);
    }

    /**
     * ID ORQALI RASM YOKI FILENI O'CHIRISH
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable UUID id) {
        ApiResponse apiResponse = attachmentService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * BIR NECHTA FILENI BIR VAQTDA DB GA SAQLASH
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadAnyFile")
    public ApiResponse uploadAnyFiles(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());
            if (file != null) {
                String originalFilename = file.getOriginalFilename();
                Attachment attachment = new Attachment();
                attachment.setName(originalFilename);
                attachment.setFileOriginalName(originalFilename);
                attachment.setSize(file.getSize());
                attachment.setContentType(file.getContentType());

                attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent();
                attachmentContent.setMainContent(file.getBytes());
                attachmentContent.setAttachment(attachment);
                attachmentContentRepository.save(attachmentContent);
            } else {
                return new ApiResponse("NOT SAVED", false);
            }
        }
        return new ApiResponse("FILES SUCCESSFULLY SAVED", true);
    }


}
