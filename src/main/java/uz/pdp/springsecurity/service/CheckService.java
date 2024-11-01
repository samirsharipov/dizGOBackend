package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckService {
    private final ProductRepository productRepository;

    public ApiResponse checkPluCode(UUID businessId, String pluCode) {
        boolean exists = productRepository.existsByPluCodeAndBusiness_Id(pluCode, businessId);
        if (exists) {
            return new ApiResponse("plu code already exists", false);
        }

        return new ApiResponse("ok", true);
    }
}
