package uz.pdp.springsecurity.shoxjaxon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.shoxjaxon.repository.CustomerRepository2;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class MijozlarController {
    private static final Logger logger = LoggerFactory.getLogger(MijozlarController.class);

    @Autowired
    private CustomerRepository2 sotuvRepository;

    @GetMapping("/totalCustomers")
    public ResponseEntity<?> getTotalCustomers(@RequestParam(name = "businessId") UUID businessId) {
        logger.debug("Received request for total customers with businessId: {}", businessId);
        try {
            Integer totalCustomers = sotuvRepository.getTotalCustomersForBusiness(businessId);
            return ResponseEntity.ok(totalCustomers);
        } catch (Exception e) {
            logger.error("Unexpected error getting total customers for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error getting total customers for businessId: " + businessId);
        }
    }

    @GetMapping("/totalCustomersByBranch")
    public ResponseEntity<?> getTotalCustomersByBranch(@RequestParam(name = "branchId") UUID branchId) {
        logger.debug("Received request for total customers with branchId: {}", branchId);
        try {
            Integer totalCustomers = sotuvRepository.getTotalCustomersForBranch(branchId);
            return ResponseEntity.ok(totalCustomers);
        } catch (Exception e) {
            logger.error("Unexpected error getting total customers for branchId: " + branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error getting total customers for branchId: " + branchId);
        }
    }

    @GetMapping("/totalSuppliers")
    public ResponseEntity<?> getTotalSuppliers(@RequestParam(name = "businessId") UUID businessId) {
        logger.debug("Received request for total suppliers with businessId: {}", businessId);
        try {
            Integer totalSuppliers = sotuvRepository.getTotalSuppliersForBusiness(businessId);
            return ResponseEntity.ok(totalSuppliers);
        } catch (Exception e) {
            logger.error("Unexpected error getting total suppliers for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error getting total suppliers for businessId: " + businessId);
        }
    }

    @GetMapping("/totalTrades")
    public ResponseEntity<?> getTotalTrades(@RequestParam UUID businessId,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        logger.debug("Received request for total trades with businessId: {}, startDate: {}, endDate: {}", businessId, startDate, endDate);
        try {
            Integer totalTrades = sotuvRepository.getTotalTradesForBusiness(businessId, startDate, endDate);
            return ResponseEntity.ok(totalTrades);
        } catch (Exception e) {
            logger.error("Unexpected error getting total trades for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error getting total trades for businessId: " + businessId);
        }
    }

    @GetMapping("/totalTradesByBranch")
    public ResponseEntity<?> getTotalTradesByBranch(@RequestParam UUID branchId,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        logger.debug("Received request for total trades with branchId: {}, startDate: {}, endDate: {}", branchId, startDate, endDate);
        try {
            Integer totalTrades = sotuvRepository.getTotalTradesForBranch(branchId, startDate, endDate);
            return ResponseEntity.ok(totalTrades);
        } catch (Exception e) {
            logger.error("Unexpected error getting total trades for branchId: " + branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error getting total trades for branchId: " + branchId);
        }
    }
}
