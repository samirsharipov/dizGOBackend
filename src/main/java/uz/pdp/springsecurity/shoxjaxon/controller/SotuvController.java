package uz.pdp.springsecurity.shoxjaxon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.shoxjaxon.repository.SotuvRepositiry;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/businessTotalSums")

public class SotuvController {

    private final SotuvRepositiry sotuvRepositiry;

    @Autowired
    public SotuvController(SotuvRepositiry sotuvRepositiry) {
        this.sotuvRepositiry = sotuvRepositiry;
    }
    private static final Logger logger = LoggerFactory.getLogger(SotuvController.class);


    @GetMapping("/businessTotalSum")
    public HttpEntity<?> getBusinessTotalSum(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
    {
        try {

            if (startDate == null && endDate == null) {
                // Agar startDate va endDate belgilanmagan bo'lsa, barcha sanalarni hisoblab olish
                BigDecimal totalSum = (BigDecimal) sotuvRepositiry.calculateTotalSumForBranch(businessId);
                return ResponseEntity.ok(totalSum);
            } else {
                // Aks holda, startDate va endDate bo'yicha sanalarni hisoblab olish
                BigDecimal totalSum = (BigDecimal) sotuvRepositiry.calculateTotalSumForBranchWithDateRange(businessId, startDate, endDate);
                return ResponseEntity.ok(totalSum);
            }

        }  catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error calculating total sum for businessId: " + businessId);
        }
    }

    @GetMapping("/businessTotalFoyda")
    public HttpEntity<?> getBusinessTotalFoyda(@RequestParam(name = "businessId", required = true) UUID businessId) {
        try {
            BigDecimal totalFoyda = (BigDecimal) sotuvRepositiry.calculateTotalFoydaForBranch(businessId);
            return ResponseEntity.ok(totalFoyda);
        }  catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error calculating total sum for businessId: " + businessId);
        }
    }

    @GetMapping("/businessTotalXarajat")
    public HttpEntity<?> getBusinessTotalXarajat(@RequestParam(name = "businessId", required = true) UUID businessId) {
        try {
            BigDecimal totalXarajat = (BigDecimal) sotuvRepositiry.calculateTotalXarajatForBranch(businessId);
            return ResponseEntity.ok(totalXarajat);
        }  catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error calculating total sum for businessId: " + businessId);
        }
    }
}
