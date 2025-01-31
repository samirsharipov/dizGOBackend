package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Language;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.entity.ProductTranslate;
import uz.pdp.springsecurity.entity.SelectedProducts;
import uz.pdp.springsecurity.enums.DiscountType;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.SelectedProductsGetDto;
import uz.pdp.springsecurity.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SelectedProductsService {
    private final SelectedProductsRepository repository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final LanguageRepository languageRepository;
    private final ProductTranslateRepository productTranslateRepository;
    private final DiscountRepository discountRepository;

    // 🔥 Universal createSelectedProducts (bitta yoki ro'yxatni bir xil metodda saqlash)
    public ApiResponse createSelectedProducts(List<SelectedProducts> selectedProductsList) {
        List<SelectedProducts> validProducts = new ArrayList<>();
        for (SelectedProducts selectedProduct : selectedProductsList) {
            if (validateSelectedProduct(selectedProduct)) {
                validProducts.add(selectedProduct);
            }
        }
        if (!validProducts.isEmpty()) {
            repository.saveAll(validProducts);
            return new ApiResponse("Successfully created selected products", true);
        }
        return new ApiResponse("No valid products found to save", false);
    }

    private boolean validateSelectedProduct(SelectedProducts selectedProduct) {
        boolean productExists = productRepository.existsById(selectedProduct.getProductId());
        if (!productExists) return false;

        boolean branchExists = branchRepository.existsById(selectedProduct.getBranchId());
        if (!branchExists) return false;

        return true;
    }

    // 🔥 Get selected product by ID
    public ApiResponse getSelectedProducts(UUID id, String languageCode) {
        return repository.findById(id)
                .map(selectedProduct -> {
                    Product product = productRepository.findById(selectedProduct.getProductId())
                            .orElseThrow(() -> new IllegalStateException("Product does not exist"));
                    Language language = languageCode != null
                            ? languageRepository.findByCode(languageCode).orElse(null)
                            : null;
                    SelectedProductsGetDto dto = convertToDto(selectedProduct, product, language);
                    return new ApiResponse("Successfully selected products", true, dto);
                })
                .orElse(new ApiResponse("Product does not exist", false));
    }

    // 🔥 Get products by branch ID
    public ApiResponse getSelectedProductsByBranchId(UUID branchId, String languageCode) {
        List<SelectedProducts> selectedProductsList = repository.findAllByBranchId(branchId);
        if (selectedProductsList.isEmpty()) return new ApiResponse("No products found for the branch", false);

        List<UUID> productIds = selectedProductsList.stream()
                .map(SelectedProducts::getProductId)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);
        Language language = languageCode != null
                ? languageRepository.findByCode(languageCode).orElse(null)
                : null;

        List<SelectedProductsGetDto> dtoList = new ArrayList<>();
        for (SelectedProducts selectedProduct : selectedProductsList) {
            products.stream()
                    .filter(product -> product.getId().equals(selectedProduct.getProductId()))
                    .findFirst()
                    .ifPresent(product -> dtoList.add(convertToDto(selectedProduct, product, language)));
        }
        if (dtoList.isEmpty()) {
            return new ApiResponse("No products found for the branch", false);
        }
        dtoList.stream()
                .filter(SelectedProductsGetDto::getDiscount)
                .forEach(selectedProductsGetDto -> updateDiscount(selectedProductsGetDto, branchId));

        return new ApiResponse("Successfully selected products", true, dtoList);
    }

    // 🔥 Yordamchi metod: SelectedProducts -> SelectedProductsGetDto
    private SelectedProductsGetDto convertToDto(SelectedProducts selectedProduct, Product product, Language language) {
        SelectedProductsGetDto dto = new SelectedProductsGetDto();
        dto.setSelectedProductId(selectedProduct.getId());
        dto.setId(product.getId());
        dto.setSalePrice(product.getSalePrice());
        dto.setBranchId(selectedProduct.getBranchId());
        dto.setBarcode(product.getBarcode());
        dto.setMXIKCode(product.getMXIKCode());
        dto.setDiscount(product.getDiscount());
        dto.setName(language != null
                ? getProductTranslatedName(product.getId(), language.getId(), product.getName())
                : product.getName());
        return dto;
    }

    private String getProductTranslatedName(UUID productId, UUID languageId, String productName) {
        return productTranslateRepository.findByProductIdAndLanguage_Id(productId, languageId)
                .map(ProductTranslate::getName)
                .orElse(productName);
    }

    // 🔥 Delete product by ID
    public ApiResponse delete(UUID id) {
        boolean exists = repository.existsById(id);
        if (!exists) return new ApiResponse("Product does not exist", false);

        repository.deleteById(id);
        return new ApiResponse("Successfully deleted selected product", true);
    }

    private void updateDiscount(SelectedProductsGetDto selectedProductsGetDto, UUID branchId) {
        LocalDate today = LocalDate.now();
        int dayNumber = today.getDayOfWeek().getValue(); // 1 (Dushanba) - 7 (Yakshanba)
        LocalTime now = LocalTime.now();

        discountRepository.findByProductIdAndBranchId(selectedProductsGetDto.getId(), branchId)
                .ifPresent(discount -> {

                    boolean matchesWeek = discount.isWeekday()
                            && discount.getWeekDays().contains(dayNumber);

                    boolean matchesTime = discount.isTime() &&
                            (!now.isBefore(discount.getStartHour().toLocalTime())
                                    && !now.isAfter(discount.getEndHour().toLocalTime()));

                    if (Boolean.FALSE.equals(matchesWeek) && Boolean.FALSE.equals(matchesTime)) {
                        selectedProductsGetDto.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                        selectedProductsGetDto.setDiscountValue(discount.getValue());
                    } else if (Boolean.TRUE.equals(matchesWeek)) {
                        if (Boolean.TRUE.equals(matchesTime)) {
                            selectedProductsGetDto.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                            selectedProductsGetDto.setDiscountValue(discount.getValue());
                        } else {
                            selectedProductsGetDto.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                            selectedProductsGetDto.setDiscountValue(discount.getValue());
                        }
                    } else if (Boolean.TRUE.equals(matchesTime)) {
                        selectedProductsGetDto.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                        selectedProductsGetDto.setDiscountValue(discount.getValue());
                    }
                });
    }
}