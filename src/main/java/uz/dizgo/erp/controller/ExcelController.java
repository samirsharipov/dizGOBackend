package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.configuration.ExcelGenerator;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ProductViewDtos;
import uz.dizgo.erp.service.ExcelService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @CheckPermission("GET_EXCEL")
    @GetMapping("/export-to-excel/{uuid}")
    public HttpEntity<?> exportIntoExcelFile(HttpServletResponse response, @PathVariable UUID uuid) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = PRODUCT " + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<ProductViewDtos> productViewDtos = excelService.getByBusiness(uuid);
        ExcelGenerator generator = new ExcelGenerator(productViewDtos);
        generator.generateExcelFile(response);
        return ResponseEntity.ok(response);
    }

//    @CheckPermission("POST_EXCEL")
//    @PostMapping("/upload")
//    public ApiResponse uploadFile(@RequestParam MultipartFile file,
//                                     @RequestParam UUID branchId,
//                                     @RequestParam UUID measurementId,
//                                     @RequestParam(required = false) UUID categoryId,
//                                     @RequestParam(required = false) UUID brandId) {
//            ApiResponse apiResponse = excelService.save(file, categoryId, measurementId, branchId, brandId);
//            ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
//            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse).getBody();
//
//        }

    @PostMapping("/upload")
    public ApiResponse uploadFileExcel(@RequestParam MultipartFile file,
                                       @RequestParam UUID branchId) {
        ApiResponse apiResponse = excelService.saveExcel(file, branchId);
        ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse).getBody();
    }

    @GetMapping("/customer-trade-history/{customerID}/{startDate}/{endDate}")
    public ResponseEntity<?> getCustomerTradeHistory(@PathVariable String customerID, @PathVariable Date startDate, @PathVariable Date endDate) {
        return excelService.getCustomerTradeHistory(customerID, startDate, endDate);
    }

    @GetMapping("/car/invoice/{carId}")
    public HttpEntity<?> getCarInvoiceHistory(@PathVariable UUID carId) {
        return excelService.getCarInvoiceHistory(carId);
    }
}