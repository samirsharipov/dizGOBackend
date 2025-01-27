package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Discount;
import uz.pdp.springsecurity.entity.Language;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.enums.DiscountType;
import uz.pdp.springsecurity.mapper.converts.ProductConvert;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.DiscountRepository;
import uz.pdp.springsecurity.repository.LanguageRepository;
import uz.pdp.springsecurity.repository.ProductRepository;
import uz.pdp.springsecurity.repository.specifications.DiscountSpecifications;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final ProductConvert productConvert;
    private final LanguageRepository languageRepository;
    @PersistenceContext
    private EntityManager entityManager;


    // ✅ **Chegirma yaratish**
    public ApiResponse createDiscount(DiscountDto discountDto) {
        Discount discount = new Discount();

        discount.setName(discountDto.getName());
        discount.setDescription(discountDto.getDescription());
        discount.setType(DiscountType.valueOf(discountDto.getType()));
        discount.setValue(discountDto.getValue());
        discount.setStartDate(discountDto.getStartDate());
        discount.setEndDate(discountDto.getEndDate());
        discount.setStartHour(discountDto.getStartHour());
        discount.setEndHour(discountDto.getEndHour());
        discount.setWeekDays(discountDto.getWeekDays());

        discount.setWeekday(discountDto.getWeekDays() != null && !discountDto.getWeekDays().isEmpty());
        discount.setTime(discountDto.getStartHour() != null && discountDto.getEndHour() != null);

        discount.setProducts(productRepository.findAllById(discountDto.getProductIds()));
        discount.setBranches(branchRepository.findAllById(discountDto.getBranchIds()));

        if (discountDto.getStartDate().toLocalDateTime().toLocalDate().isEqual(LocalDate.now())) {
            discount.setActive(true);
            // productlarning discount columnini true qilish
            this.updateProductDiscountStatusId(discountDto.getProductIds(), true);
        } else {
            discount.setActive(false);
        }

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
        List<DiscountGetDto> discountGetDtoList = criteriaApi(businessId, branchId, type, startDate, endDate)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new ApiResponse("Chegirmalar ro‘yxati", true, discountGetDtoList);
    }

    // ✅ **Chegirmani yangilash**
    public ApiResponse updateDiscount(UUID id, DiscountEditDto discountDetails) {
        return discountRepository.findById(id)
                .map(discount -> {
                    discount.setValue(discountDetails.getValue());
                    if (discountDetails.getStartTime() != null) discount.setStartDate(discountDetails.getStartTime());
                    if (discountDetails.getEndTime() != null) discount.setEndDate(discountDetails.getEndTime());

                    discount.setProducts(productRepository.findAllById(discountDetails.getProductIds()));
                    discount.setWeekday(discountDetails.getWeekDays() != null && !discountDetails.getWeekDays().isEmpty());
                    discount.setTime(discountDetails.getStartHour() != null && discountDetails.getEndHour() != null);

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
                    updateProductDiscountStatus(discount.getProducts(), false);
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
                    updateProductDiscountStatus(discount.getProducts(), false);
                    return new ApiResponse("Chegirma tizimdan o‘chirildi (Soft delete)", true);
                })
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }

    public ApiResponse active(UUID id) {
        return discountRepository.findById(id)
                .map(discount -> {
                    discount.setActive(true);
                    discount.setStartDate(new Timestamp(System.currentTimeMillis()));
                    discountRepository.save(discount);
                    updateProductDiscountStatus(discount.getProducts(), true);
                    return new ApiResponse("Chegirma muvaffaqiyatli faolsiz qilindi", true);
                })
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }

    // ✅ **`Discount` obyektini `DiscountGetDto` ga o'tkazish**
    private DiscountGetDto mapToDto(Discount discount) {
        List<DiscountGetDto.ProductInfo> productList = discount.getProducts().stream()
                .map(product -> new DiscountGetDto.ProductInfo(product.getId(), product.getName(), product.getSalePrice()))
                .collect(Collectors.toList());

        List<UUID> branchIds = discount.getBranches().stream()
                .map(Branch::getId) // Branch obyektidan faqat id maydonini olamiz
                .collect(Collectors.toList());

        return new DiscountGetDto(
                discount.getId(),
                productList,
                discount.getValue(),
                discount.isActive(),
                discount.getName(),
                discount.getDescription(),
                discount.getStartDate(),
                discount.getEndDate(),
                discount.getStartHour(),
                discount.getEndHour(),
                discount.getWeekDays(),
                discount.getType(),
                branchIds
        );
    }

    public void updateProductDiscountStatusId(List<UUID> productIds, boolean status) {
        List<Product> products = productRepository.findAllById(productIds);
        products.forEach(product -> product.setDiscount(status));
        productRepository.saveAll(products);
    }

    private void updateProductDiscountStatus(List<Product> products, boolean status) {
        products.forEach(product -> product.setDiscount(status));
        productRepository.saveAll(products);
    }

    public ApiResponse checkProductList(List<UUID> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        List<DiscountGetDto.ProductInfo> productInfoList = new ArrayList<>();
        products.forEach(product -> {
            if (product.getDiscount() != null && product.getDiscount()) {
                productInfoList.add(new DiscountGetDto.ProductInfo(product.getId(), product.getName(), product.getSalePrice()));
            }
        });
        return new ApiResponse("all", true, productInfoList);
    }

    public ApiResponse search(UUID branchId, String name, String code) {
        Language language = languageRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("LANGUAGE NOT FOUND"));

        Branch branch = findByIdOrThrow(branchRepository, branchId, "branch");
        UUID mainBranchBusinessId = branch.getMainBranchId() != null
                ? findByIdOrThrow(branchRepository, branch.getMainBranchId(), "product").getBusiness().getId()
                : branch.getBusiness().getId();

        List<Product> products = productRepository.findAllProductsWithTranslates(branch.getBusiness().getId(), name);
        if (products.isEmpty()) {
            products = productRepository.findAllProductsWithTranslates(mainBranchBusinessId, name);
        }

        if (products.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        List<ProductGetDto> productGetDtoList = products.stream()
                .map(product -> {
                    ProductGetDto productGetDto = productConvert.convertToDto(product);

                    product.getTranslations().stream()
                            .filter(translate -> translate.getLanguage().getId().equals(language.getId()))
                            .findFirst()
                            .ifPresent(translate -> {
                                productGetDto.setName(translate.getName());
                                productGetDto.setDescription(translate.getDescription());
                                productGetDto.setLongDescription(translate.getLongDescription());
                                productGetDto.setAttributes(translate.getAttributes());
                            });
                    return productGetDto;
                })
                .toList();
        if (!productGetDtoList.isEmpty()) {
            for (ProductGetDto productGetDto : productGetDtoList) {
                if (productGetDto.getDiscount() != null && productGetDto.getDiscount()) {
                    if (code.equals("uz")) {
                        return new ApiResponse(productGetDto.getName() + " mahlusoti alaqachon chegirmada!", false);
                    } else if (code.equals("ru")) {
                        return new ApiResponse("Продукт " + productGetDto.getName() + " уже на скидке!", false);
                    } else {
                        return new ApiResponse("The " + productGetDto.getName() + " product is already on sale", false);
                    }
                }
            }
        }

        return new ApiResponse("all", true, productGetDtoList);
    }

    public static <T> T findByIdOrThrow(JpaRepository<T, UUID> repository, UUID id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(entityName + " not found"));
    }

    public List<Discount> criteriaApi(UUID businessId, UUID branchId, DiscountType type, Timestamp startDate, Timestamp endDate) {
        Specification<Discount> spec = Specification.where(null);

        if (branchId != null) {
            spec = spec.and(DiscountSpecifications.belongsToBranch(branchId));
        } else if (businessId != null) {
            spec = spec.and(DiscountSpecifications.belongsToBusiness(businessId));
        }

        if (type != null) {
            spec = spec.and(DiscountSpecifications.hasType(type));
        }

        if (startDate != null) {
            spec = spec.and(DiscountSpecifications.startDateAfter(startDate));
        }

        if (endDate != null) {
            spec = spec.and(DiscountSpecifications.endDateBefore(endDate));
        }

        spec = spec.and(DiscountSpecifications.isNotDeleted());

        return discountRepository.findAll(spec);
    }

}