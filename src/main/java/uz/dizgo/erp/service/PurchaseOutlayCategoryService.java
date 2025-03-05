package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.PurchaseOutlayCategory;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.PurchaseOutlayCategoryDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.PurchaseOutlayCategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOutlayCategoryService {
    private final PurchaseOutlayCategoryRepository repository;
    private final BusinessRepository businessRepository;

    public ApiResponse create(PurchaseOutlayCategoryDto purchaseOutlayCategoryDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(purchaseOutlayCategoryDto.getBusinessId());
        if (optionalBusiness.isEmpty())
            return new ApiResponse("not found", false);

        Business business = optionalBusiness.get();
        PurchaseOutlayCategory category = new PurchaseOutlayCategory();
        category.setBusiness(business);
        category.setName(purchaseOutlayCategoryDto.getName());
        repository.save(category);
        return new ApiResponse("created", true);
    }

    public ApiResponse edit(UUID id, PurchaseOutlayCategoryDto purchaseOutlayCategoryDto) {
        Optional<PurchaseOutlayCategory> optionalPurchaseOutlayCategory = repository.findById(id);
        if (optionalPurchaseOutlayCategory.isEmpty())
            return new ApiResponse("not found", false);

        PurchaseOutlayCategory category = optionalPurchaseOutlayCategory.get();
        category.setName(purchaseOutlayCategoryDto.getName());
        repository.save(category);

        return new ApiResponse("updated", true);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<PurchaseOutlayCategory> all = repository.findAllByBusinessIdAndDeletedFalse(businessId);
        if (all.isEmpty())
            return new ApiResponse("not found", false);

        return new ApiResponse("found", true, toDto(all));
    }

    public ApiResponse delete(UUID id) {
        Optional<PurchaseOutlayCategory> optional = repository.findById(id);
        if (optional.isEmpty())
            return new ApiResponse("not found", false);
        optional.get().setDeleted(true);
        repository.save(optional.get());
        return new ApiResponse("deleted", true);
    }

    public ApiResponse getById(UUID id) {
        Optional<PurchaseOutlayCategory> optionalPurchaseOutlayCategory = repository.findById(id);
        return optionalPurchaseOutlayCategory
                .map(purchaseOutlayCategory -> new ApiResponse("found", true, toDto(purchaseOutlayCategory)))
                .orElseGet(() -> new ApiResponse("not found", false));
    }

    private PurchaseOutlayCategoryDto toDto(PurchaseOutlayCategory purchaseOutlayCategory) {
        PurchaseOutlayCategoryDto dto = new PurchaseOutlayCategoryDto();
        dto.setId(purchaseOutlayCategory.getId());
        dto.setName(purchaseOutlayCategory.getName());
        dto.setBusinessId(purchaseOutlayCategory.getBusiness().getId());
        return dto;
    }

    private List<PurchaseOutlayCategoryDto> toDto(List<PurchaseOutlayCategory> purchaseOutlayCategories) {
        List<PurchaseOutlayCategoryDto> dtoList = new ArrayList<>();
        for (PurchaseOutlayCategory purchaseOutlayCategory : purchaseOutlayCategories) {
            dtoList.add(toDto(purchaseOutlayCategory));
        }
        return dtoList;
    }
}
