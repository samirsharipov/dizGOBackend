package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.entity.CustomerSupplier;
import uz.pdp.springsecurity.entity.Supplier;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CustomerSupplierDto;
import uz.pdp.springsecurity.repository.CustomerRepository;
import uz.pdp.springsecurity.repository.CustomerSupplierRepository;
import uz.pdp.springsecurity.repository.SupplierRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerSupplierService {
    private final CustomerSupplierRepository customerSupplierRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

    public ApiResponse create(CustomerSupplierDto customerSupplierDto) {
        boolean exists = customerSupplierRepository
                .existsBySupplierIdOrCustomerId(
                        customerSupplierDto.getSupplierId(),
                        customerSupplierDto.getCustomerId());
        if (exists)
            return new ApiResponse("Customer or Supplier already exists", false);


        Optional<Customer> optionalCustomer = customerRepository
                .findById(customerSupplierDto.getCustomerId());
        Optional<Supplier> optionalSupplier = supplierRepository
                .findById(customerSupplierDto.getSupplierId());
        if (optionalCustomer.isEmpty() || optionalSupplier.isEmpty())
            return new ApiResponse("Customer or Supplier not found");


        CustomerSupplier customerSupplier = new CustomerSupplier();
        customerSupplier.setCustomer(optionalCustomer.get());
        customerSupplier.setSupplier(optionalSupplier.get());
        CustomerSupplier save = customerSupplierRepository.save(customerSupplier);
        calculation(save);
        return new ApiResponse("Customer supplier created", true);
    }

    public void calculation(CustomerSupplier customerSupplier) {
        Supplier supplier = customerSupplier.getSupplier();
        Customer customer = customerSupplier.getCustomer();
        double supplierDebt = supplier.getDebt();
        double customerDebt = customer.getDebt();

        customerSupplier.setBalance(supplierDebt - customerDebt);
        customerSupplierRepository.save(customerSupplier);
    }

    public ApiResponse getAll(UUID businessId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerSupplier> customerSuppliers =
                customerSupplierRepository.
                        findAllByCustomer_BusinessId(businessId, pageable);
        if (customerSuppliers.isEmpty())
            return new ApiResponse("Customer supplier not found");

        List<CustomerSupplierDto> customerSupplierDtoList = getCustomerSupplierDtoList(customerSuppliers);
        Map<String, Object> response = new HashMap<>();
        response.put("data", customerSupplierDtoList);
        response.put("currentPage", customerSuppliers.getNumber());
        response.put("totalItems", customerSuppliers.getTotalElements());
        response.put("totalPages", customerSuppliers.getTotalPages());

        return new ApiResponse("found",true,response);
    }

    private static @NotNull List<CustomerSupplierDto> getCustomerSupplierDtoList(Page<CustomerSupplier> customerSuppliers) {
        List<CustomerSupplierDto> customerSupplierDtoList = new ArrayList<>();
        for (CustomerSupplier customerSupplier : customerSuppliers) {
            CustomerSupplierDto customerSupplierDto = new CustomerSupplierDto();
            customerSupplierDto.setCustomerId(customerSupplier.getCustomer().getId());
            customerSupplierDto.setSupplierId(customerSupplier.getSupplier().getId());
            customerSupplierDto.setName(customerSupplier.getSupplier().getName());
            customerSupplierDto.setBalance(customerSupplier.getBalance());
            customerSupplierDtoList.add(customerSupplierDto);
        }
        return customerSupplierDtoList;
    }
}
