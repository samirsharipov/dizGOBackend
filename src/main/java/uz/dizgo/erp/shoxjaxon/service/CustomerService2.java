package uz.dizgo.erp.shoxjaxon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.shoxjaxon.activity.Customer2;
import uz.dizgo.erp.shoxjaxon.repository.CustomerRepository2;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService2 {

    private final CustomerRepository2 customerRepository;

    @Autowired
    public CustomerService2(CustomerRepository2 customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer2> getCustomersByBusinessId(UUID businessId) {
        List<Customer2> customers = customerRepository.getCustomersByBusinessId(businessId);

        // Set a unique id for each customer
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setId(UUID.randomUUID()); // Use any unique identifier
        }

        return customers;
    }


}
