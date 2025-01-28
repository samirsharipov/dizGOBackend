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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final LanguageRepository languageRepository;
    private final ProductService productService;


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
        discount.setActive(false);

        discount.setWeekday(discountDto.getWeekDays() != null && !discountDto.getWeekDays().isEmpty());
        discount.setTime(discountDto.getStartHour() != null && discountDto.getEndHour() != null);

        discount.setProducts(productRepository.findAllById(discountDto.getProductIds()));
        discount.setBranches(branchRepository.findAllById(discountDto.getBranchIds()));

        if (discountDto.getStartDate().toLocalDateTime().toLocalDate().isEqual(LocalDate.now()))
            discount.setActive(true);

        ApiResponse apiResponse = checkProductList(discountDto.getProductIds());
        if (apiResponse.isSuccess()) {
            return new ApiResponse(apiResponse.getMessage(), false, apiResponse.getObject());
        }

        this.updateProductDiscountStatusId(discountDto.getProductIds(), true);

        discountRepository.save(discount);
        return new ApiResponse("Chegirma muvaffaqiyatli yaratildi", true);
    }

    public ApiResponse getDiscountById(UUID id) {
        return discountRepository.findById(id)
                .map(discount -> new ApiResponse("Chegirma topildi", true, mapToDto(discount)))
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }

    public ApiResponse getAllDiscounts(UUID businessId, UUID branchId, DiscountType type, Timestamp startDate, Timestamp endDate) {
        List<DiscountGetDto> discountGetDtoList = criteriaApi(businessId, branchId, type, startDate, endDate)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new ApiResponse("Chegirmalar ro‘yxati", true, discountGetDtoList);
    }

    public ApiResponse updateDiscount(UUID id, DiscountEditDto discountDetails) {
        return discountRepository.findById(id)
                .map(discount -> {
                    // Chegirma qiymatini yangilash
                    discount.setValue(discountDetails.getValue());

                    // Boshlanish va tugash vaqtlarini yangilash
                    if (discountDetails.getStartTime() != null) {
                        discount.setStartDate(discountDetails.getStartTime());
                    }
                    if (discountDetails.getEndTime() != null) {
                        discount.setEndDate(discountDetails.getEndTime());
                    }

                    // Haftaning kunlarini yangilash
                    if (discountDetails.getWeekDays() != null && !discountDetails.getWeekDays().isEmpty()) {
                        discount.setWeekDays(discountDetails.getWeekDays());
                    }

                    // Chegirma amal qilish soatlarini yangilash
                    if (discountDetails.getStartHour() != null && discountDetails.getEndHour() != null) {
                        discount.setStartHour(discountDetails.getStartHour());
                        discount.setEndHour(discountDetails.getEndHour());
                        discount.setTime(true); // Soat bo‘yicha cheklovni faollashtirish
                    } else {
                        discount.setTime(false);
                    }

                    // **Mahsulotlarni yangilash** (eskilarni olib tashlash va yangilarni qo‘shish)
                    Set<UUID> currentProductIds = new HashSet<>();
                    if (discountDetails.getCurrentProductIds() != null) {
                        currentProductIds.addAll(discountDetails.getCurrentProductIds());
                    }

                    // **Eskirgan mahsulotlarni olib tashlash**
                    if (discountDetails.getOldProductIds() != null) {
                        List<Product> oldProducts = productRepository.findAllById(discountDetails.getOldProductIds());
                        for (Product product : oldProducts) {
                            product.setDiscount(false);
                        }
                        productRepository.saveAll(oldProducts);
                        discountDetails.getOldProductIds().forEach(currentProductIds::remove);
                    }

                    // **Yangi mahsulotlarni qo‘shishdan oldin ularning boshqa chegirmalarda borligini tekshirish**
                    List<Product> newProducts = new ArrayList<>();
                    if (discountDetails.getNewProductIds() != null) {
                        List<Product> potentialNewProducts = productRepository.findAllById(discountDetails.getNewProductIds());

                        for (Product product : potentialNewProducts) {
                            if (product.getDiscount() != null) {
                                return new ApiResponse("Mahsulot allaqachon boshqa chegirmaga bog‘langan: " + product.getName(), false);
                            }
                            product.setDiscount(true);
                            newProducts.add(product);
                        }

                        productRepository.saveAll(newProducts);
                        currentProductIds.addAll(discountDetails.getNewProductIds());
                    }

                    // **Yangilangan mahsulotlar ro‘yxatini saqlash**
                    List<Product> updatedProducts = productRepository.findAllById(currentProductIds);
                    discount.setProducts(updatedProducts);

                    discountRepository.save(discount);
                    return new ApiResponse("Chegirma muvaffaqiyatli yangilandi", true, mapToDto(discount));
                })
                .orElseGet(() -> new ApiResponse("Chegirma topilmadi", false));
    }


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
        if (productInfoList.isEmpty()) {
            return new ApiResponse("not found products", false);
        }
        return new ApiResponse("all", true, productInfoList);
    }

    public ApiResponse search(UUID branchId, String name, String code) {

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

        List<ProductGetDto> productGetDtoList = productService.getProductGetDtoList(products, code);
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

        if (branchId != null) spec = spec.and(DiscountSpecifications.belongsToBranch(branchId));
        else spec = spec.and(DiscountSpecifications.belongsToBusiness(businessId));

        if (type != null) spec = spec.and(DiscountSpecifications.hasType(type));
        if (startDate != null) spec = spec.and(DiscountSpecifications.startDateAfter(startDate));
        if (endDate != null) spec = spec.and(DiscountSpecifications.endDateBefore(endDate));

        spec = spec.and(DiscountSpecifications.isNotDeleted());
        return discountRepository.findAll(spec);
    }
}