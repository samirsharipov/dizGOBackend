package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Discount;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.enums.DiscountType;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.DiscountRepository;
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
    private final ProductService productService;


    public ApiResponse createDiscount(DiscountDto discountDto) {
        Discount discount = new Discount();

        if (discountDto.getStartDate().toLocalDateTime().isBefore(LocalDate.now().atStartOfDay())) {
            return new ApiResponse("Sana bugungi kundan oldin bo'lishi mumkin emas!",false);
        }

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

        ApiResponse apiResponse = checkProductList(discountDto.getProductIds(), discountDto.getBranchIds());
        if (apiResponse.isSuccess()) {
            return new ApiResponse("Chegirmaga tushgan maxsulotlar mavjud!", false, apiResponse.getObject());
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

        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if (optionalDiscount.isEmpty()) {
            return new ApiResponse("Chegirma topildi", false);
        }

        Discount discount = optionalDiscount.get();
        discount.setValue(discountDetails.getValue());
        if (discountDetails.getStartDate() != null) discount.setStartDate(discountDetails.getStartDate());
        if (discountDetails.getEndDate() != null) discount.setEndDate(discountDetails.getEndDate());
        if (discountDetails.getWeekDays() != null && !discountDetails.getWeekDays().isEmpty())
            discount.setWeekDays(discountDetails.getWeekDays());

        if (discountDetails.getStartHour() != null && discountDetails.getEndHour() != null) {
            discount.setStartHour(discountDetails.getStartHour());
            discount.setEndHour(discountDetails.getEndHour());
            discount.setTime(true); // Soat bo‘yicha cheklovni faollashtirish
        } else {
            discount.setTime(false);
        }

        Set<Product> productSet = new HashSet<>();
        Set<Product> newProductSet = new HashSet<>();

        // eski mahsulotlarni ochirish
        if (discountDetails.getOldProductIds() != null && !discountDetails.getOldProductIds().isEmpty()) {
            List<Product> oldProducts = productRepository.findAllById(discountDetails.getOldProductIds());
            for (Product product : oldProducts) {
                product.setDiscount(false);
                productSet.add(product);
            }
        }

        //yangi mahsulotlarni qoshish
        if (discountDetails.getNewProductIds() != null && !discountDetails.getNewProductIds().isEmpty()) {
            List<Product> newProducts = productRepository.findAllById(discountDetails.getNewProductIds());
            for (Product product : newProducts) {
                product.setDiscount(true);
                productSet.add(product);
                newProductSet.add(product);
            }
        }

        if (!productSet.isEmpty()) {
            discount.getProducts().clear();
            discount.setProducts(new ArrayList<>(newProductSet));
            productRepository.saveAll(productSet);
        }
        discountRepository.save(discount);
        return new ApiResponse("Chegirma muvaffaqiyatli yangilandi", true);
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
        List<String> branchNames = discount.getBranches().stream()
                .map(Branch::getName)
                .toList();

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
                branchIds,
                branchNames
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

    public ApiResponse checkProductList(List<UUID> productIds, List<UUID> branchIds) {
        Set<UUID> productSet = new HashSet<>();

        for (UUID branchId : branchIds) {
            for (UUID productId : productIds) {
                if (discountRepository.existsByProductIdAndBranchId(productId, branchId)) {
                    productSet.add(productId);
                }
            }
        }

        List<Product> products = productRepository.findAllById(productSet);
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

        List<ProductGetDto> productGetDtoList = productService.getProductGetDtoList(products, code,branchId);
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