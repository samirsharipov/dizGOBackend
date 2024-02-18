package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.PaymentMethod;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PayMethodDto;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.PayMethodRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayMethodService {
    @Autowired
    PayMethodRepository payMethodRepository;

    @Autowired
    BusinessRepository businessRepository;

    public ApiResponse add(PayMethodDto payMethodDto) {
        boolean b = payMethodRepository.existsByType(payMethodDto.getType());
        if (b) return new ApiResponse("SUCH A PAYMENT METHOD ALREADY EXISTS", false);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(payMethodDto.getType());

        Optional<Business> optionalBusiness = businessRepository.findById(payMethodDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND",false);
        paymentMethod.setBusiness(optionalBusiness.get());

        payMethodRepository.save(paymentMethod);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, PayMethodDto payMethodDto) {
        Optional<PaymentMethod> optional = payMethodRepository.findById(id);
        if (optional.isEmpty()) return new ApiResponse("NOT FOUND", false);

        boolean b = payMethodRepository.existsByType(payMethodDto.getType());
        if (b) return new ApiResponse("SUCH A PAYMENT METHOD ALREADY EXISTS", false);

        PaymentMethod paymentMethod = payMethodRepository.getById(id);
        paymentMethod.setType(payMethodDto.getType());

        Optional<Business> optionalBusiness = businessRepository.findById(payMethodDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND",false);
        paymentMethod.setBusiness(optionalBusiness.get());

        payMethodRepository.save(paymentMethod);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id) {
        if (!payMethodRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, payMethodRepository.findById(id).get());
    }

    public ApiResponse delete(UUID id) {
        if (!payMethodRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);

        payMethodRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAllByBusiness(UUID business_id) {
        List<PaymentMethod> allByBranch_business_id = payMethodRepository.findAllByBusiness_Id(business_id);
        if (allByBranch_business_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBranch_business_id);
    }
}
