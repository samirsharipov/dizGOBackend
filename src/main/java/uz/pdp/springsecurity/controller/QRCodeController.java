package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.service.QRCodeService;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;

    // /api/qr/generate?data=employeeId:timestamp:latitude:longitude
    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String data) throws Exception {
        byte[] qrCodeImage = qrCodeService.generateQRCode(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(qrCodeImage);
    }
}