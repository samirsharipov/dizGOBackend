package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.PaymentStatus;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PayStatusDto;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.PaymentStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayStatusService {

    @Autowired
    PaymentStatusRepository payStatusRepository;

    @Autowired
    BusinessRepository businessRepository;

    public ApiResponse add(PayStatusDto payStatusDto) {
        boolean b = payStatusRepository.existsByStatus(payStatusDto.getStatus());
        if (b) return new ApiResponse("SUCH A PAYMENT STATUS ALREADY EXISTS", false);

        Optional<Business> optionalBusiness = businessRepository.findById(payStatusDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("BUSINESS NOT FOUND", false);
        }

        PaymentStatus paymentStatus = new PaymentStatus(
                payStatusDto.getStatus()
        );
        payStatusRepository.save(paymentStatus);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, PayStatusDto payStatusDto) {
        Optional<PaymentStatus> statusOptional = payStatusRepository.findById(id);
        if (statusOptional.isEmpty()) return new ApiResponse("NOT FOUND", false);

        boolean b = payStatusRepository.existsByStatus(payStatusDto.getStatus());
        if (b) return new ApiResponse("SUCH A PAYMENT STATUS ALREADY EXISTS", false);

        PaymentStatus paymentStatus = payStatusRepository.getById(id);
        paymentStatus.setStatus(payStatusDto.getStatus());

        payStatusRepository.save(paymentStatus);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id) {
        if (!payStatusRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, payStatusRepository.findById(id).get());
    }

    public ApiResponse delete(UUID id) {
        if (!payStatusRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        payStatusRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAll() {
        List<PaymentStatus> all = payStatusRepository.findAll();
        if (all.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true,all);
    }
}
