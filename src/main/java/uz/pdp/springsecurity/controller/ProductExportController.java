package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.repository.AttachmentRepository;
import uz.pdp.springsecurity.service.AttachmentService;
import uz.pdp.springsecurity.service.ProductExportService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-export")
@RequiredArgsConstructor
public class ProductExportController {

    private final AttachmentService attachmentService;
    private final ProductExportService productExportService;
    private final AttachmentRepository attachmentRepository;

    @GetMapping("/download")
    public void download(HttpServletResponse response) {
        try {
            Optional<Attachment> optionalAttachment = attachmentRepository.findByName("products.csv");
            if (optionalAttachment.isPresent()) {
                Attachment attachment = optionalAttachment.get();
                attachmentService.download(attachment.getId(), response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/restart")
    public void restart() {
        productExportService.exportProducts();
    }
}
