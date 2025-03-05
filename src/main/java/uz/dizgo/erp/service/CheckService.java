package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.ProductRepository;

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
