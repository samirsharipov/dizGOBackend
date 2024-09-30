package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.CustomerCredit;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CreditRepaymentDto;
import uz.pdp.springsecurity.payload.CustomerCreditGetDto;
import uz.pdp.springsecurity.repository.CustomerCreditRepository;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerCreditService {
    private final CustomerCreditRepository customerCreditRepository;

    public ApiResponse getCustomerCredit(UUID customerId) {
        List<CustomerCredit> all =
                customerCreditRepository.findByCustomerIdOrderByPaymentDate(customerId);

        if (all.isEmpty())
            return new ApiResponse("Customer not found", false);

        return new ApiResponse("success", true, toList(all));
    }

    public CustomerCreditGetDto toDto(CustomerCredit customerCredit) {
        CustomerCreditGetDto customerCreditGetDto = new CustomerCreditGetDto();

        customerCreditGetDto.setId(customerCredit.getId());
        customerCreditGetDto.setCustomerId(customerCredit.getCustomer().getId());
        customerCreditGetDto.setCustomerNumber(customerCredit.getCustomer().getPhoneNumber());
        customerCreditGetDto.setName(customerCredit.getCustomer().getName());
        customerCreditGetDto.setComment(customerCredit.getComment());
        customerCreditGetDto.setAmount(customerCredit.getAmount());
        customerCreditGetDto.setPaymentDate(customerCredit.getPaymentDate());
        customerCreditGetDto.setLastPaymentDate(customerCredit.getLastPaymentDate());
        customerCreditGetDto.setAmountGiven(customerCredit.getAmountGiven());
        customerCreditGetDto.setClosedDebt(customerCredit.getClosedDebt());
        customerCreditGetDto.setTotalSumma(customerCredit.getTotalSumma());

        return customerCreditGetDto;
    }

    public List<CustomerCreditGetDto> toList(List<CustomerCredit> customerCreditList) {
        List<CustomerCreditGetDto> customerCreditGetDtoList = new ArrayList<>();
        for (CustomerCredit customerCredit : customerCreditList) {
            customerCreditGetDtoList.add(toDto(customerCredit));
        }
        return customerCreditGetDtoList;
    }

    public ApiResponse repayment(UUID customerCreditId, CreditRepaymentDto repayment) {
        Optional<CustomerCredit> optionalCustomerCredit =
                customerCreditRepository.findById(customerCreditId);

        if (optionalCustomerCredit.isEmpty())
            return new ApiResponse("Customer not found", false);

        CustomerCredit customerCredit = optionalCustomerCredit.get();

        customerCredit.setAmountGiven(repayment.getRepaymentAmount());
        double v = customerCredit.getAmount() - repayment.getRepaymentAmount();

        if (v == 0)
            customerCredit.setClosedDebt(true);

        customerCredit.setLastPaymentDate(LocalDate.now());
        customerCreditRepository.save(customerCredit);
        return new ApiResponse("success", true);
    }

    public ApiResponse getById(UUID id) {

        Optional<CustomerCredit> optionalCustomerCredit = customerCreditRepository.findById(id);
        if (optionalCustomerCredit.isEmpty())
            return new ApiResponse("Customer not found", false);

        CustomerCredit customerCredit = optionalCustomerCredit.get();

        return new ApiResponse("success", true,toDto(customerCredit));
    }
}
