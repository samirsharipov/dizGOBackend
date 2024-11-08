package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.entity.Currency;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;

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
    private final CurrencyRepository currencyRepository;
    private final FifoCalculationRepository fifoCalculationRepository;
    private final LanguageRepository languageRepository;
    private final ProductTranslateRepository productTranslateRepository;

    @Transactional
    public ApiResponse addProduct(ProductDTO productDto) {

        Optional<Business> optionalBusiness = businessRepository.findById(productDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found business", false);
        }
        Product product = new Product();
        List<UUID> branchId = productDto.getBranchIds();

        Optional<Measurement> optionalMeasurement =
                measurementRepository.findById(productDto.getMeasurementId());

        List<Branch> allBranch = branchRepository.findAllById(branchId);

        if (optionalMeasurement.isEmpty()) {
            return new ApiResponse("not found measurement", false);
        }
        if (allBranch.isEmpty()) {
            return new ApiResponse("not found branches", false);
        }

        fromDto(productDto, product, optionalMeasurement, optionalBusiness, allBranch);

        return createOrEditProduct(product, productDto, false);
    }


    public ApiResponse editProduct(UUID id, ProductDTO productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return createOrEditProduct(optionalProduct.get(), productDto, true);
    }

    public ApiResponse createOrEditProduct(Product product, ProductDTO productDto, boolean isUpdate) {

        Business business = product.getBusiness();

        Optional<Currency> optionalCurrency = currencyRepository.findByBusinessId(business.getId());
        Currency currency;
        if (optionalCurrency.isPresent()) {
            currency = optionalCurrency.get();
        } else {
            Optional<Currency> optional = currencyRepository.findFirstByCourseIsNotNullOrderByUpdateAtDesc();
            currency = optional.orElseGet(() -> currencyRepository.save(new Currency(
                    business,
                    11400
            )));
        }
        product.setBuyPriceDollar((productDto.getBuyPrice() / currency.getCourse() * 100) / 100);
        product.setSalePriceDollar((productDto.getSalePrice() / currency.getCourse() * 100) / 100.);
        product.setGrossPriceDollar((productDto.getGrossPrice() / currency.getCourse() * 100) / 100.);


        if (productDto.getBarcode() != null && !productDto.getBarcode().isBlank()) {
            if (isUpdate) {
                if (productRepository.existsByBarcodeAndBusinessIdAndIdIsNotAndActiveTrue(productDto.getBarcode(), product.getBusiness().getId(), product.getId()))
                    return new ApiResponse("product with the barcode is already exist", false);
            } else {
                if (productRepository.existsByBarcodeAndBusinessIdAndActiveTrue(productDto.getBarcode(), product.getBusiness().getId()))
                    return new ApiResponse("product with the barcode is already exist", false);
            }
            product.setBarcode(productDto.getBarcode());
        } else {
            product.setBarcode(generateBarcode(product.getBusiness().getId(), product.getName(), product.getId(), isUpdate));
        }

        if (productDto.getPluCode() != null && !productDto.getPluCode().isBlank()) {
            if (isUpdate) {
                if (!productRepository.existsByPluCodeAndBusiness_Id(productDto.getPluCode(), product.getBusiness().getId())) {
                    product.setPluCode(productDto.getPluCode());
                }
            } else {
                product.setPluCode(productDto.getPluCode());
            }
        }

        Product saved = productRepository.save(product);

        for (ProductTranslateDTO productTranslateDTO : productDto.getTranslations()) {
            ProductTranslate productTranslate = fromTranslateDto(productTranslateDTO, saved);
            productTranslateRepository.save(productTranslate);
        }


        return new ApiResponse("OK", true);
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

    public ApiResponse getAll(User user) {
        Set<Branch> branches = user.getBranches();
        List<Product> productList = new ArrayList<>();
        for (Branch branch : branches) {
            List<Product> all = productRepository.findAllByBranchIdAndActiveIsTrue(branch.getId());
            if (!all.isEmpty()) {
                productList.addAll(all);
            }
        }
        if (productList.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, productList);
    }

    public ApiResponse getProduct(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        return optionalProduct.map(product -> new ApiResponse("FOUND", true, convertToDto(product)))
                .orElseGet(() -> new ApiResponse("NOT FOUND", false));

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
            return new ApiResponse("DELETED", true);
        }
        return new ApiResponse("NOT FOUND", false);
    }

    public ApiResponse getByBarcode(String barcode, User user) {
        Set<Branch> branches = user.getBranches();
        List<Product> productAllByBarcode = new ArrayList<>();
        Branch branchGet = null;
        for (Branch branch : branches) {
            Optional<Product> optionalProduct = productRepository.findAllByBarcodeAndBranchIdAndActiveTrue(barcode, branch.getId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                productAllByBarcode.add(product);
                branchGet = branch;
            }
        }


        List<ProductViewDto> viewDtos = new ArrayList<>();
        for (Product product : productAllByBarcode) {
            ProductViewDto productViewDto = new ProductViewDto();
            productViewDto.setProductId(product.getId());
            productViewDto.setProductName(product.getName());
            if (product.getBrand() != null)
                productViewDto.setBrandName(product.getBrand().getName());
            productViewDto.setBuyPrice(product.getBuyPrice());
            productViewDto.setSalePrice(product.getSalePrice());
            productViewDto.setMinQuantity(product.getMinQuantity());
            productViewDto.setBranch(product.getBranch());
            productViewDto.setExpiredDate(product.getExpireDate());

            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branchGet.getId(), product.getId());
            if (optionalWarehouse.isPresent()) {
                Warehouse warehouse = optionalWarehouse.get();
                if (warehouse.getProduct().getId().equals(product.getId())) {
                    productViewDto.setAmount(warehouse.getAmount());
                }
            }
            viewDtos.add(productViewDto);
        }

        if (viewDtos.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, viewDtos);
    }

    public ApiResponse getByCategory(UUID category_id, User user) {
        Set<Branch> branches = user.getBranches();
        List<Product> productList = new ArrayList<>();
        for (Branch branch : branches) {
            List<Product> all = productRepository.findAllByCategoryIdAndBranchIdAndActiveTrue(category_id, branch.getId());
            if (!all.isEmpty()) {
                productList.addAll(all);
            }
        }

        List<ProductViewDto> viewDtos = new ArrayList<>();
        for (Product product : productList) {
            ProductViewDto productViewDto = new ProductViewDto();
            productViewDto.setProductId(product.getId());
            productViewDto.setProductName(product.getName());
            if (product.getBrand() != null)
                productViewDto.setBrandName(product.getBrand().getName());
            productViewDto.setBuyPrice(product.getBuyPrice());
            productViewDto.setSalePrice(product.getSalePrice());
            productViewDto.setMinQuantity(product.getMinQuantity());
            productViewDto.setBranch(product.getBranch());
            productViewDto.setExpiredDate(product.getExpireDate());
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByProduct_Id(product.getId());
            if (optionalWarehouse.isPresent()) {
                Warehouse warehouse = optionalWarehouse.get();
                if (warehouse.getProduct().getId().equals(product.getId())) {
                    productViewDto.setAmount(warehouse.getAmount());
                }
            }
            viewDtos.add(productViewDto);
        }


        if (viewDtos.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, viewDtos);
    }

    public ApiResponse getByBrand(UUID brand_id) {
        List<ProductViewDto> productViewDtos = new ArrayList<>();
        List<Product> allProductByBrand = productRepository.findAllByBrandIdAndActiveIsTrue(brand_id);
        getProductMethod(productViewDtos, allProductByBrand, null);

        return new ApiResponse("FOUND", true, productViewDtos);
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

    public ApiResponse getByBusinessPageable(UUID businessId, UUID branchId, UUID brandId, UUID catId, String search, int page, int size) {
        if (search.isBlank()) search = null;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");

        Page<Product> productPage;

        if (branchId != null) {
            if (search != null) {
                productPage = productRepository.findAllByBranch_IdAndNameContainingIgnoreCaseAndActiveTrueOrBusinessIdAndBarcodeContainingIgnoreCaseAndActiveTrue(branchId, search, businessId, search, pageable);
            } else if (catId != null && brandId != null) {
                productPage = productRepository.findAllByBranch_IdAndCategoryIdAndBrandIdAndActiveTrue(branchId, catId, brandId, pageable);
            } else if (catId != null) {
                productPage = productRepository.findAllByBranch_IdAndCategoryIdAndActiveTrue(branchId, catId, pageable);
            } else if (brandId != null) {
                productPage = productRepository.findAllByBranch_IdAndBrandIdAndActiveTrue(branchId, brandId, pageable);
            } else {
                productPage = productRepository.findAllByBranch_IdAndActiveTrue(branchId, pageable);
            }
        } else {
            if (search != null) {
                productPage = productRepository.findAllByBusinessIdAndNameContainingIgnoreCaseAndActiveTrue(businessId, search, pageable);
            } else if (catId != null && brandId != null) {
                productPage = productRepository.findAllByBusinessIdAndCategoryIdAndBrandIdAndActiveTrue(businessId, catId, brandId, pageable);
            } else if (catId != null) {
                productPage = productRepository.findAllByBusinessIdAndCategoryIdAndActiveTrue(businessId, catId, pageable);
            } else if (brandId != null) {
                productPage = productRepository.findAllByBusinessIdAndBrandIdAndActiveTrue(businessId, brandId, pageable);
            } else {
                productPage = productRepository.findAllByBusinessIdAndActiveTrue(businessId, pageable);
            }
        }
        List<ProductViewDto> productViewDtoList = new ArrayList<>();
        getProductMethod(productViewDtoList, productPage.getContent(), branchId);
        if (productViewDtoList.isEmpty()) {
            return new ApiResponse("MA'LUMOT TOPILMADI", false);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("product_list", productViewDtoList);

        response.put("currentPage", productPage.getNumber());
        response.put("totalPages", productPage.getTotalPages());
        response.put("totalItems", productPage.getTotalElements());
        return new ApiResponse(true, response);

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

    public ApiResponse search(UUID branchId, String name) {
        List<Product> all = productRepository.findAllByBranchIdAndActiveIsTrueAndNameContainingIgnoreCase(branchId, name);
        List<Product> allByBarcode = productRepository.findAllByBranchIdAndActiveIsTrueAndBarcodeContainingIgnoreCase(branchId, name);
        all.addAll(allByBarcode);
        List<ProductGetForPurchaseDto> getForPurchaseDtoList = new ArrayList<>();
        Set<Product> allProduct = new HashSet<>(all);
        all = new ArrayList<>(allProduct);
        toViewDtoMto(branchId, getForPurchaseDtoList, all);
        if (getForPurchaseDtoList.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("all", true, getForPurchaseDtoList);
    }


    private void fromDto(ProductDTO productDto, Product product, Optional<Measurement> optionalMeasurement, Optional<Business> optionalBusiness, List<Branch> allBranch) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setLongDescription(productDto.getLongDescription());
        product.setKeywords(productDto.getKeywords());
        product.setAttributes(productDto.getAttributes());

        product.setUniqueSKU(productDto.getUniqueSKU());

        product.setStockAmount(productDto.getStockAmount());
        product.setInStock(productDto.getInStock());
        product.setPreorder(productDto.getPreorder());
        product.setLength(productDto.getLength());
        product.setWidth(productDto.getWidth());
        product.setHeight(productDto.getHeight());
        product.setWeight(productDto.getWeight());

        product.setHsCode12(productDto.getHsCode12());
        product.setHsCode22(productDto.getHsCode22());
        product.setHsCode32(productDto.getHsCode32());
        product.setHsCode44(productDto.getHsCode44());

        product.setAgreementExportsID(productDto.getAgreementExportsID());
        product.setAgreementExportsPID(productDto.getAgreementExportsPID());
        product.setAgreementLocalID(productDto.getAgreementLocalID());
        product.setAgreementLocalPID(productDto.getAgreementLocalPID());
        product.setLangGroup(productDto.getLangGroup());
        product.setShippingClass(productDto.getShippingClass());
        product.setSoldIndividually(productDto.getSoldIndividually());

        product.setDueDate(productDto.getDueDate());
        product.setActive(productDto.isActive());
        product.setProfitPercent(productDto.getProfitPercent());
        product.setTax(productDto.getTax());


        product.setBuyPrice(productDto.getBuyPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setGrossPrice(productDto.getGrossPrice());
        product.setGrossPriceMyControl(productDto.getGrossPriceMyControl());
        product.setBuyDollar(productDto.isBuyDollar());
        product.setSaleDollar(productDto.isSaleDollar());

        product.setMinQuantity(productDto.getMinQuantity());

        product.setWarehouseCount(productDto.getWarehouseCount());
        product.setQuantity(product.getQuantity());
        product.setIsGlobal(productDto.getIsGlobal());
        product.setMain(productDto.isMain());

        if (productDto.getBrandId() != null) {
            UUID brandId = productDto.getBrandId();
            Optional<Brand> optionalBrand = brandRepository.findById(brandId);
            optionalBrand.ifPresent(product::setBrand);
        }

        if (productDto.getCategoryId() != null) {
            UUID categoryId = productDto.getCategoryId();
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            optionalCategory.ifPresent(product::setCategory);
        }

        product.setMeasurement(optionalMeasurement.get());

        if (productDto.getPhotoId() != null) {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(productDto.getPhotoId());
            optionalAttachment.ifPresent(product::setPhoto);
        }

        Business business = optionalBusiness.get();
        product.setBusiness(business);
        product.setBranch(allBranch);
        product.setActive(true);
    }


    public ProductGetDto convertToDto(Product product) {
        ProductGetDto dto = new ProductGetDto();

        // ID va yaratilgan vaqt
        dto.setId(product.getId());  // UUID dan string ga o'zgartirish
        dto.setCreatedAt(product.getCreatedAt()); // Yaratilgan vaqt

        // Mahsulot nomi va tavsifi
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setLongDescription(product.getLongDescription());
        dto.setKeywords(product.getKeywords());
        dto.setAttributes(product.getAttributes());

        // Mahsulotga oid boshqa ma'lumotlar
        dto.setUniqueSKU(product.getUniqueSKU());
        dto.setSalePrice(product.getSalePrice());
        dto.setBuyPrice(product.getBuyPrice());
        dto.setSalePriceDollar(product.getSalePriceDollar());
        dto.setStockAmount(product.getStockAmount());
        dto.setInStock(product.getInStock());
        dto.setPreorder(product.getPreorder());
        dto.setLength(product.getLength());
        dto.setWidth(product.getWidth());
        dto.setHeight(product.getHeight());
        dto.setWeight(product.getWeight());

        // Harmonizatsiya kodlari
        dto.setHsCode12(product.getHsCode12());
        dto.setHsCode22(product.getHsCode22());
        dto.setHsCode32(product.getHsCode32());
        dto.setHsCode44(product.getHsCode44());

        // Shartnomalar bilan bog'liq ma'lumotlar
        dto.setAgreementExportsID(product.getAgreementExportsID());
        dto.setAgreementExportsPID(product.getAgreementExportsPID());
        dto.setAgreementLocalID(product.getAgreementLocalID());
        dto.setAgreementLocalPID(product.getAgreementLocalPID());

        // Qo'shimcha ma'lumotlar
        dto.setLangGroup(product.getLangGroup());
        dto.setShippingClass(product.getShippingClass());
        dto.setSoldIndividually(product.getSoldIndividually());
        dto.setActive(product.isActive());
        dto.setProfitPercent(product.getProfitPercent());
        dto.setTax(product.getTax());

        // Ixtiyoriy qiymatlar
        dto.setGrossPrice(product.getGrossPrice());
        dto.setGrossPriceDollar(product.getGrossPriceDollar());
        dto.setGrossPriceMyControl(product.getGrossPriceMyControl());
        dto.setBuyPriceDollar(product.getBuyPriceDollar());
        dto.setBuyDollar(product.isBuyDollar());
        dto.setSaleDollar(product.isSaleDollar());
        dto.setKpiPercent(product.getKpiPercent());
        dto.setKpi(product.getKpi());
        dto.setExpireDate(product.getExpireDate());
        dto.setBarcode(product.getBarcode());
        dto.setPluCode(product.getPluCode());
        dto.setMinQuantity(product.getMinQuantity());

        // Brand, Category va boshqa bog'lanishlar
        dto.setBrandName(product.getBrand() != null ? product.getBrand().getName() : null); // Brend nomi
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null); // Kategoriya nomi
        dto.setMeasurementUnit(product.getMeasurement() != null ? product.getMeasurement().getName() : null); // O'lchov birligi
        dto.setPhotoId(product.getPhoto() != null ? product.getPhoto().getId() : null);

        // Ombor joylari va boshqa bog'lanishlar
        Optional<Warehouse> optionalWarehouse =
                warehouseRepository.findByProduct_Id(product.getId());
        dto.setWarehouseCount(optionalWarehouse.map(Warehouse::getAmount).orElse(0d));
        dto.setBusinessName(product.getBusiness() != null ? product.getBusiness().getName() : null);
        dto.setBranches(product.getBranch() != null ? product.getBranch().stream().map(Branch::getName).collect(Collectors.toList()) : null); // Filiallar ro'yxati

        return dto;
    }

}