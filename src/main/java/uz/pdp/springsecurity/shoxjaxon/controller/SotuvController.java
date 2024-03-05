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
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            if (startDate == null && endDate == null) {
                // If startDate and endDate are not specified, calculate total sum without date range
                BigDecimal totalSum = sotuvRepositiry.calculateTotalSumForBranchWithDateRange(businessId);
                return ResponseEntity.ok(totalSum);
            } else {
                // If startDate and endDate are specified, calculate total sum within the date range
                BigDecimal totalSum = sotuvRepositiry.calculateTotalSumForBranchWithDateRangeFiltered(businessId, startDate, endDate);
                return ResponseEntity.ok(totalSum);
            }
        } catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error calculating total sum for businessId: " + businessId);
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
    public HttpEntity<?> getBusinessTotalXarajat(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalXarajat;

            if (startDate == null && endDate == null) {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranch(businessId);
            } else {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranchWithDateRange(businessId, startDate, endDate);
            }

            return ResponseEntity.ok(totalXarajat);
        }  catch (Exception e) {
            logger.error("Unexpected error calculating total xarajat for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error calculating total xarajat for businessId: " + businessId);
        }
    }




    @GetMapping("/xarajatForBranch")
    public ResponseEntity<BigDecimal> getXarajatForBranchWithDateRange(
            @RequestParam(name = "branchId", required = true) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        try {
            BigDecimal totalXarajat;

            if (startDate == null && endDate == null) {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranch(branchId);
            } else {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranchWithDateRange(branchId, startDate, endDate);
            }

            return ResponseEntity.ok(totalXarajat);

        } catch (Exception e) {
            // Handle the exception appropriately
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/xarajatForBusiness")
    public ResponseEntity<BigDecimal> getXarajatForBusinessWithDateRange(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        // ...
        return null;
    }
}
