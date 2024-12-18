package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Language;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.entity.ProductTranslate;
import uz.pdp.springsecurity.entity.SelectedProducts;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.SelectedProductsGetDto;
import uz.pdp.springsecurity.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SelectedProductsService {
    private final SelectedProductsRepository repository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final LanguageRepository languageRepository;
    private final ProductTranslateRepository productTranslateRepository;

    public ApiResponse createSelectedProducts(SelectedProducts selectedProducts) {
        ApiResponse Product_does_not_exist = getApiResponse(selectedProducts);
        if (Product_does_not_exist != null) return Product_does_not_exist;
        repository.save(selectedProducts);
        return new ApiResponse("Successfully created selected products", true);
    }

    public ApiResponse updateSelectedProducts(UUID id, SelectedProducts selectedProducts) {
        Optional<SelectedProducts> optionalSelectedProducts = repository.findById(id);
        if (optionalSelectedProducts.isEmpty())
            return new ApiResponse("Product does not exist", false);

        ApiResponse Product_does_not_exist = getApiResponse(selectedProducts);
        if (Product_does_not_exist != null) return Product_does_not_exist;

        SelectedProducts selected = optionalSelectedProducts.get();
        selected.setBranchId(selectedProducts.getBranchId());
        selected.setProductId(selectedProducts.getProductId());
        repository.save(selected);
        return new ApiResponse("Successfully updated selected products", true);
    }

    @Nullable
    private ApiResponse getApiResponse(SelectedProducts selectedProducts) {
        boolean existsProduct = productRepository.existsById(selectedProducts.getProductId());
        if (!existsProduct)
            return new ApiResponse("Product does not exist", false);

        boolean existsBranch = branchRepository.existsById(selectedProducts.getBranchId());
        if (!existsBranch)
            return new ApiResponse("Branch does not exist", false);

        return null;
    }

    public ApiResponse getSelectedProducts(UUID id, String languageCode) {
        Optional<SelectedProducts> optionalSelectedProducts = repository.findById(id);
        if (optionalSelectedProducts.isEmpty())
            return new ApiResponse("Product does not exist", false);

        SelectedProducts selectedProducts = optionalSelectedProducts.get();
        SelectedProductsGetDto selectedProductsGetDto = new SelectedProductsGetDto();

        Optional<Product> optionalProduct = productRepository.findById(selectedProducts.getProductId());
        if (optionalProduct.isEmpty()) {
            return new ApiResponse("Product does not exist", false);
        }
        Product product = optionalProduct.get();

        selectedProductsGetDto.setId(selectedProducts.getId());
        selectedProductsGetDto.setProductId(product.getId());
        selectedProductsGetDto.setProductPrice(product.getSalePrice());
        selectedProductsGetDto.setBranchId(selectedProducts.getBranchId());

        if (languageCode != null) {
            Optional<Language> optionalLanguage = languageRepository.findByCode(languageCode);
            if (optionalLanguage.isPresent()) {
                Optional<ProductTranslate> optionalProductTranslate = productTranslateRepository.findByProductIdAndLanguage_Id(product.getId(), selectedProducts.getProductId());
                if (optionalProductTranslate.isPresent()) {
                    ProductTranslate productTranslate = optionalProductTranslate.get();
                    selectedProductsGetDto.setProductName(productTranslate.getName());
                }
            }
        } else {
            selectedProductsGetDto.setProductName(product.getName());
        }


        return new ApiResponse("Successfully selected products", true, selectedProductsGetDto);
    }

    public ApiResponse getSelectedProductsByBranchId(UUID branchId, String languageCode) {
        List<SelectedProducts> all = repository.findAllByBranchId(branchId);
        if (all.isEmpty()) {
            return new ApiResponse("Branch does not exist", false);
        }
        Optional<Language> optionalLanguage = Optional.empty();
        if (languageCode != null) {
            optionalLanguage = languageRepository.findByCode(languageCode);
        }

        List<SelectedProductsGetDto> selectedProductsGetDtoList = new ArrayList<>();
        for (SelectedProducts selectedProducts : all) {
            SelectedProductsGetDto selectedProductsGetDto = new SelectedProductsGetDto();
            Optional<Product> optionalProduct = productRepository.findById(selectedProducts.getProductId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                selectedProductsGetDto.setId(selectedProducts.getId());
                selectedProductsGetDto.setProductId(product.getId());
                selectedProductsGetDto.setProductPrice(product.getSalePrice());
                selectedProductsGetDto.setBranchId(selectedProducts.getBranchId());
                if (optionalLanguage.isPresent()) {
                    Language language = optionalLanguage.get();
                    Optional<ProductTranslate> optionalProductTranslate = productTranslateRepository.findByProductIdAndLanguage_Id(product.getId(), language.getId());
                    if (optionalProductTranslate.isPresent()) {
                        ProductTranslate productTranslate = optionalProductTranslate.get();
                        selectedProductsGetDto.setProductName(productTranslate.getName());
                    }
                } else {
                    selectedProductsGetDto.setProductName(product.getName());
                }
            }
            selectedProductsGetDtoList.add(selectedProductsGetDto);
        }


        return new ApiResponse("Successfully selected products", true, selectedProductsGetDtoList);
    }
}
