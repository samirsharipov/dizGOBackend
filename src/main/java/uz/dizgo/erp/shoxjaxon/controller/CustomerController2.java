package uz.dizgo.erp.shoxjaxon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.shoxjaxon.activity.Customer2;
import uz.dizgo.erp.shoxjaxon.service.CustomerService2;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/getcustomer")

public class CustomerController2 {

    private final CustomerService2 customerService;

    public CustomerController2(CustomerService2 customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getCustomersByBusinessId")
    public List<Customer2> getCustomersByBusinessId(@RequestParam UUID businessId) {
        return customerService.getCustomersByBusinessId(businessId);
    }


}