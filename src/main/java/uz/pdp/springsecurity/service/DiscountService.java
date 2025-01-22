package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Discount;
import uz.pdp.springsecurity.enums.DiscountType;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.DiscountDto;
import uz.pdp.springsecurity.payload.DiscountEditDto;
import uz.pdp.springsecurity.payload.DiscountGetDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.DiscountRepository;
import uz.pdp.springsecurity.repository.ProductRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    // ✅ **Chegirma yaratish**
    public ApiResponse createDiscount(DiscountDto discountDto) {
        Discount discount = new Discount();
        discount.setName(discountDto.getName());
        discount.setDescription(discountDto.getDescription());
        discount.setType(DiscountType.valueOf(discountDto.getType()));
        discount.setValue(discountDto.getValue());
        discount.setStartDate(discountDto.getStartDate());
        discount.setEndDate(discountDto.getEndDate());
        discount.setProducts(productRepository.findAllById(discountDto.getProductIds()));
        discount.setBranches(branchRepository.findAllById(discountDto.getBranchIds()));

        discountRepository.save(discount);
        return new ApiResponse("Chegirma muvaffaqiyatli yaratildi", true);
    }

    // ✅ **Bitta chegirmalarni olish**
    public ApiResponse getDiscountById(UUID id) {
        return discountRepository.findById(id)
                .map(discount -> new ApiResponse("Chegirma topildi", true, mapToDto(discount)))
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }

    // ✅ **Chegirmalar ro‘yxatini olish**
    public ApiResponse getAllDiscounts(UUID businessId, UUID branchId, DiscountType type, Timestamp startDate, Timestamp endDate) {
        List<DiscountGetDto> discountGetDtoList = discountRepository.findAll().stream()
                .filter(discount -> branchId == null
                        ? discount.getBranches().stream().anyMatch(branch -> branch.getBusiness().getId().equals(businessId))
                        : discount.getBranches().stream().anyMatch(branch -> branch.getId().equals(branchId)))
                .filter(discount -> discount.getType().equals(type))
                .filter(discount -> discount.getStartDate().after(startDate) && discount.getEndDate().before(endDate))
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new ApiResponse("Chegirmalar ro‘yxati", true, discountGetDtoList);
    }

    // ✅ **Chegirmani yangilash**
    public ApiResponse updateDiscount(UUID id, DiscountEditDto discountDetails) {
        return discountRepository.findById(id)
                .map(discount -> {
                    discount.setValue(discountDetails.getValue());
                    discount.setStartDate(discountDetails.getStartTime());
                    discount.setEndDate(discountDetails.getEndTime());
                    discount.setProducts(productRepository.findAllById(discountDetails.getProductIds()));

                    discountRepository.save(discount);
                    return new ApiResponse("Chegirma muvaffaqiyatli yangilandi", true, mapToDto(discount));
                })
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }

    // ✅ **Chegirmani faolsiz qilish**
    public ApiResponse deactivateDiscount(UUID id) {
        return discountRepository.findById(id)
                .map(discount -> {
                    discount.setActive(false);
                    discountRepository.save(discount);
                    return new ApiResponse("Chegirma muvaffaqiyatli faolsiz qilindi", true);
                })
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }

    // ✅ **Chegirmani soft delete qilish**
    public ApiResponse deleteDiscount(UUID id) {
        return discountRepository.findById(id)
                .map(discount -> {
                    discount.setActive(false);
                    discount.setDeleted(true);
                    discountRepository.save(discount);
                    return new ApiResponse("Chegirma tizimdan o‘chirildi (Soft delete)", true);
                })
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }

    // ✅ **`Discount` obyektini `DiscountGetDto` ga o'tkazish**
    private DiscountGetDto mapToDto(Discount discount) {
        List<DiscountGetDto.ProductInfo> productList = discount.getProducts().stream()
                .map(product -> new DiscountGetDto.ProductInfo(product.getId(), product.getName()))
                .collect(Collectors.toList());

        return new DiscountGetDto(
                discount.getId(),
                productList,
                discount.getValue(),
                discount.getStartDate() + " - " + discount.getEndDate(),
                discount.getEndDate().after(new Timestamp(System.currentTimeMillis()))
        );
    }
}