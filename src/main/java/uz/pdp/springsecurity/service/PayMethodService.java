package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.PaymentMethod;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PayMethodDto;
import uz.pdp.springsecurity.repository.PayMethodRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayMethodService {

    private final PayMethodRepository payMethodRepository;

    public ApiResponse add(PayMethodDto payMethodDto) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(payMethodDto.getType());
        paymentMethod.setCard(paymentMethod.isCard());
        paymentMethod.setCash(payMethodDto.isCash());
        payMethodRepository.save(paymentMethod);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, PayMethodDto payMethodDto) {
        Optional<PaymentMethod> optional = payMethodRepository.findById(id);
        if (optional.isEmpty()) return new ApiResponse("NOT FOUND", false);

        PaymentMethod paymentMethod = payMethodRepository.getById(id);
        paymentMethod.setType(payMethodDto.getType());
        paymentMethod.setCard(paymentMethod.isCard());
        paymentMethod.setCash(payMethodDto.isCash());
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

    public ApiResponse getAll() {
        List<PaymentMethod> all = payMethodRepository.findAll();
        if (all.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, all);
    }
}
