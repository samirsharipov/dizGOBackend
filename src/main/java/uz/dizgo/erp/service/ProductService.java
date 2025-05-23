package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.dizgo.erp.entity.*;
import uz.dizgo.erp.mapper.ProductMapper;
import uz.dizgo.erp.payload.*;
import uz.dizgo.erp.repository.*;
import uz.dizgo.erp.enums.DiscountType;
import uz.dizgo.erp.mapper.converts.ProductConvert;
import uz.dizgo.erp.repository.specifications.ProductSpecifications;
import uz.dizgo.erp.service.logger.ProductActivityLogger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final MeasurementRepository measurementRepository;
    private final AttachmentRepository attachmentRepository;
    private final BranchRepository branchRepository;
    private final BusinessRepository businessRepository;
    private final WarehouseRepository warehouseRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final TradeProductRepository tradeProductRepository;
    private final ContentProductRepository contentProductRepository;
    private final FifoCalculationRepository fifoCalculationRepository;
    private final LanguageRepository languageRepository;
    private final ProductTranslateRepository productTranslateRepository;
    private final ProductConvert productConvert;
    private final DiscountRepository discountRepository;
    private final MessageService messageService;
    private final ProductActivityLogger logger;

    @Transactional
    public ApiResponse editProduct(UUID productId, ProductEditDto productEditDto) {
        // Mahsulotni topish
        Measurement measurement = findByIdOrThrow(measurementRepository, productEditDto.getMeasurementId(), "Measurement");
        Product product = findByIdOrThrow(productRepository, productId, "Product");

        ProductDTO oldProduct = new ProductDTO();
        ProductDTO newProduct = new ProductDTO();

        ProductMapper.update(product, oldProduct);

        if (productEditDto.getBrandId() != null) {
            Brand brand = findByIdOrThrow(brandRepository, productEditDto.getBrandId(), "Brand");
            product.setBrand(brand);

        }
        if (productEditDto.getCategoryId() != null) {
            Category category = findByIdOrThrow(categoryRepository, productEditDto.getCategoryId(), "Category");
            product.setCategory(category);
        }


        // Mahsulotni yangilash
        product.setName(productEditDto.getName());
        product.setDescription(productEditDto.getDescription());
        product.setLongDescription(productEditDto.getLongDescription());
        product.setKeywords(productEditDto.getKeywords());
        product.setAttributes(productEditDto.getAttributes());
        product.setPluCode(productEditDto.getPluCode());
        product.setBuyPrice(productEditDto.getBuyPrice());
        product.setSalePrice(productEditDto.getSalePrice());
        product.setMXIKCode(productEditDto.getMXIKCode());
        product.setKpi(productEditDto.getKpi());
        product.setMinQuantity(productEditDto.getMinQuantity());


        validateUniqueBarcode(productEditDto.getBarcode(), productId, product.getBusiness().getId());

        oldProduct.setBarcode(product.getBarcode());
        product.setBarcode(productEditDto.getBarcode());
        newProduct.setBarcode(productEditDto.getBarcode());

        product.setMeasurement(measurement);

        // Foto ni yangilash
        if (productEditDto.getPhotoId() != null) {
            Attachment photo = findByIdOrThrow(attachmentRepository, productEditDto.getPhotoId(), "Photo");
            oldProduct.setPhoto(product.getPhoto().getId());
            product.setPhoto(photo);
            newProduct.setPhoto(photo.getId());
        }

        // Tarjimalarni yangilash
        saveProductTranslations(productEditDto.getTranslations(), product);


        // Mahsulotni saqlash
        productRepository.save(product);
        ProductMapper.update(product, newProduct);
        logger.logUpdate(productId, oldProduct, newProduct);

        return new ApiResponse("Product updated successfully", true);
    }

    @NotNull
    private ProductTranslate fromTranslateDto(ProductTranslateDTO productTranslateDTO, Product saved) {
        ProductTranslate productTranslate = new ProductTranslate();
        productTranslate.setName(productTranslateDTO.getName());
        productTranslate.setDescription(productTranslateDTO.getDescription());
        productTranslate.setLongDescription(productTranslateDTO.getLongDescription());
        productTranslate.setKeywords(productTranslateDTO.getKeywords());
        productTranslate.setAttributes(productTranslateDTO.getAttributes());
        productTranslate.setProduct(saved);
        Optional<Language> optionalLanguage = languageRepository.findById(productTranslateDTO.getLanguageId());
        optionalLanguage.ifPresent(productTranslate::setLanguage);
        return productTranslate;
    }

    private String generateBarcode(UUID businessId, String productName, UUID productId, boolean edit) {
        String name = productName.toLowerCase();
        StringBuilder str = new StringBuilder(String.valueOf(System.currentTimeMillis()));
        str.append(name.charAt(0));
        str.reverse();
        String barcode = str.substring(0, 9);
        if (edit) {
            if (productRepository.existsByBarcodeAndBusinessIdAndIdIsNotAndActiveTrue(barcode, businessId, productId))
                return generateBarcode(businessId, productName, productId, true);
        } else {
            if (productRepository.existsByBarcodeAndBusinessIdAndActiveTrue(barcode, businessId))
                return generateBarcode(businessId, productName, productId, false);
        }
        return barcode;
    }

    public ApiResponse getProduct(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        return optionalProduct.map(product -> {
            ProductGetDto productGetDto = productConvert.convertToDto(product, null, null);
            List<ProductTranslateDTO> translates = productTranslateRepository.findAllByProductId(product.getId())
                    .stream()
                    .map(this::productTranslateToDto)
                    .collect(Collectors.toList());
            if (!translates.isEmpty()) {
                productGetDto.setTranslates(translates);
            }
            return new ApiResponse("found", true, productGetDto);
        }).orElseGet(() -> new ApiResponse("not found", false));

    }

    @Transactional
    public ApiResponse deleteProduct(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(false);
            warehouseRepository.deleteAllByProductId(product.getId());
            fifoCalculationRepository.deleteAllByProductId(product.getId());
            productRepository.save(product);
            logger.logDelete(product);
            return new ApiResponse("DELETED", true);
        }
        return new ApiResponse("NOT FOUND", false);
    }

    public ApiResponse getByBarcode(String barcode, UUID branchId) {

        Branch branch = findByIdOrThrow(branchRepository, branchId, "branch");

        Branch mainBranch = branch.getMainBranchId() != null
                ? findByIdOrThrow(branchRepository, branch.getMainBranchId(), "branch")
                : branch;


        if (productRepository
                .existsByBarcodeAndBusinessId(barcode,
                        branch.getBusiness().getId())) {
            return new ApiResponse(messageService.getMessage("not.found"), false);
        }

        Optional<Product> optionalProduct = productRepository
                .findByBarcodeAndBusinessId(barcode, mainBranch.getBusiness().getId());

        return optionalProduct.map(product -> {
            ProductGetDto productGetDto = productConvert.convertToDto(product, null, null);

            List<ProductTranslateDTO> translates = productTranslateRepository.findAllByProductId(product.getId())
                    .stream()
                    .map(this::productTranslateToDto)
                    .collect(Collectors.toList());

            if (!translates.isEmpty()) {
                productGetDto.setTranslates(translates);
            }

            return new ApiResponse(messageService.getMessage("found"), true, productGetDto);
        }).orElseGet(() -> new ApiResponse(messageService.getMessage("not.found"), false));
    }

    public ApiResponse getByCategory(UUID category_id, String code) {

        List<ProductShortDto> productShortDto =
                productRepository.findAllByCategoryIdAndActiveOrderByNameAsc(category_id, code);

        if (productShortDto.isEmpty()) {
            return new ApiResponse(messageService.getMessage("not.found"), false);
        }

        return new ApiResponse(messageService.getMessage("found"), true, productShortDto);
    }

    public ApiResponse getByBrand(UUID brand_id, String code) {
        List<ProductShortDto> productShortDtoList = productRepository.findAllByBrandIdAndActiveOrderByNameAsc(brand_id, code);
        if (productShortDtoList.isEmpty())
            return new ApiResponse("NOT FOUND", false);

        return new ApiResponse("FOUND", true, productShortDtoList);
    }

    @NotNull
    public List<ProductGetDto> getProductGetDtoList(List<Product> products, String code, UUID branchId) {

        Language language = languageRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("LANGUAGE NOT FOUND"));

        return products.stream()
                .map(product -> {
                    ProductGetDto productGetDto = productConvert.convertToDto(product, branchId, code);

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
    }

    public ApiResponse getByBranchAndBarcode(UUID branch_id, User user, ProductBarcodeDto barcodeDto) {
        Set<Branch> branches = user.getBranches();
        for (Branch branch : branches) {
            if (branch.getId().equals(branch_id)) {
                return new ApiResponse("BRANCH NOT FOUND OR NOT ALLOWED", false);
            }
        }
        List<Product> productList = productRepository.findAllByBranchIdAndBarcodeOrNameAndActiveTrue(branch_id, barcodeDto.getBarcode(), barcodeDto.getName());

        if (productList.isEmpty()) {
            return new ApiResponse("PRODUCT NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, productList);


    }

    public ApiResponse getByBranch(UUID branch_id) {
        return getProductByBranch(branch_id);

    }

    public ApiResponse getByBranchForSearch(UUID branch_id) {

        List<ProductGetForPurchaseDto> getForPurchaseDtoList = new ArrayList<>();
        List<Product> productList = productRepository.findAllByBranchIdAndActiveIsTrue(branch_id);
        if (productList.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        } else {
            toViewDtoMto(branch_id, getForPurchaseDtoList, productList);
            return new ApiResponse("FOUND", true, getForPurchaseDtoList);
        }
    }

    private void toViewDtoMto(UUID branch_id, List<ProductGetForPurchaseDto> getForPurchaseDtoList, List<Product> productList) {
        for (Product product : productList) {
            if (product.getCategory() != null) {
                ProductGetForPurchaseDto getForPurchaseDto = new ProductGetForPurchaseDto();
                getForPurchaseDto.setBusinessCheapSellingPrice(product.getBusiness().isCheapSellingPrice());
                getForPurchaseDto.setProductId(product.getId());
                getForPurchaseDto.setName(product.getName());
                getForPurchaseDto.setBarcode(product.getBarcode());
                getForPurchaseDto.setBuyPrice(product.getBuyPrice());
                getForPurchaseDto.setSalePrice(product.getSalePrice());
                getForPurchaseDto.setBuyDollar(product.isBuyDollar());
                getForPurchaseDto.setSaleDollar(product.isSaleDollar());
                getForPurchaseDto.setBuyPriceDollar(product.getSalePriceDollar());
                getForPurchaseDto.setSalePriceDollar(product.getSalePriceDollar());
                getForPurchaseDto.setGrossPrice(product.getGrossPrice());
                getForPurchaseDto.setGrossPriceDollar(product.getGrossPriceDollar());
                getForPurchaseDto.setMinQuantity(product.getMinQuantity());
                getForPurchaseDto.setExpiredDate(product.getExpireDate());
                getForPurchaseDto.setGrossPriceMyControl(product.getGrossPriceMyControl());
                getForPurchaseDto.setGrossPricePermission(businessRepository.getById(product.getBusiness().getId()).isGrossPriceControl());
                getForPurchaseDto.setCategoryId(product.getCategory().getId());
                if (product.getMeasurement() != null) {
                    getForPurchaseDto.setMeasurementName(product.getMeasurement().getName());
                }
                if (product.getBrand() != null) getForPurchaseDto.setBrandName(product.getBrand().getName());
                if (product.getPhoto() != null) getForPurchaseDto.setPhotoId(product.getPhoto().getId());
                Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branch_id, product.getId());
                getForPurchaseDto.setAmount(optionalWarehouse.map(Warehouse::getAmount).orElse(0d));
                getForPurchaseDtoList.add(getForPurchaseDto);
            }
        }
    }


    public ApiResponse getByBusiness(UUID businessId, UUID branch_id, UUID brand_id, UUID categoryId) {
        List<ProductViewDto> productViewDtoList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();

        boolean checkingBranch = false;
        boolean checkingBrand = false;
        boolean checkingCategory = false;
        boolean checkingBusiness = false;

        if (categoryId != null) {
            checkingCategory = true;
        }
        if (brand_id != null) {
            checkingBrand = true;
        }
        if (branch_id != null) {
            checkingBranch = true;
        }
        if (businessId != null) {
            checkingBusiness = true;
        }
        if (checkingBranch) {
            checkingBusiness = false;
        }

        if (checkingCategory && checkingBrand && checkingBranch) {
            productList = productRepository.findAllByBrandIdAndCategoryIdAndBranchIdAndActiveTrue(brand_id, categoryId, branch_id);
        } else if (checkingBrand && checkingBranch) {
            productList = productRepository.findAllByBrandIdAndBranchIdAndActiveTrue(brand_id, branch_id);
        } else if (checkingCategory && checkingBranch) {
            productList = productRepository.findAllByCategoryIdAndBranchIdAndActiveTrue(categoryId, branch_id);
        } else if (checkingCategory && checkingBrand && checkingBusiness) {
            productList = productRepository.findAllByBrandIdAndCategoryIdAndBusinessIdAndActiveTrue(brand_id, categoryId, businessId);
        } else if (checkingBrand && checkingBusiness) {
            productList = productRepository.findAllByBrandIdAndBusinessIdAndActiveTrue(brand_id, businessId);
        } else if (checkingCategory && checkingBusiness) {
            productList = productRepository.findAllByCategoryIdAndBusinessIdAndActiveTrue(categoryId, businessId);
        } else if (checkingBusiness) {
            productList = productRepository.findAllByBusiness_IdAndActiveTrue(businessId);
        } else if (checkingBranch) {
            productList = productRepository.findAllByBranchIdAndActiveTrue(branch_id);
        }
        if (checkingBranch) {
            getProductMethod(productViewDtoList, productList, branch_id);
        } else {
            getProductMethod(productViewDtoList, productList, null);
        }


        if (productList.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }

        return new ApiResponse("FOUND", true, productViewDtoList);
    }

    public ApiResponse getByBusinessPageableWithTranslations(UUID businessId, UUID branchId, UUID brandId, UUID catId, String search, int page, int size, String lang) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");

        Page<Product> productPage = null;
        if (!search.isBlank()) {
            if (branchId == null) {
                productPage = productRepository.findByBusinessIdAndNameContainingIgnoreCase(businessId, search, pageable);
            } else {
                productPage = productRepository.findByBranchIdAndNameContainingIgnoreCase(branchId, search, pageable);
            }
        } else {
            // Dinamik filtrlash
            Specification<Product> spec = Specification
                    .where(ProductSpecifications.isActiveTrue())
                    .and(branchId != null ? ProductSpecifications.belongsToBranch(branchId) : ProductSpecifications.belongsToBusiness(businessId))
                    .and(catId != null ? ProductSpecifications.belongsToCategory(catId) : null)
                    .and(brandId != null ? ProductSpecifications.belongsToBrand(brandId) : null);
            productPage = productRepository.findAll(spec, pageable);
        }


        List<ProductGetDto> productViewDtoList = productPage.stream()
                .map(product -> {
                    ProductGetDto dto = productConvert.convertToDto(product, branchId, lang);
                    addTranslationToDto(dto, product, lang);
                    return dto;
                })
                .toList();

        if (productViewDtoList.isEmpty()) {
            return new ApiResponse("MA'LUMOT TOPILMADI", false);
        }

        return new ApiResponse(true, Map.of(
                "product_list", productViewDtoList,
                "currentPage", productPage.getNumber(),
                "totalPages", productPage.getTotalPages(),
                "totalItems", productPage.getTotalElements()
        ));
    }

    private void addTranslationToDto(ProductGetDto dto, Product product, String lang) {
        if (product.getTranslations() != null && lang != null) {
            product.getTranslations().stream()
                    .filter(translation -> lang.equalsIgnoreCase(translation.getLanguage().getCode()))
                    .findFirst()
                    .ifPresent(translation -> {
                        dto.setName(translation.getName() != null ? translation.getName() : dto.getName());
                        dto.setDescription(translation.getDescription() != null ? translation.getDescription() : dto.getDescription());
                        dto.setLongDescription(translation.getLongDescription() != null ? translation.getLongDescription() : dto.getLongDescription());
                        dto.setAttributes(translation.getAttributes() != null ? translation.getAttributes() : dto.getAttributes());
                    });
        }
    }

    public ApiResponse getByBranchProduct(UUID branchId) {
        return getProductByBranch(branchId);
    }

    @Transactional
    public ApiResponse deleteProducts(ProductIdsDto productIdsDto) {
        List<UUID> ids = productIdsDto.getIds();
        for (UUID id : ids) {
            Optional<Product> optional = productRepository.findById(id);
            if (optional.isPresent()) {
                Product product = optional.get();
                product.setActive(false);
                warehouseRepository.deleteAllByProductId(product.getId());
                fifoCalculationRepository.deleteAllByProductId(product.getId());
                productRepository.save(product);
                logger.logDelete(product);
            }
        }
        return new ApiResponse("DELETED", true);
    }


    private ApiResponse getProductByBranch(UUID branchId) {
        List<ProductViewDto> productViewDtoList = new ArrayList<>();
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);

        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found");
        }
        List<Product> productList = productRepository.findAllByBranchIdAndActiveIsTrue(branchId);
        getProductMethod(productViewDtoList, productList, branchId);
        return new ApiResponse("FOUND", true, productViewDtoList);
    }

    private void getProductMethod(List<ProductViewDto> productViewDtoList, List<Product> productList, UUID branchId) {
        Double amountD;
        double amount;
        for (Product product : productList) {
            ProductViewDto productViewDto = new ProductViewDto();

            productViewDto.setUniqueSKU(product.getUniqueSKU());
            productViewDto.setStockAmount(product.getStockAmount());
            productViewDto.setInStock(product.getInStock());
            productViewDto.setPreorder(product.getPreorder());
            productViewDto.setLength(product.getLength());
            productViewDto.setWidth(product.getWidth());
            productViewDto.setHeight(product.getHeight());
            productViewDto.setWeight(product.getWeight());
            productViewDto.setHsCode12(product.getHsCode12());
            productViewDto.setHsCode22(product.getHsCode22());
            productViewDto.setHsCode32(product.getHsCode32());
            productViewDto.setHsCode44(product.getHsCode44());

            productViewDto.setLongDescription(product.getLongDescription());
            productViewDto.setAgreementExportsID(product.getAgreementExportsID());
            productViewDto.setAgreementExportsPID(product.getAgreementExportsPID());
            productViewDto.setAgreementLocalID(product.getAgreementLocalID());
            productViewDto.setAgreementLocalPID(product.getAgreementLocalPID());
            productViewDto.setLangGroup(product.getLangGroup());
            productViewDto.setShippingClass(product.getShippingClass());
            productViewDto.setAttributes(product.getAttributes());

            productViewDto.setProductId(product.getId());
            productViewDto.setProductName(product.getName());
            productViewDto.setBarcode(product.getBarcode());
            productViewDto.setKpi(product.getKpi());
            productViewDto.setMinQuantity(product.getMinQuantity());
            productViewDto.setBuyDollar(product.isBuyDollar());
            productViewDto.setSaleDollar(product.isSaleDollar());
            productViewDto.setBranch(product.getBranch());
            productViewDto.setExpiredDate(product.getExpireDate());

            if (product.getBrand() != null)
                productViewDto.setBrandName(product.getBrand().getName());

            if (product.getCategory() != null)
                productViewDto.setCategory(product.getCategory().getName());

            if (product.getPhoto() != null)
                productViewDto.setPhotoId(product.getPhoto().getId());

            if (product.getMeasurement() != null) {
                productViewDto.setMeasurementId(product.getMeasurement().getName());
            }

            productViewDto.setRastas(product.getRastaList());
            productViewDto.setBuyPrice(product.getBuyPrice());
            productViewDto.setSalePrice(product.getSalePrice());
            productViewDto.setBuyPriceDollar(product.getBuyPriceDollar());
            productViewDto.setSalePriceDollar(product.getSalePriceDollar());
            productViewDto.setGrossPrice(product.getGrossPrice());
            productViewDto.setGrossPriceDollar(product.getGrossPriceDollar());
            productViewDto.setGrossPriceControl(product.getGrossPriceMyControl());
            productViewDto.setGrossPricePermission(businessRepository.findById(product.getBusiness().getId()).get().isGrossPriceControl());

            if (branchId != null)
                amountD = warehouseRepository.amountByProductSingleAndBranchId(product.getId(), branchId);
            else
                amountD = warehouseRepository.amountByProductSingle(product.getId());
            amount = amountD == null ? 0 : amountD;
            productViewDto.setAmount(amount);
            productViewDtoList.add(productViewDto);
        }


    }

    public ApiResponse getPurchaseProduct(UUID branchId, UUID productId, int page, int size) {
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NOT FOUND", false);
        Pageable pageable = PageRequest.of(page, size);
        Page<PurchaseProduct> purchaseProductPage;
        if (productRepository.existsById(productId)) {
            purchaseProductPage = purchaseProductRepository.findAllByPurchase_BranchIdAndProductIdOrderByCreatedAtDesc(branchId, productId, pageable);
        } else {
            return new ApiResponse("PRODUCT NOT FOUND", false);
        }
        if (purchaseProductPage.isEmpty()) return new ApiResponse("LIST EMPTY", false);

        List<ProductHistoryDto> productHistoryDtoList = new ArrayList<>();
        for (PurchaseProduct purchaseProduct : purchaseProductPage.getContent()) {
            productHistoryDtoList.add(new ProductHistoryDto(
                    purchaseProduct.getProduct().getName(),
                    purchaseProduct.getPurchasedQuantity(),
                    purchaseProduct.getTotalSum(),
                    purchaseProduct.getCreatedAt(),
                    purchaseProduct.getPurchase().getSeller().getFirstName(),
                    purchaseProduct.getPurchase().getSeller().getLastName(),
                    purchaseProduct.getPurchase().getSupplier().getName()
            ));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("productHistoryDtoList", productHistoryDtoList);
        response.put("currentPage", purchaseProductPage.getNumber());
        response.put("totalItem", purchaseProductPage.getTotalElements());
        response.put("totalPage", purchaseProductPage.getTotalPages());
        return new ApiResponse("all", true, response);
    }

    public ApiResponse getProductionProduct(UUID branchId, UUID productId, int page, int size) {
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NOT FOUND", false);
        Pageable pageable = PageRequest.of(page, size);
        Page<FifoCalculation> fifoCalculationPage;
        if (productRepository.existsById(productId)) {
            fifoCalculationPage = fifoCalculationRepository.findAllByBranchIdAndProductIdAndProductionIsNotNullOrderByCreatedAtDesc(branchId, productId, pageable);
        } else {
            return new ApiResponse("PRODUCT NOT FOUND", false);
        }
        if (fifoCalculationPage.isEmpty()) return new ApiResponse("LIST EMPTY", false);

        List<ProductHistoryDto> productHistoryDtoList = new ArrayList<>();
        for (FifoCalculation fifoCalculation : fifoCalculationPage.getContent()) {
            ProductHistoryDto productHistoryDto = new ProductHistoryDto(
                    fifoCalculation.getProduct().getName(),
                    fifoCalculation.getPurchasedAmount(),
                    0,
                    fifoCalculation.getCreatedAt()
            );
            if (fifoCalculation.getProduct().equals(fifoCalculation.getProduction().getProduct()))
                productHistoryDto.setSumma(fifoCalculation.getProduction().getTotalPrice());
            else
                productHistoryDto.setSumma(fifoCalculation.getProduct().getSalePrice() * fifoCalculation.getPurchasedAmount());
            productHistoryDtoList.add(productHistoryDto);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("productHistoryDtoList", productHistoryDtoList);
        response.put("currentPage", fifoCalculationPage.getNumber());
        response.put("totalItem", fifoCalculationPage.getTotalElements());
        response.put("totalPage", fifoCalculationPage.getTotalPages());
        return new ApiResponse("all", true, response);
    }


    public ApiResponse getTradeProduct(UUID branchId, UUID productId, int page, int size) {
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NOT FOUND", false);
        Pageable pageable = PageRequest.of(page, size);
        Page<TradeProduct> tradeProductPage;
        if (productRepository.existsById(productId)) {
            tradeProductPage = tradeProductRepository.findAllByTrade_BranchIdAndProductIdOrderByCreatedAtDesc(branchId, productId, pageable);
        } else {
            return new ApiResponse("PRODUCT NOT FOUND", false);
        }
        if (tradeProductPage.isEmpty()) return new ApiResponse("LIST EMPTY", false);

        List<ProductHistoryDto> productHistoryDtoList = new ArrayList<>();
        for (TradeProduct tradeProduct : tradeProductPage.getContent()) {
            productHistoryDtoList.add(new ProductHistoryDto(
                    tradeProduct.getProduct().getName(),
                    tradeProduct.getTradedQuantity(),
                    tradeProduct.getTotalSalePrice(),
                    tradeProduct.getCreatedAt(),
                    tradeProduct.getTrade().getTrader().getFirstName(),
                    tradeProduct.getTrade().getTrader().getLastName(),
                    tradeProduct.getTrade().getCustomer() == null ? "" : tradeProduct.getTrade().getCustomer().getName()
            ));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("productHistoryDtoList", productHistoryDtoList);
        response.put("currentPage", tradeProductPage.getNumber());
        response.put("totalItem", tradeProductPage.getTotalElements());
        response.put("totalPage", tradeProductPage.getTotalPages());
        return new ApiResponse("all", true, response);
    }

    public ApiResponse getContentProduct(UUID branchId, UUID productId, int page, int size) {
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NOT FOUND", false);
        Pageable pageable = PageRequest.of(page, size);
        Page<ContentProduct> contentProductPage;
        if (productRepository.existsById(productId)) {
            contentProductPage = contentProductRepository.findAllByProduction_BranchIdAndProductIdAndProductionIsNotNullAndByProductIsFalseAndLossProductIsFalseOrderByCreatedAtDesc(branchId, productId, pageable);
        } else {
            return new ApiResponse("PRODUCT NOT FOUND", false);
        }
        if (contentProductPage.isEmpty()) return new ApiResponse("LIST EMPTY", false);

        List<ProductHistoryDto> productHistoryDtoList = new ArrayList<>();
        for (ContentProduct contentProduct : contentProductPage.getContent()) {
            productHistoryDtoList.add(new ProductHistoryDto(
                    contentProduct.getProduct().getName(),
                    contentProduct.getQuantity(),
                    contentProduct.getTotalPrice(),
                    contentProduct.getCreatedAt()
            ));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("productHistoryDtoList", productHistoryDtoList);
        response.put("currentPage", contentProductPage.getNumber());
        response.put("totalItem", contentProductPage.getTotalElements());
        response.put("totalPage", contentProductPage.getTotalPages());
        return new ApiResponse("all", true, response);
    }

    public void editPriceAccordingToDollar(UUID businessId, double course) {
        List<Product> productListBuy = productRepository.findAllByBusinessIdAndActiveTrueAndBuyDollarTrueOrSaleDollarTrue(businessId);
        editPriceHelper(productListBuy, course);
    }

    private void editPriceHelper(List<Product> productList, double course) {
        for (Product product : productList) {
            product.setSalePrice(course * product.getSalePriceDollar());
        }
        productRepository.saveAll(productList);
    }


    public ApiResponse getByBranchForTrade(String searchValue, UUID branchId, UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> all;
//        productRepository.findAllByBranchIdAndNameContainingIgnoreCaseOrBranchIdAndBarcodeContainingIgnoreCaseOrBranchIdAndCategoryIdAndActiveTrue(branchId, searchValue, branchId, searchValue, branchId, categoryId, pageable);

        if (searchValue != null && branchId != null) {
            all = productRepository.findAllByBranch_IdAndNameContainingIgnoreCaseAndActiveTrueOrBusinessIdAndBarcodeContainingIgnoreCaseAndActiveTrue(branchId, searchValue, branchId, searchValue, pageable);
        } else if (categoryId != null && branchId != null) {
            all = productRepository.findAllByCategory_IdAndBranch_IdAndActiveTrue(categoryId, branchId, pageable);
        } else {
            all = productRepository.findAllByBranch_IdAndActiveIsTrue(branchId, pageable);
        }

        if (all.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        List<ProductGetForPurchaseDto> getForPurchaseDtoList = new ArrayList<>();

        toViewDtoMto(branchId, getForPurchaseDtoList, all.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("productHistoryDtoList", getForPurchaseDtoList);
        response.put("currentPage", all.getNumber());
        response.put("totalItem", getForPurchaseDtoList.size());
        response.put("totalPage", getForPurchaseDtoList.size() / size);

        return new ApiResponse("all", true, response);
    }

    public ApiResponse search(UUID branchId, String name, String code) {


        Branch branch = findByIdOrThrow(branchRepository, branchId, "branch");
        UUID mainBranchBusinessId = branch.getMainBranchId() != null
                ? findByIdOrThrow(branchRepository, branch.getMainBranchId(), "branch").getBusiness().getId()
                : branch.getBusiness().getId();

        List<Product> products = productRepository.findAllProductsWithTranslates(branch.getBusiness().getId(), name);
        if (products.isEmpty()) {
            products = productRepository.findAllProductsWithTranslates(mainBranchBusinessId, name);
        }

        if (products.isEmpty()) {
            return new ApiResponse(messageService.getMessage("not.found"), false);
        }
        List<ProductGetDto> productGetDtoList = getProductGetDtoList(products, code, branchId);

        return new ApiResponse(messageService.getMessage("found"), true, productGetDtoList);
    }

    @Transactional
    public ApiResponse createProduct(ProductPostDto productPostDto) {

        Business business = findByIdOrThrow(businessRepository, productPostDto.getBusinessId(), "Business");
        Measurement measurement = findByIdOrThrow(measurementRepository, productPostDto.getMeasurementId(), "Measurement");

        Product product = buildProduct(productPostDto, business, measurement);

        if (productPostDto.getBrandId() != null) {
            Brand brand = findByIdOrThrow(brandRepository, productPostDto.getBrandId(), "Brand");
            product.setBrand(brand);
        }
        if (productPostDto.getCategoryId() != null) {
            Category category = findByIdOrThrow(categoryRepository, productPostDto.getCategoryId(), "Category");
            product.setCategory(category);
        }

        validateUniqueBarcode(productPostDto.getBarcode(), null, productPostDto.getBusinessId());


        // Branchlarni o'rnatish
        if (productPostDto.getIsGlobal()) {
            setGlobalBranches(productPostDto, product);
        } else {
            setBusinessBranches(productPostDto, product, business);
        }

        // Maxsulot tarjimalarini saqlash
        saveProductTranslations(productPostDto.getTranslations(), product);

        productRepository.save(product);  // Yangi maxsulotni saqlash
        logger.logCreate(product);

        return new ApiResponse("success", true);
    }

    private Product buildProduct(ProductPostDto productPostDto, Business business, Measurement measurement) {
        Product product = new Product();
        setProductFields(product,
                productPostDto.getName(), productPostDto.getDescription(), productPostDto.getLongDescription(),
                productPostDto.getKeywords(), productPostDto.getAttributes(), productPostDto.getPluCode(),
                productPostDto.getBarcode(), productPostDto.getMXIKCode(), productPostDto.getAgreementExportsID(),
                productPostDto.getAgreementExportsPID(), productPostDto.getAgreementLocalID(), productPostDto.getAgreementLocalPID(),
                productPostDto.getHsCode12(), productPostDto.getHsCode22(), productPostDto.getHsCode32(),
                productPostDto.getHsCode44(), productPostDto.getUniqueSKU(), productPostDto.getLength(),
                productPostDto.getWidth(), productPostDto.getHeight(), productPostDto.getWeight(),
                productPostDto.getShippingClass());

        product.setMeasurement(measurement);

        product.setBusiness(business);
        product.setClone(productPostDto.isClone());

        // Fotoni o'rnatish
        if (productPostDto.getPhotoId() != null) {
            Attachment photo = findByIdOrThrow(attachmentRepository, productPostDto.getPhotoId(), "Photo");
            product.setPhoto(photo);
        }

        product.setIsGlobal(productPostDto.getIsGlobal());
        return product;
    }

    private void setGlobalBranches(ProductPostDto productPostDto, Product product) {
        if (productPostDto.getBranchIds() != null) {
            List<Branch> branches = productPostDto.getBranchIds().stream()
                    .map(branchId -> findByIdOrThrow(branchRepository, branchId, "Branch"))
                    .collect(Collectors.toList());
            product.setBranch(branches);
        }
    }

    private void setBusinessBranches(ProductPostDto productPostDto, Product product, Business business) {
        List<Branch> branches = branchRepository.findAllByBusiness_Id(business.getId());
        product.setBranch(branches);

        // KPI va boshqa parametrlarni o'rnatish
        product.setKpiPercent(productPostDto.getKpiPercent());
        product.setKpi(productPostDto.getKpi());
        product.setMinQuantity(productPostDto.getMinQuantity());
    }

    @Transactional
    public void saveProductTranslations(List<ProductTranslateDTO> productTranslateDTOList, Product product) {
        // Eski tarjimalarni o'chirish
        productTranslateRepository.deleteAllByProductId(product.getId());

        if (productTranslateDTOList != null) {
            productTranslateDTOList.forEach(translation -> {
                ProductTranslate productTranslate = fromTranslateDto(translation, product);
                productTranslateRepository.save(productTranslate);
            });
        }
    }

    public static <T> T findByIdOrThrow(JpaRepository<T, UUID> repository, UUID id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(entityName + " not found"));
    }

    @Transactional
    public ApiResponse editProductMain(UUID productId, ProductEditMainDto productEditMainDto) {
        // Mahsulotni ID bo'yicha topish yoki xatolik yuborish
        Product product = findByIdOrThrow(productRepository, productId, "Product");
        // Measurement, Brand va Category-ni yangilash

        ProductDTO oldProduct = new ProductDTO();
        ProductDTO newProduct = new ProductDTO();
        ProductMapper.update(product, oldProduct);

        Measurement measurement = new Measurement();
        if (productEditMainDto.getMeasurementId() != null) {
            measurement = findByIdOrThrow(measurementRepository, productEditMainDto.getMeasurementId(), "Measurement");
        }

        Brand brand = new Brand();
        if (productEditMainDto.getBrandId() != null) {
            brand = findByIdOrThrow(brandRepository, productEditMainDto.getBrandId(), "Brand");
        }

        Category category = new Category();
        if (productEditMainDto.getCategoryId() != null) {
            category = findByIdOrThrow(categoryRepository, productEditMainDto.getCategoryId(), "Category");
        }

        String barcode = product.getBarcode();
        validateUniqueBarcode(productEditMainDto.getBarcode(), productId, product.getBusiness().getId());

        // Barcode bir xil bo'lgan mahsulotlarni olish
        List<Product> productsWithSameBarcode = productRepository.findAllByBarcode(barcode);
        productsWithSameBarcode.forEach(p -> {
            //o'zgarishlarni kiritish
            editorProduct(productEditMainDto, p, p.getMeasurement(), p.getCategory(), p.getBrand());
            saveProductTranslations(productEditMainDto, p);
        });


        editorProduct(productEditMainDto, product, measurement, category, brand);


        // Fotoni yangilash
        if (productEditMainDto.getPhotoId() != null) {
            Attachment photo = findByIdOrThrow(attachmentRepository, productEditMainDto.getPhotoId(), "Photo");
            product.setPhoto(photo);
        }

        // Filiallarni yangilash
        if (productEditMainDto.getBranchIds() != null) {
            List<Branch> branches = productEditMainDto.getBranchIds().stream()
                    .map(branchId -> findByIdOrThrow(branchRepository, branchId, "Branch"))
                    .collect(Collectors.toList());
            product.setBranch(branches);
        }

        // Tarjimalarni saqlash
        saveProductTranslations(productEditMainDto, product);

        // Yangilangan mahsulotni saqlash
        productRepository.save(product);
        productRepository.saveAll(productsWithSameBarcode);

        ProductMapper.update(product, newProduct);
        logger.logUpdate(productId, oldProduct, newProduct);

        return new ApiResponse("Product updated successfully", true);
    }

    private static void editorProduct(ProductEditMainDto productEditMainDto, Product product, Measurement measurement, Category category, Brand brand) {
        setProductFields(product,
                productEditMainDto.getName(), productEditMainDto.getDescription(), productEditMainDto.getLongDescription(),
                productEditMainDto.getKeywords(), productEditMainDto.getAttributes(), productEditMainDto.getPluCode(),
                productEditMainDto.getBarcode(), productEditMainDto.getMXIKCode(), productEditMainDto.getAgreementExportsID(),
                productEditMainDto.getAgreementExportsPID(), productEditMainDto.getAgreementLocalID(), productEditMainDto.getAgreementLocalPID(),
                productEditMainDto.getHsCode12(), productEditMainDto.getHsCode22(), productEditMainDto.getHsCode32(),
                productEditMainDto.getHsCode44(), productEditMainDto.getUniqueSKU(), productEditMainDto.getLength(),
                productEditMainDto.getWidth(), productEditMainDto.getHeight(), productEditMainDto.getWeight(),
                productEditMainDto.getShippingClass());

        product.setMeasurement(measurement);
        product.setCategory(category);
        product.setBrand(brand);
    }

    private static void setProductFields(Product product,
                                         String name, String description, String longDescription, String keywords, String attributes,
                                         String pluCode, String barcode,
                                         String mxikCode, String agreementExportsID, String agreementExportsPID,
                                         String agreementLocalID, String agreementLocalPID,
                                         String hsCode12, String hsCode22, String hsCode32, String hsCode44,
                                         String uniqueSKU, Double length, Double width, Double height,
                                         Double weight, String shippingClass) {

        // Asosiy maydonlarni o'rnatish
        product.setName(name);
        product.setDescription(description);
        product.setLongDescription(longDescription);
        product.setKeywords(keywords);
        product.setAttributes(attributes);
        product.setPluCode(pluCode);


        product.setBarcode(barcode);

        // Qo'shimcha maydonlarni o'rnatish
        product.setMXIKCode(mxikCode);
        product.setAgreementExportsID(agreementExportsID);
        product.setAgreementExportsPID(agreementExportsPID);
        product.setAgreementLocalID(agreementLocalID);
        product.setAgreementLocalPID(agreementLocalPID);
        product.setHsCode12(hsCode12);
        product.setHsCode22(hsCode22);
        product.setHsCode32(hsCode32);
        product.setHsCode44(hsCode44);
        product.setUniqueSKU(uniqueSKU);
        product.setLength(length);
        product.setWidth(width);
        product.setHeight(height);
        product.setWeight(weight);
        product.setShippingClass(shippingClass);
    }

    // Tarjimalarni saqlash funksiyasi
    @Transactional
    public void saveProductTranslations(ProductEditMainDto productEditMainDto, Product product) {
        // Eski tarjimalarni o'chirish
        productTranslateRepository.deleteAllByProductId(product.getId());

        // Yangi tarjimalarni saqlash
        productEditMainDto.getTranslations().forEach(translation -> {
            ProductTranslate productTranslate = fromTranslateDto(translation, product);
            productTranslateRepository.save(productTranslate);
        });
    }

    public void validateUniqueBarcode(String barcode, UUID productId, UUID businessId) {
        // Barcode yaratishda yoki boshqa mahsulotda mavjudligini tekshirish
        Optional<Product> existingProduct = productRepository.findByBarcodeAndBusinessId(barcode, businessId);
        if (existingProduct.isPresent() && !existingProduct.get().getId().equals(productId)) {
            throw new IllegalArgumentException("Ushbu barcode bilan mahsulot allaqachon mavjud!");
        }
    }


    private ProductTranslateDTO productTranslateToDto(ProductTranslate productTranslate) {
        ProductTranslateDTO translateDTO = new ProductTranslateDTO();
        translateDTO.setId(productTranslate.getId());
        translateDTO.setName(productTranslate.getName());
        translateDTO.setDescription(productTranslate.getDescription());
        translateDTO.setLongDescription(productTranslate.getLongDescription());
        translateDTO.setKeywords(productTranslate.getKeywords());
        translateDTO.setAttributes(productTranslate.getAttributes());
        translateDTO.setLanguageCode(productTranslate.getLanguage().getCode());
        translateDTO.setLanguageId(productTranslate.getLanguage().getId());
        translateDTO.setLanguageName(productTranslate.getLanguage().getName());
        return translateDTO;
    }

    public ApiResponse searchTrade(UUID branchId, String name, String language) {

        if (name == null || name.isBlank()) {
            return new ApiResponse(messageService.getMessage("product.not.found"), false);
        }

        List<ProductResponseDTO> all = findProductByKeyword(branchId, name, language);

        // 4️⃣ Mahsulot topilmasa, barcode'dan keyin 'totalKg'ni aniqlashga harakat qilamiz
        if (all.isEmpty()) {
            try {
                int barcode = Integer.parseInt(name.substring(1, 7)); // barcode o'rni: 1-6
                double totalKg = Double.parseDouble(name.substring(7, name.length() - 1)) / 1000; // 7-x dan oxirigacha
                all = findProductByKeyword(branchId, String.valueOf(barcode), language);
                for (ProductResponseDTO product : all) {
                    product.setAmount(totalKg);
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                return new ApiResponse(messageService.getMessage("error.message"), false);
            }
        }

        if (all.isEmpty()) {
            return new ApiResponse(messageService.getMessage("product.not.found"), false);
        }

        all.stream()
                .filter(productResponseDTO -> Boolean.TRUE.equals(productResponseDTO.getDiscount()))
                .forEach(productResponseDTO -> updateDiscount(productResponseDTO, branchId));

        return new ApiResponse(messageService.getMessage("product.found"), true, all);
    }

    public ApiResponse searchBarcode(UUID branchId, String barcodeWithKg) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            String language = locale.getLanguage();
            ProductResponseDTO productByKeywordDto = new ProductResponseDTO();
            int barcode = Integer.parseInt(barcodeWithKg.substring(1, 7)); // barcode o'rni: 1-6
            double totalKg = Double.parseDouble(barcodeWithKg.substring(7, barcodeWithKg.length() - 1)) / 1000; // 7-x dan oxirigacha
            productByKeywordDto =
                    findProductByKeywordDto(branchId, String.valueOf(barcode), language);

            if (productByKeywordDto != null) {
                productByKeywordDto.setAmount(totalKg);
            } else {
                productByKeywordDto =
                        findProductByKeywordDto(branchId, barcodeWithKg, language);
                if (productByKeywordDto == null) {
                    return new ApiResponse(messageService.getMessage("product.not.found"), false);
                }
            }
            if (Boolean.TRUE.equals(productByKeywordDto.getDiscount())) {
                updateDiscount(productByKeywordDto, branchId);
            }


            return new ApiResponse(messageService.getMessage("found"), true, productByKeywordDto);

        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return new ApiResponse(messageService.getMessage("error.message"), false);
        }
    }


    private List<ProductResponseDTO> findProductByKeyword(UUID branchId, String keyword, String language) {
        if (keyword == null || keyword.isBlank()) return new ArrayList<>();
        return productRepository.findProductsByBranchIdAndKeyword(branchId, keyword, language);
    }

    private ProductResponseDTO findProductByKeywordDto(UUID branchId, String keyword, String language) {
        Optional<ProductResponseDTO> optional = productRepository.findByBranchIdAndBarcodeDto(branchId, keyword, language);
        return optional.orElse(null);
    }

    private void updateDiscount(ProductResponseDTO productResponseDTO, UUID branchId) {
        LocalDate today = LocalDate.now();
        int dayNumber = today.getDayOfWeek().getValue(); // 1 (Dushanba) - 7 (Yakshanba)
        LocalTime now = LocalTime.now();

        discountRepository.findByProductIdAndBranchId(productResponseDTO.getId(), branchId)
                .ifPresent(discount -> {

                    boolean matchesWeek = discount.getIsWeekday()
                            && discount.getWeekDays().contains(dayNumber);

                    boolean matchesTime = discount.getIsTime() &&
                            (!now.isBefore(discount.getStartHour().toLocalTime())
                                    && !now.isAfter(discount.getEndHour().toLocalTime()));

                    productResponseDTO.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                    productResponseDTO.setDiscountValue(discount.getValue());

                    if (matchesWeek) {
                        if (matchesTime) {
                            productResponseDTO.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                            productResponseDTO.setDiscountValue(discount.getValue());
                        }
                        productResponseDTO.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                        productResponseDTO.setDiscountValue(discount.getValue());
                    }
                    if (matchesTime) {
                        productResponseDTO.setPercentage(discount.getType().equals(DiscountType.PERCENTAGE));
                        productResponseDTO.setDiscountValue(discount.getValue());
                    }
                });
    }

    public ApiResponse generateBarcode(UUID businessId) {
        Random random = new Random();
        String prefix = "300";
        int businessPart = Math.abs(businessId.hashCode()) % 1_000_000;
        int productPart = random.nextInt(1_000);
        String baseBarcode = String.format("%s%06d%03d", prefix, businessPart, productPart);
        int checksum = calculateEAN13Checksum(baseBarcode);
        String barcode = baseBarcode + checksum;

        boolean exist = productRepository.existsByBarcodeAndBusinessId(barcode, businessId);
        if (exist) {
            generateBarcode(businessId);
        }

        return new ApiResponse("success", true, barcode);
    }

    private static int calculateEAN13Checksum(String barcode) {
        int sum = 0;
        for (int i = 0; i < barcode.length(); i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        return (10 - (sum % 10)) % 10;
    }

}