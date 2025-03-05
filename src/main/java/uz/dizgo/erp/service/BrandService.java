package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Brand;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.BrandDto;
import uz.dizgo.erp.repository.BrandRepository;
import uz.dizgo.erp.repository.BusinessRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final BusinessRepository businessRepository;
    private final MessageService messageService;


    public ApiResponse addBrand(BrandDto brandDto) {
        Brand brand = new Brand();
        Optional<Business> optionalBusiness = businessRepository.findById(brandDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse(messageService.getMessage("business.not.found"), false);
        }
        Business business = optionalBusiness.get();
        brand.setBusiness(business);
        brand.setName(brandDto.getName());
        brandRepository.save(brand);
        return new ApiResponse(messageService.getMessage("added.successfully"), true);
    }

    public ApiResponse editBrand(UUID id, BrandDto brandDto) {

        if (!brandRepository.existsById(id)) return new ApiResponse("BRAND NOT FOUND", false);

        Brand brand = brandRepository.getById(id);
        brand.setName(brandDto.getName());

        Optional<Business> optionalBusiness = businessRepository.findById(brandDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse(messageService.getMessage("business.not.found"), false);
        }

        brand.setBusiness(optionalBusiness.get());

        brandRepository.save(brand);
        return new ApiResponse(messageService.getMessage("edited.successfully"), true);
    }

    public ApiResponse getBrand(UUID id) {
        if (!brandRepository.existsById(id)) return new ApiResponse("BRAND NOT FOUND", false);
        return new ApiResponse(messageService.getMessage("found"), true, brandRepository.findById(id).get());
    }

    public ApiResponse deleteBrand(UUID id) {
        if (!brandRepository.existsById(id)) return new ApiResponse("BRAND NOT FOUND", false);
        brandRepository.deleteById(id);
        return new ApiResponse(messageService.getMessage("deleted.successfully"), true);
    }

    public ApiResponse getAllByBusiness(UUID business_id) {
        List<Brand> allByBranch_id = brandRepository.findAllByBusiness_Id(business_id);
        if (allByBranch_id.isEmpty()) return new ApiResponse(messageService.getMessage("not.found"), false);

        return new ApiResponse(messageService.getMessage("found"), true, allByBranch_id);
    }
}
