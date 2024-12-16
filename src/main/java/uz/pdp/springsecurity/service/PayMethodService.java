package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PayMethodService {

    private final PayMethodRepository payMethodRepository;
    private final BusinessRepository businessRepository;

    public ApiResponse add(PayMethodDto payMethodDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(payMethodDto.getBusinessId());
        if (optionalBusiness.isEmpty())
            return new ApiResponse("BUSINESS NOT FOUND", false);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setBusiness(optionalBusiness.get());
        paymentMethod.setType(payMethodDto.getType());
        payMethodRepository.save(paymentMethod);

        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, PayMethodDto payMethodDto) {
        Optional<PaymentMethod> optional = payMethodRepository.findById(id);
        if (optional.isEmpty()) return new ApiResponse("NOT FOUND", false);

        PaymentMethod paymentMethod = payMethodRepository.getById(id);

        Optional<Business> optionalBusiness = businessRepository.findById(payMethodDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);

        paymentMethod.setBusiness(optionalBusiness.get());
        paymentMethod.setType(payMethodDto.getType());
        payMethodRepository.save(paymentMethod);

        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id) {
        if (!payMethodRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, payMethodRepository.findById(id).get());
    }

    public ApiResponse delete(UUID id) {
        Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(id);
        if (optionalPaymentMethod.isEmpty()) return new ApiResponse("NOT FOUND", false);
        PaymentMethod paymentMethod = optionalPaymentMethod.get();
        paymentMethod.setDeleted(true);
        paymentMethod.setActive(false);
        payMethodRepository.save(paymentMethod);

        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAllByBusiness(UUID business_id) {
        List<PaymentMethod> allByBranch_business_id = payMethodRepository.findAllByBusiness_Id(business_id);
        if (allByBranch_business_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBranch_business_id);
    }

    public ApiResponse addPaymentMethodSuperAdmin(PayMethodDto payMethodDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(payMethodDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("NOT FOUND", false);

        if (payMethodDto.isGlobal()) {
            List<Business> all = businessRepository.findAll();
            for (Business business : all) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setBusiness(business);
                paymentMethod.setType(payMethodDto.getType());
                payMethodRepository.save(paymentMethod);
            }
        } else {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setType(payMethodDto.getType());
            paymentMethod.setBusiness(optionalBusiness.get());
            payMethodRepository.save(paymentMethod);
        }
        return new ApiResponse("ADDED", true);
    }
}
