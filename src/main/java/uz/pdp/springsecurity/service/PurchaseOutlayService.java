package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.PurchaseOutlay;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PurchaseOutlayDto;
import uz.pdp.springsecurity.repository.PurchaseOutlayRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOutlayService {

    private final PurchaseOutlayRepository repository;

    public ApiResponse getByPurchaseId(UUID purchaseId) {
        List<PurchaseOutlay> all = repository.findAllByPurchaseId(purchaseId);
        if (all.isEmpty())
            return new ApiResponse("not found", false);

        return new ApiResponse("success", true, toDtoList(all));
    }

    private PurchaseOutlayDto toDto(PurchaseOutlay purchaseOutlay) {
        PurchaseOutlayDto dto = new PurchaseOutlayDto();
        dto.setId(purchaseOutlay.getId());
        dto.setPurchaseId(purchaseOutlay.getPurchase().getId());
        dto.setCategoryId(purchaseOutlay.getCategory().getId());
        dto.setCategoryName(purchaseOutlay.getCategory().getName());
        dto.setBusinessId(purchaseOutlay.getBusiness().getId());
        return dto;
    }

    private List<PurchaseOutlayDto> toDtoList(List<PurchaseOutlay> purchaseOutlays) {
        List<PurchaseOutlayDto> dtos = new ArrayList<>();
        for (PurchaseOutlay purchaseOutlay : purchaseOutlays) {
            dtos.add(toDto(purchaseOutlay));
        }
        return dtos;
    }
}
