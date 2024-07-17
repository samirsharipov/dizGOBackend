package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.CarInvoiceType;
import uz.pdp.springsecurity.enums.OUTLAY_STATUS;
import uz.pdp.springsecurity.mapper.TradeLidMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.payload.projections.ReportForProduct;
import uz.pdp.springsecurity.repository.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsService {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProductionRepository productionRepository;
    @Autowired
    CustomerDebtRepaymentRepository customerDebtRepaymentRepository;
    @Autowired
    DebtCanculsRepository debtCanculsRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    TradeProductRepository tradeProductRepository;
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    PurchaseProductRepository purchaseProductRepository;
    @Autowired
    OutlayRepository outlayRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private final CustomerDebtRepository customerDebtRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    OutlayCategoryRepository outlayCategoryRepository;

    @Autowired
    ProductTypePriceRepository productTypePriceRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    FifoCalculationService fifoCalculationService;

    @Autowired
    MeasurementRepository measurementRepository;

    private final PayMethodRepository payMethodRepository;
    private final CarInvoiceRepository carInvoiceRepository;
    private final static Date date = new Date();
    private final static Timestamp currentDay = new Timestamp(System.currentTimeMillis());
    private final static Timestamp enDate = new Timestamp(date.getTime());
    private final static LocalDateTime dateTime = enDate.toLocalDateTime();
    private final static LocalDateTime LAST_MONTH = dateTime.minusMonths(1);
    private final static LocalDate localDate = LocalDate.now();
    private final static LocalDateTime THIS_MONTH = localDate.withDayOfMonth(1).atStartOfDay();

    private final static LocalDate WEEK_START_DAY = localDate.minusDays(7 + localDate.getDayOfWeek().getValue() - 1);
    private final static LocalDate WEEK_END_DAY = localDate.minusDays(7 + localDate.getDayOfWeek().getValue() - 7);

    private final static LocalDate TEMP_START_OF_YEAR = LocalDate.of(localDate.getYear() - 1, 1, 1);
    private final static LocalDate TEMP_FOR_THIS_START_OF_YEAR = LocalDate.of(localDate.getYear(), 1, 1);
    private final static LocalDate TEMP_START_OF_DAY = localDate.minusDays(1);
    private final static LocalDate TEMP_END_OF_DAY = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());
    private final static LocalDate TEMP_END_OF_YEAR = LocalDate.of(localDate.getYear() - 1, 12, 31);
    private final static LocalDate TEMP_START_OF_MONTH_ONE = LocalDate.of(localDate.getYear(), localDate.getMonth().getValue(), 1);
    private final static LocalDate TEMP_START_OF_MONTH = TEMP_START_OF_MONTH_ONE.minusMonths(1);
    private final static LocalDate TEMP_END_OF_MONTH = LocalDate.of(localDate.getYear(), TEMP_START_OF_MONTH.getMonth(), TEMP_START_OF_MONTH.lengthOfMonth());
    private final static LocalDateTime START_OF_YEAR = TEMP_START_OF_YEAR.atStartOfDay();
    private final static LocalDateTime START_OF_YEAR_FOR_THIS = TEMP_FOR_THIS_START_OF_YEAR.atStartOfDay();
    private final static LocalDateTime END_OF_YEAR = TEMP_END_OF_YEAR.atStartOfDay();
    private final static LocalDateTime START_OF_MONTH = TEMP_START_OF_MONTH.atStartOfDay();
    private final static LocalDateTime END_OF_MONTH = TEMP_END_OF_MONTH.atStartOfDay();
    private final static LocalDateTime START_OF_DAY = TEMP_START_OF_DAY.atStartOfDay();
    private final static LocalDateTime END_OF_DAY = TEMP_END_OF_DAY.atStartOfDay();
    private final static LocalDateTime TODAY_START = LocalDate.now().atStartOfDay();
    private final static LocalDateTime TODAY_END = LocalDateTime.of(TODAY_START.getYear(), TODAY_START.getMonth(), TODAY_START.getDayOfMonth(), 23, 59, 59);

    @Autowired
    private LidRepository lidRepository;
    @Autowired
    private LidStatusRepository lidStatusRepository;
    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private UserRepository userRepository;

    private final TradeLidMapper tradeLidMapper;

    @Autowired
    private RepaymentDebtRepository repaymentDebtRepository;
    @Autowired
    private FifoCalculationRepository fifoCalculationRepository;

    public ApiResponse allProductAmount(UUID branchId, UUID brandId, UUID categoryId, String production) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);

        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }

        Optional<Business> optionalBusiness = businessRepository.findById(optionalBranch.get().getBusiness().getId());

        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("Business Not Found", false);
        }
        UUID businessId = optionalBranch.get().getBusiness().getId();
        List<Product> productList = new ArrayList<>();
        List<ProductTypePrice> productTypePriceList = new ArrayList<>();
        if (categoryId == null && production == null && brandId == null) {
            productList = productRepository.findAllByBranchIdAndActiveIsTrue(branchId);
            productTypePriceList = productTypePriceRepository.findAllByProduct_BranchIdAndProduct_ActiveIsTrue(branchId);
            if (productList.isEmpty()) {
                return new ApiResponse("No Found Products");
            }
        } else if (categoryId != null && brandId == null) {
            productList = productRepository.findAllByCategoryIdAndBranchIdAndActiveTrue(categoryId, branchId);
            productTypePriceList = productTypePriceRepository.findAllByProduct_Category_IdAndProduct_Branch_IdAndProduct_ActiveIsTrue(categoryId, branchId);
        } else if (categoryId == null && production == null) {
            productList = productRepository.findAllByBrandIdAndBranchIdAndActiveTrue(brandId, branchId);
            productTypePriceList = productTypePriceRepository.findAllByProduct_Brand_IdAndProduct_Branch_IdAndProduct_ActiveIsTrue(brandId, branchId);
        } else if (production != null && categoryId != null) {
            List<Production> productionList = productionRepository.findAllByProduct_CategoryIdAndProduct_BrandIdAndProduct_BranchIdAndDoneIsTrue(categoryId, brandId, branchId);
            if (productionList.isEmpty()) {
                return new ApiResponse("Production Not Found", false);
            }

            List<Product> products = new ArrayList<>();
            List<ProductTypePrice> priceList = new ArrayList<>();
            for (Production productions : productionList) {
                if (productions.getProduct() != null) {
                    Optional<Product> optionalProduct = productRepository.findById(productions.getProduct().getId());
                    products.add(optionalProduct.get());
                } else {
                    Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository.findById(productions.getProductTypePrice().getId());
                    priceList.add(optionalProductTypePrice.get());
                }
            }
            productList = products;
            productTypePriceList = priceList;

        } else if (production != null && categoryId != null && brandId == null) {
            List<Production> productionList = productionRepository.findAllByProduct_CategoryIdAndProduct_BranchIdAndDoneIsTrue(categoryId, branchId);
            if (productionList.isEmpty()) {
                return new ApiResponse("Production Not Found", false);
            }

            List<Product> products = new ArrayList<>();
            List<ProductTypePrice> priceList = new ArrayList<>();
            for (Production productions : productionList) {
                if (productions.getProduct() != null) {
                    Optional<Product> optionalProduct = productRepository.findById(productions.getProduct().getId());
                    products.add(optionalProduct.get());
                } else {
                    Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository.findById(productions.getProductTypePrice().getId());
                    priceList.add(optionalProductTypePrice.get());
                }
            }
            productList = products;
            productTypePriceList = priceList;

        } else if (production != null && categoryId == null && brandId != null) {
            List<Production> productionList = productionRepository.findAllByProduct_BrandIdAndProduct_BranchIdAndDoneIsTrue(categoryId, branchId);
            if (productionList.isEmpty()) {
                return new ApiResponse("Production Not Found", false);
            }
            List<Product> products = new ArrayList<>();
            List<ProductTypePrice> priceList = new ArrayList<>();
            for (Production productions : productionList) {
                if (productions.getProduct() != null) {
                    Optional<Product> optionalProduct = productRepository.findById(productions.getProduct().getId());
                    products.add(optionalProduct.get());
                } else {
                    Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository.findById(productions.getProductTypePrice().getId());
                    priceList.add(optionalProductTypePrice.get());
                }
            }
            productList = products;
            productTypePriceList = priceList;
        } else if (brandId != null && categoryId != null) {
            productList = productRepository.findAllByBrandIdAndCategoryIdAndBranchIdAndActiveTrue(brandId, categoryId, branchId);
            productTypePriceList = productTypePriceRepository.findAllByProduct_BrandIdAndProduct_CategoryIdAndProduct_Branch_IdAndProduct_ActiveIsTrue(brandId, categoryId, branchId);
        } else if (production != null && categoryId == null && brandId == null) {
            List<Production> productionList = productionRepository.findAllByBranchIdAndDoneIsTrueOrderByCreatedAtDesc(branchId);
            if (productionList.isEmpty()) {
                return new ApiResponse("Production Not Found", false);
            }
            List<Product> products = new ArrayList<>();
            List<ProductTypePrice> priceList = new ArrayList<>();
            for (Production productions : productionList) {
                if (productions.getProduct() != null) {
                    Optional<Product> optionalProduct = productRepository.findById(productions.getProduct().getId());
                    products.add(optionalProduct.get());
                } else {
                    Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository.findById(productions.getProductTypePrice().getId());
                    priceList.add(optionalProductTypePrice.get());
                }
            }
            productList = products;
            productTypePriceList = priceList;
        }
        if (productList.isEmpty() && productTypePriceList.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        double SumBySalePrice = 0;
        double SumByBuyPrice = 0;

        List<ProductReportDto> productReportDtoList = new ArrayList<>();
        ProductReportDto productReportDto = new ProductReportDto();
        for (Product product : productList) {
            productReportDto = new ProductReportDto();
            productReportDto.setName(product.getName());
            if (product.getBrand() != null) productReportDto.setBrand(product.getBrand().getName());
            productReportDto.setBranch(optionalBranch.get().getName());
            if (product.getCategory() != null) productReportDto.setCategory(product.getCategory().getName());
            if (product.getChildCategory() != null) productReportDto.setCategory(product.getChildCategory().getName());
            productReportDto.setBuyPrice(product.getBuyPrice());
            productReportDto.setSalePrice(product.getSalePrice());

            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByProductIdAndBranchId(product.getId(), optionalBranch.get().getId());
            Warehouse warehouse = new Warehouse();
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
            }
            productReportDto.setAmount(warehouse.getAmount());

            double amount = warehouse.getAmount();
            double salePrice = product.getSalePrice();
            double buyPrice = product.getBuyPrice();

            SumBySalePrice = amount * salePrice;
            SumByBuyPrice = amount * buyPrice;
            productReportDto.setSumBySalePrice(SumBySalePrice);
            productReportDto.setSumByBuyPrice(SumByBuyPrice);
            if (productReportDto.getBarcode() == null && productReportDto.getAmount() == 0 && productReportDto.getBuyPrice() == 0 && productReportDto.getSalePrice() == 0)
                continue;
            productReportDtoList.add(productReportDto);
        }

        for (ProductTypePrice productTypePrice : productTypePriceList) {
            productReportDto = new ProductReportDto();
            productReportDto.setName(productTypePrice.getName());
            if (productTypePrice.getProduct().getBrand() != null)
                productReportDto.setBrand(productTypePrice.getProduct().getBrand().getName());
            productReportDto.setBranch(optionalBranch.get().getName());
            if (productTypePrice.getProduct().getCategory() != null)
                productReportDto.setCategory(productTypePrice.getProduct().getCategory().getName());
            if (productTypePrice.getProduct().getChildCategory() != null)
                productReportDto.setChildCategory(productTypePrice.getProduct().getChildCategory().getName());
            productReportDto.setBuyPrice(productTypePrice.getBuyPrice());
            productReportDto.setSalePrice(productTypePrice.getSalePrice());

            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByProductTypePriceIdAndBranchId(productTypePrice.getId(), branchId);
            Warehouse warehouse = new Warehouse();
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
            }
            productReportDto.setAmount(warehouse.getAmount());

            double amount = warehouse.getAmount();
            double salePrice = productTypePrice.getSalePrice();
            double buyPrice = productTypePrice.getBuyPrice();

            SumBySalePrice = amount * salePrice;
            SumByBuyPrice = amount * buyPrice;
            productReportDto.setSumBySalePrice(SumBySalePrice);
            productReportDto.setSumByBuyPrice(SumByBuyPrice);
            productReportDtoList.add(productReportDto);
        }
        productReportDtoList.sort(Comparator.comparing(ProductReportDto::getAmount).reversed());
        return new ApiResponse("Business Products Amount", true, productReportDtoList);
    }

    public ApiResponse tradeProductByBranch(UUID branchId, UUID payMethodId, UUID customerId, Date startDate, Date endDate) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }

        List<TradeProduct> tradeProductList = getTradeProductList(branchId, payMethodId, customerId, startDate, endDate);
        if (tradeProductList.isEmpty()) {
            return new ApiResponse("Trade Not Found", false);
        }

        List<TradeReportsDto> tradeReportsDtoList = new ArrayList<>();
        for (TradeProduct tradeProduct : tradeProductList) {
            TradeReportsDto tradeReportsDto = new TradeReportsDto();
            if (tradeProduct.getProduct() != null) {
                tradeReportsDto.setName(tradeProduct.getProduct().getName());
                tradeReportsDto.setBarcode(tradeProduct.getProduct().getBarcode());
                tradeReportsDto.setSalePrice(tradeProduct.getProduct().getSalePrice());
            } else {
                tradeReportsDto.setName(tradeProduct.getProductTypePrice().getName());
                tradeReportsDto.setBarcode(tradeProduct.getProductTypePrice().getBarcode());
                tradeReportsDto.setSalePrice(tradeProduct.getProductTypePrice().getSalePrice());
            }

            tradeReportsDto.setTradeProductId(tradeProduct.getTrade().getId());
            tradeReportsDto.setTradedDate(tradeProduct.getTrade().getPayDate());

            if (tradeProduct.getTrade().getCustomer() != null) {
                tradeReportsDto.setCustomerName(tradeProduct.getTrade().getCustomer().getName());
            }
            tradeReportsDto.setPayMethod(tradeProduct.getTrade().getPayMethod().getType());
            tradeReportsDto.setAmount(tradeProduct.getTradedQuantity());
            if (tradeProduct.getTrade().getCustomer() != null && tradeProduct.getTrade().getCustomer().getCustomerGroup() != null) {
                tradeReportsDto.setDiscount(tradeProduct.getTrade().getCustomer().getCustomerGroup().getPercent());
            }
            tradeReportsDto.setTotalSum(tradeProduct.getTotalSalePrice());
            tradeReportsDto.setProfit(tradeProduct.getProfit());
            tradeReportsDto.setInvoice(tradeProduct.getTrade().getInvoice());

            // Sotuvchi ma'lumotlarini olish
            User trader = tradeProduct.getTrade().getTrader();
            if (trader != null) {
                tradeReportsDto.setTraderName(trader.getFirstName() + " " + trader.getLastName());
            }

            tradeReportsDtoList.add(tradeReportsDto);
        }
        tradeReportsDtoList.sort(Comparator.comparing(TradeReportsDto::getAmount).reversed());
        return new ApiResponse("Traded Products", true, tradeReportsDtoList);
    }

    private List<TradeProduct> getTradeProductList(UUID branchId, UUID payMethodId, UUID customerId, Date startDate, Date endDate) {
        // Yuqoridagi barcha shartlarni tekshirish uchun kodni shu metodga olib keldik
        List<TradeProduct> tradeProductList = new ArrayList<>();
        if (payMethodId == null && customerId == null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByTrade_BranchId(branchId);
        } else if (payMethodId != null && customerId == null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByTrade_PayMethodIdAndTrade_BranchId(payMethodId, branchId);
        } else if (payMethodId != null && customerId == null && startDate != null && endDate != null) {
            Timestamp from = new Timestamp(startDate.getTime());
            Timestamp to = new Timestamp(endDate.getTime());
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_PayMethodIdAndTrade_BranchId(from, to, payMethodId, branchId);
        } else if (customerId != null && payMethodId == null && startDate != null && endDate != null) {
            Timestamp from = new Timestamp(startDate.getTime());
            Timestamp to = new Timestamp(endDate.getTime());
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerIdAndTrade_BranchId(from, to, customerId, branchId);
        } else if (customerId == null && payMethodId == null && startDate != null && endDate != null) {
            Timestamp from = new Timestamp(startDate.getTime());
            Timestamp to = new Timestamp(endDate.getTime());
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchId(from, to, branchId);
        } else if (payMethodId == null && customerId != null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByTrade_CustomerIdAndTrade_BranchId(customerId, branchId);
        } else if (customerId != null && payMethodId != null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByTrade_CustomerIdAndTrade_BranchIdAndTrade_PayMethodId(customerId, branchId, payMethodId);
        }
        return tradeProductList;
    }
    public ApiResponse allProductByBrand(UUID branchId, UUID brandId) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        if (optionalBrand.isEmpty()) {
            return new ApiResponse("Brand Not Found", false);
        }

        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        Optional<Business> optionalBusiness = businessRepository.findById(optionalBranch.get().getBusiness().getId());

        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("Business Not Found", false);
        }
        UUID businessId = optionalBranch.get().getBusiness().getId();
        List<Product> productList = productRepository.findAllByBrandIdAndBusinessIdAndActiveTrue(brandId, businessId);
        List<ProductTypePrice> productTypePrices = productTypePriceRepository.findAllByProduct_BranchIdAndProduct_BrandIdAndActiveTrue(branchId, brandId);
        if (productList.isEmpty() && productTypePrices.isEmpty()) {
            return new ApiResponse("No Found Products", false);
        }

        double SumBySalePrice = 0;
        double SumByBuyPrice = 0;

        List<ProductReportDto> productReportDtoList = new ArrayList<>();
        ProductReportDto productReportDto = new ProductReportDto();
        for (Product product : productList) {
            productReportDto = new ProductReportDto();
            productReportDto.setName(product.getName());
            if (product.getBrand() != null) productReportDto.setBrand(product.getBrand().getName());
            productReportDto.setBranch(optionalBranch.get().getName());
            if (product.getCategory() != null) productReportDto.setCategory(product.getCategory().getName());
            if (product.getChildCategory() != null)
                productReportDto.setChildCategory(product.getChildCategory().getName());
            productReportDto.setBuyPrice(product.getBuyPrice());
            productReportDto.setSalePrice(product.getSalePrice());

            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByProductIdAndBranchId(product.getId(), optionalBranch.get().getId());
            Warehouse warehouse = new Warehouse();
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
            }
            productReportDto.setAmount(warehouse.getAmount());

            double amount = warehouse.getAmount();
            double salePrice = product.getSalePrice();
            double buyPrice = product.getBuyPrice();

            SumBySalePrice = amount * salePrice;
            SumByBuyPrice = amount * buyPrice;
            productReportDto.setSumBySalePrice(SumBySalePrice);
            productReportDto.setSumByBuyPrice(SumByBuyPrice);
            if (productReportDto.getSalePrice() == 0 && productReportDto.getAmount() == 0 && productReportDto.getBuyPrice() == 0 && productReportDto.getBarcode() == null)
                continue;
            productReportDtoList.add(productReportDto);
        }
        for (ProductTypePrice product : productTypePrices) {
            productReportDto = new ProductReportDto();
            productReportDto.setName(product.getName());
            if (product.getProduct().getBrand() != null)
                productReportDto.setBrand(product.getProduct().getBrand().getName());
            productReportDto.setBranch(optionalBranch.get().getName());
            if (product.getProduct().getCategory() != null)
                productReportDto.setCategory(product.getProduct().getCategory().getName());
            if (product.getProduct().getChildCategory() != null)
                productReportDto.setChildCategory(product.getProduct().getChildCategory().getName());
            productReportDto.setBuyPrice(product.getBuyPrice());
            productReportDto.setSalePrice(product.getSalePrice());

            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(optionalBranch.get().getId(), product.getId());
            Warehouse warehouse = new Warehouse();
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
            }
            productReportDto.setAmount(warehouse.getAmount());

            double amount = warehouse.getAmount();
            double salePrice = product.getSalePrice();
            double buyPrice = product.getBuyPrice();

            SumBySalePrice = amount * salePrice;
            SumByBuyPrice = amount * buyPrice;
            productReportDto.setSumBySalePrice(SumBySalePrice);
            productReportDto.setSumByBuyPrice(SumByBuyPrice);
            productReportDtoList.add(productReportDto);
        }
        productReportDtoList.sort(Comparator.comparing(ProductReportDto::getAmount).reversed());
        return new ApiResponse("Business Products Amount", true, productReportDtoList);
    }

    public ApiResponse allProductByCategory(UUID branchId, UUID categoryId) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            return new ApiResponse("Category Not Found", false);
        }
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        Optional<Business> optionalBusiness = businessRepository.findById(optionalBranch.get().getBusiness().getId());

        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("Business Not Found", false);
        }
        UUID businessId = optionalBranch.get().getBusiness().getId();
        List<Product> productList = productRepository.findAllByCategoryIdAndBusinessIdAndActiveTrue(categoryId, businessId);
        List<ProductTypePrice> productTypePriceList = productTypePriceRepository.findAllByProduct_CategoryIdAndProduct_BranchIdAndProduct_ActiveTrue(categoryId, branchId);
        if (productList.isEmpty() && productTypePriceList.isEmpty()) {
            return new ApiResponse("No Found Products", false);
        }

        double SumBySalePrice = 0;
        double SumByBuyPrice = 0;

        List<ProductReportDto> productReportDtoList = new ArrayList<>();
        ProductReportDto productReportDto;
        for (Product product : productList) {
            productReportDto = new ProductReportDto();
            productReportDto.setName(product.getName());
            if (product.getBrand() != null) productReportDto.setBrand(product.getBrand().getName());
            productReportDto.setBranch(optionalBranch.get().getName());
            if (product.getCategory() != null) productReportDto.setCategory(product.getCategory().getName());
            if (product.getChildCategory() != null)
                productReportDto.setChildCategory(product.getChildCategory().getName());
            productReportDto.setBuyPrice(product.getBuyPrice());
            productReportDto.setSalePrice(product.getSalePrice());

            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByProductIdAndBranchId(product.getId(), optionalBranch.get().getId());
            Warehouse warehouse = new Warehouse();
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
            }
            productReportDto.setAmount(warehouse.getAmount());

            double amount = warehouse.getAmount();
            double salePrice = product.getSalePrice();
            double buyPrice = product.getBuyPrice();

            SumBySalePrice = amount * salePrice;
            SumByBuyPrice = amount * buyPrice;
            productReportDto.setSumBySalePrice(SumBySalePrice);
            productReportDto.setSumByBuyPrice(SumByBuyPrice);
            if (productReportDto.getBarcode() == null && productReportDto.getBuyPrice() == 0 && productReportDto.getSalePrice() == 0 && productReportDto.getAmount() == 0)
                continue;
            productReportDtoList.add(productReportDto);
        }
        for (ProductTypePrice product : productTypePriceList) {
            productReportDto = new ProductReportDto();
            productReportDto.setName(product.getName());
            if (product.getProduct().getBrand() != null)
                productReportDto.setBrand(product.getProduct().getBrand().getName());
            productReportDto.setBranch(optionalBranch.get().getName());
            if (product.getProduct().getCategory() != null)
                productReportDto.setCategory(product.getProduct().getCategory().getName());
            if (product.getProduct().getChildCategory() != null)
                productReportDto.setChildCategory(product.getProduct().getChildCategory().getName());
            productReportDto.setBuyPrice(product.getBuyPrice());
            productReportDto.setSalePrice(product.getSalePrice());
            productReportDto.setBrand(product.getBarcode());
            productReportDto.setBarcode(product.getBarcode());

            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(branchId, product.getId());
            Warehouse warehouse = new Warehouse();
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
            }
            productReportDto.setAmount(warehouse.getAmount());

            double amount = warehouse.getAmount();
            double salePrice = product.getSalePrice();
            double buyPrice = product.getBuyPrice();

            SumBySalePrice = amount * salePrice;
            SumByBuyPrice = amount * buyPrice;
            productReportDto.setSumBySalePrice(SumBySalePrice);
            productReportDto.setSumByBuyPrice(SumByBuyPrice);
            productReportDtoList.add(productReportDto);
        }
        productReportDtoList.sort(Comparator.comparing(ProductReportDto::getAmount).reversed());
        return new ApiResponse("Business Products Amount", true, productReportDtoList);
    }


    @Transactional
    public ApiResponse allProductAmountByBranch(UUID branchId, UUID businessId) {

        Optional<Branch> optionalBranch = Optional.empty();
        Optional<Business> optionalBusiness = Optional.empty();
        if (branchId != null) {
            optionalBranch = branchRepository.findById(branchId);
        }
        if (businessId != null) {
            optionalBusiness = businessRepository.findById(businessId);
        }

        boolean checkingBranch = optionalBranch.isPresent();

        if (optionalBranch.isEmpty() && optionalBusiness.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        if (checkingBranch) {
            cleanWarehouseByDeletedProduct(optionalBranch.get().getBusiness().getId());
        } else {
            cleanWarehouseByDeletedProduct(businessId);
        }

        double totalSumBySalePrice = 0;
        Amount amounts = new Amount();
        List<Warehouse> warehouseList;
        if (checkingBranch) {
            warehouseList = warehouseRepository.findAllByBranchId(branchId);
            amounts.setTotalSumByBuyPrice(fifoCalculationService.productBuyPriceByBranch(branchId));
        } else {
            warehouseList = warehouseRepository.findAllByBranch_Business_Id(businessId);
            amounts.setTotalSumByBuyPrice(fifoCalculationService.productBuyPriceByBusiness(businessId));
        }
        for (Warehouse warehouse : warehouseList) {
            if (warehouse.getProduct() != null)
                totalSumBySalePrice += warehouse.getAmount() * warehouse.getProduct().getSalePrice();
            else
                totalSumBySalePrice += warehouse.getAmount() * warehouse.getProductTypePrice().getSalePrice();
        }
        amounts.setTotalSumBySalePrice(totalSumBySalePrice);
        return new ApiResponse("Business Products Amount", true, amounts);
    }

    @Transactional
    public void cleanWarehouseByDeletedProduct(UUID businessId) {
        List<Product> productList = productRepository.findAllByBusinessIdAndActiveFalse(businessId);
        for (Product product : productList) {
            warehouseRepository.deleteAllByProductId(product.getId());
            warehouseRepository.deleteAllByProductTypePrice_ProductId(product.getId());
            fifoCalculationRepository.deleteAllByProductId(product.getId());
            fifoCalculationRepository.deleteAllByProductTypePrice_ProductId(product.getId());
        }
    }

    public ApiResponse mostUnSaleProducts(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found");
        }
        List<TradeProduct> tradeProductList = tradeProductRepository.findAllByTrade_BranchId(branchId);

        if (tradeProductList.isEmpty()) {
            return new ApiResponse("Traded Product Not Found");
        }

        Map<UUID, Double> productAmount = new HashMap<>();
        List<TradeProduct> allByProductId = new ArrayList<>();
        for (TradeProduct tradeProduct : tradeProductList) {
            double amount = 0;
            if (tradeProduct.getProductTypePrice() != null) {
                List<TradeProduct> tradeProducts = tradeProductRepository.findAllByTrade_BranchIdAndProductTypePriceId(tradeProduct.getTrade().getBranch().getId(), tradeProduct.getProductTypePrice().getId());
                if (tradeProduct.getProductTypePrice() != null) {
                    for (TradeProduct product : tradeProducts) {
                        amount += product.getTradedQuantity();
                        productAmount.put(product.getProductTypePrice().getId(), amount);
                    }
                }
            }
            if (tradeProduct.getProduct() != null) {
                List<TradeProduct> productList = tradeProductRepository.findAllByTrade_BranchIdAndProduct_Id(tradeProduct.getTrade().getBranch().getId(), tradeProduct.getProduct().getId());
                if (tradeProduct.getProduct() != null) {
                    for (TradeProduct product : productList) {
                        amount += product.getTradedQuantity();
                        productAmount.put(product.getProduct().getId(), amount);
                    }
                }
            }
        }
        List<MostSaleProductsDto> mostSaleProductsDtoList = new ArrayList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            MostSaleProductsDto mostSaleProductsDto = new MostSaleProductsDto();
            Optional<Product> product = productRepository.findById(entry.getKey());
            if (product.isPresent()) {
                mostSaleProductsDto.setName(product.get().getName());
                mostSaleProductsDto.setAmount(entry.getValue());
                mostSaleProductsDto.setSalePrice(product.get().getSalePrice());
                mostSaleProductsDto.setBuyPrice(product.get().getBuyPrice());
                mostSaleProductsDto.setBarcode(product.get().getBarcode());
                mostSaleProductsDto.setMeasurement(product.get().getMeasurement().getName());
                mostSaleProductsDto.setBranchName(optionalBranch.get().getName());
                mostSaleProductsDtoList.add(mostSaleProductsDto);
            }
            Optional<ProductTypePrice> productTypePrice = productTypePriceRepository.findById(entry.getKey());
            if (productTypePrice.isPresent()) {
                mostSaleProductsDto.setName(productTypePrice.get().getName());
                mostSaleProductsDto.setAmount(entry.getValue());
                mostSaleProductsDto.setSalePrice(productTypePrice.get().getSalePrice());
                mostSaleProductsDto.setBuyPrice(productTypePrice.get().getBuyPrice());
                mostSaleProductsDto.setBarcode(productTypePrice.get().getBarcode());
                mostSaleProductsDto.setMeasurement(productTypePrice.get().getProduct().getMeasurement().getName());
                mostSaleProductsDto.setBranchName(optionalBranch.get().getName());
                mostSaleProductsDtoList.add(mostSaleProductsDto);
            }
        }
        mostSaleProductsDtoList.sort(Comparator.comparing(MostSaleProductsDto::getAmount));
        return new ApiResponse("Found", true, mostSaleProductsDtoList);
    }

    public ApiResponse purchaseReports(UUID branchId, UUID supplierId, Date startDate, Date endDate) {
        Timestamp end = null;
        Timestamp start = null;
        List<PurchaseProduct> purchaseProductList = new ArrayList<>();
        List<PurchaseReportsDto> purchaseReportsDtoList = new ArrayList<>();

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        Branch branch = optionalBranch.get();

        if (startDate != null && endDate != null) {
            end = new Timestamp(endDate.getTime());
            start = new Timestamp(startDate.getTime());
        }

        if (supplierId != null) {
            if (end != null) {
                purchaseProductList = purchaseProductRepository.findAllByCreatedAtBetweenAndProduct_BranchIdAndPurchase_SupplierId(start, end, branchId, supplierId);
            } else {
                purchaseProductList = purchaseProductRepository.findAllByPurchase_BranchIdAndPurchase_SupplierId(branchId, supplierId);
            }
        } else {
            if (end != null) {
                purchaseProductList = purchaseProductRepository.findAllByCreatedAtBetweenAndPurchase_BranchId(start, end, branchId);
            } else {
                purchaseProductList = purchaseProductRepository.findAllByPurchase_BranchId(branch.getId());
            }
        }
        if (purchaseProductList.isEmpty()) {
            return new ApiResponse("Purchase Not Found", false);
        }
        for (PurchaseProduct purchaseProduct : purchaseProductList) {
            if (purchaseProduct.getProduct() == null) {
                PurchaseReportsDto purchaseReportsDto = new PurchaseReportsDto();
                purchaseReportsDto.setPurchaseId(purchaseProduct.getPurchase().getId());
                purchaseReportsDto.setPurchasedAmount(purchaseProduct.getPurchasedQuantity());
                purchaseReportsDto.setName(purchaseProduct.getProductTypePrice().getName());
                purchaseReportsDto.setBuyPrice(purchaseProduct.getProductTypePrice().getBuyPrice());
                purchaseReportsDto.setBarcode(purchaseProduct.getProductTypePrice().getBarcode());
                purchaseReportsDto.setTax(purchaseProduct.getProductTypePrice().getProfitPercent());
                purchaseReportsDto.setTotalSum(purchaseProduct.getTotalSum());
                purchaseReportsDto.setPurchasedDate(purchaseProduct.getCreatedAt());
                purchaseReportsDto.setSupplier(purchaseProduct.getPurchase().getSupplier().getName());
                purchaseReportsDto.setDebt(purchaseProduct.getPurchase().getDebtSum());
                purchaseReportsDtoList.add(purchaseReportsDto);
            } else {
                PurchaseReportsDto purchaseReportsDto = new PurchaseReportsDto();
                purchaseReportsDto.setPurchaseId(purchaseProduct.getPurchase().getId());
                purchaseReportsDto.setPurchasedAmount(purchaseProduct.getPurchasedQuantity());
                purchaseReportsDto.setName(purchaseProduct.getProduct().getName());
                purchaseReportsDto.setBuyPrice(purchaseProduct.getBuyPrice());
                purchaseReportsDto.setBarcode(purchaseProduct.getProduct().getBarcode());
                purchaseReportsDto.setTax(purchaseProduct.getProduct().getTax());
                purchaseReportsDto.setTotalSum(purchaseProduct.getTotalSum());
                purchaseReportsDto.setPurchasedDate(purchaseProduct.getCreatedAt());
                purchaseReportsDto.setSupplier(purchaseProduct.getPurchase().getSupplier().getName());
                purchaseReportsDto.setDebt(purchaseProduct.getPurchase().getDebtSum());
                purchaseReportsDtoList.add(purchaseReportsDto);
            }
        }
        return new ApiResponse("Found", true, purchaseReportsDtoList);
    }

    public ApiResponse deliveryPriceGet(UUID branchId) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Not Found");
        }

        List<Purchase> purchaseList = purchaseRepository.findAllByBranch_IdOrderByCreatedAtDesc(branchId);

        if (purchaseList.isEmpty()) {
            return new ApiResponse("Not Found Purchase", false);
        }

        double totalDelivery = 0;
        for (Purchase purchase : purchaseList) {
            totalDelivery += purchase.getDeliveryPrice();
        }
        return new ApiResponse("Found", true, totalDelivery);
    }

    public ApiResponse outlayReports(UUID branchId, UUID categoryId, Date startDate, Date endDate) {
        List<Outlay> outlayList = new ArrayList<>();
        Timestamp start = null;
        Timestamp end = null;

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        if (startDate != null && endDate != null) {
            start = new Timestamp(startDate.getTime());
            end = new Timestamp(endDate.getTime());
        }

        if (categoryId == null && startDate == null && endDate == null) {
            outlayCategoryRepository.findAllByBranch_Id(branchId);
            outlayList = outlayRepository.findAllByBranch_IdOrderByCreatedAtDesc(branchId);
            if (outlayList.isEmpty()) {
                return new ApiResponse("Not Found Outlay", false);
            }
        } else if (categoryId != null && startDate == null && endDate == null) {
            outlayList = outlayRepository.findAllByBranch_IdAndOutlayCategoryId(branchId, categoryId);
            if (outlayList.isEmpty()) {
                return new ApiResponse("Not Found Outlay", false);
            }
        } else if (categoryId != null && startDate != null && endDate != null) {
            outlayList = outlayRepository.findAllByCreatedAtBetweenAndBranchIdAndOutlayCategoryId(start, end, branchId, categoryId);
            if (outlayList.isEmpty()) {
                return new ApiResponse("Not Found Outlay", false);
            }
        } else if (categoryId == null && startDate != null && endDate != null) {
            outlayList = outlayRepository.findAllByCreatedAtBetweenAndBranchId(start, end, branchId);
            if (outlayList.isEmpty()) {
                return new ApiResponse("Not Found Outlay", false);
            }
        }
        Map<UUID, Double> productAmount = new HashMap<>();
        for (Outlay outlay : outlayList) {
            OutlayCategory category = outlay.getOutlayCategory();
            double totalSum = productAmount.getOrDefault(category.getId(), 0.0);
            totalSum += outlay.getTotalSum();
            productAmount.put(category.getId(), totalSum);
        }
        Map<String, Double> outlays = new HashMap<>();
        List<Outlay> all;
        for (Outlay outlay : outlayList) {
            if (startDate != null) {
                all = outlayRepository.findAllByCreatedAtBetweenAndBranchIdAndOutlayCategoryId(start, end, branchId, outlay.getOutlayCategory().getId());
            } else {
                all = outlayRepository.findAllByBranch_IdAndOutlayCategoryId(branchId, outlay.getOutlayCategory().getId());
            }
            double totalsumma = 0;
            for (Outlay outlayByCategory : all) {
                totalsumma += outlayByCategory.getTotalSum();
                outlays.put(outlay.getOutlayCategory().getTitle(), totalsumma);
            }
        }

        List<OutlayGetCategory> outlayGetCategoryList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : outlays.entrySet()) {
            OutlayGetCategory category = new OutlayGetCategory();
            category.setType(entry.getKey());
            category.setTotalSum(entry.getValue());
            outlayGetCategoryList.add(category);
        }

        outlayGetCategoryList.sort(Comparator.comparing(OutlayGetCategory::getTotalSum));
        return new ApiResponse("Found", true, outlayGetCategoryList);
    }

    public ApiResponse getCustomerByLimit(UUID branchId, UUID customerId, Date startDate, Date endDate, Integer page, Integer limit) {
        Timestamp to = null;
        Timestamp from = null;

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }
        List<TradeProduct> tradeProducts = tradeProductRepository.findAllByTrade_BranchId(branchId);
        if (tradeProducts.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        if (startDate != null && endDate != null) {
            to = new Timestamp(endDate.getTime());
            from = new Timestamp(startDate.getTime());
        }

        List<TradeProduct> tradeProductList;
        List<CustomerReportsDto> customerReportsDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, limit);
        int totalPages = 0;
        if (customerId != null) {
            if (from != null) {
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_Customer_Id(from, to, customerId);
            } else {
                tradeProductList = tradeProductRepository.findAllByTrade_CustomerId(customerId);
            }
        } else {
            if (from != null) {
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchId(from, to, branchId);
            } else {
                Page<TradeProduct> all = tradeProductRepository.findAllByTrade_BranchIdOrderByCreatedAt(branchId, pageable);
                tradeProductList = all.getContent();
                totalPages = all.getTotalPages();
            }
        }

        if (tradeProductList.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        for (TradeProduct tradeProduct : tradeProductList) {
            if (tradeProduct.getProduct() != null) {
                CustomerReportsDto customerReportsDto = new CustomerReportsDto();
                if (tradeProduct.getTrade().getCustomer() == null) continue;
                customerReportsDto.setCustomerName(tradeProduct.getTrade().getCustomer().getName());
                customerReportsDto.setDate(tradeProduct.getTrade().getPayDate());
                customerReportsDto.setDebt(tradeProduct.getTrade().getDebtSum());
                customerReportsDto.setProduct(tradeProduct.getProduct().getName());
                customerReportsDto.setPaidSum(tradeProduct.getTrade().getPaidSum());
                customerReportsDto.setTradedQuantity(tradeProduct.getTradedQuantity());
                customerReportsDto.setBranchName(tradeProduct.getTrade().getBranch().getName());
                customerReportsDto.setTotalSum(tradeProduct.getTrade().getTotalSum());
                customerReportsDto.setPayMethod(tradeProduct.getTrade().getPayMethod().getType());
                customerReportsDto.setPaymentStatus(tradeProduct.getTrade().getPaymentStatus().getStatus());
                customerReportsDtoList.add(customerReportsDto);
            } else {
                CustomerReportsDto customerReportsDto = new CustomerReportsDto();
                if (tradeProduct.getTrade().getCustomer() == null) continue;
                customerReportsDto.setCustomerName(tradeProduct.getTrade().getCustomer().getName());
                customerReportsDto.setDate(tradeProduct.getTrade().getPayDate());
                customerReportsDto.setDebt(tradeProduct.getTrade().getDebtSum());
                customerReportsDto.setProduct(tradeProduct.getProductTypePrice().getName());
                customerReportsDto.setPaidSum(tradeProduct.getTrade().getPaidSum());
                customerReportsDto.setTradedQuantity(tradeProduct.getTradedQuantity());
                customerReportsDto.setBranchName(tradeProduct.getTrade().getBranch().getName());
                customerReportsDto.setTotalSum(tradeProduct.getTrade().getTotalSum());
                customerReportsDto.setPayMethod(tradeProduct.getTrade().getPayMethod().getType());
                customerReportsDto.setPaymentStatus(tradeProduct.getTrade().getPaymentStatus().getStatus());
                customerReportsDtoList.add(customerReportsDto);
            }
        }

        customerReportsDtoList.sort(Comparator.comparing(CustomerReportsDto::getTotalSum).reversed());
        Map<String, Object> data = new HashMap<>();
        data.put("content", customerReportsDtoList);
        data.put("pages", totalPages);
        return new ApiResponse("Found", true, data);
    }

    public ApiResponse customerReports(UUID branchId, UUID customerId, Date startDate, Date endDate) {
        Timestamp to = null;
        Timestamp from = null;

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }
        List<TradeProduct> tradeProducts = tradeProductRepository.findAllByTrade_BranchId(branchId);
        if (tradeProducts.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        if (startDate != null && endDate != null) {
            to = new Timestamp(endDate.getTime());
            from = new Timestamp(startDate.getTime());
        }

        List<TradeProduct> tradeProductList;
        List<CustomerReportsDto> customerReportsDtoList = new ArrayList<>();

        if (customerId != null) {
            if (from != null) {
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_Customer_Id(from, to, customerId);
            } else {
                tradeProductList = tradeProductRepository.findAllByTrade_CustomerId(customerId);
            }
        } else {
            if (from != null) {
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchId(from, to, branchId);
            } else {
                tradeProductList = tradeProductRepository.findAllByTrade_BranchId(branchId);
            }
        }

        if (tradeProductList.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        for (TradeProduct tradeProduct : tradeProductList) {
            if (tradeProduct.getProduct() != null) {
                CustomerReportsDto customerReportsDto = new CustomerReportsDto();
                if (tradeProduct.getTrade().getCustomer() == null) continue;
                customerReportsDto.setCustomerName(tradeProduct.getTrade().getCustomer().getName());
                customerReportsDto.setDate(tradeProduct.getTrade().getPayDate());
                customerReportsDto.setDebt(tradeProduct.getTrade().getDebtSum());
                customerReportsDto.setProduct(tradeProduct.getProduct().getName());
                customerReportsDto.setPaidSum(tradeProduct.getTrade().getPaidSum());
                customerReportsDto.setTradedQuantity(tradeProduct.getTradedQuantity());
                customerReportsDto.setBranchName(tradeProduct.getTrade().getBranch().getName());
                customerReportsDto.setTotalSum(tradeProduct.getTrade().getTotalSum());
                customerReportsDto.setPayMethod(tradeProduct.getTrade().getPayMethod().getType());
                customerReportsDto.setPaymentStatus(tradeProduct.getTrade().getPaymentStatus().getStatus());
                customerReportsDtoList.add(customerReportsDto);
            } else {
                CustomerReportsDto customerReportsDto = new CustomerReportsDto();
                if (tradeProduct.getTrade().getCustomer() == null) continue;
                customerReportsDto.setCustomerName(tradeProduct.getTrade().getCustomer().getName());
                customerReportsDto.setDate(tradeProduct.getTrade().getPayDate());
                customerReportsDto.setDebt(tradeProduct.getTrade().getDebtSum());
                customerReportsDto.setProduct(tradeProduct.getProductTypePrice().getName());
                customerReportsDto.setPaidSum(tradeProduct.getTrade().getPaidSum());
                customerReportsDto.setTradedQuantity(tradeProduct.getTradedQuantity());
                customerReportsDto.setBranchName(tradeProduct.getTrade().getBranch().getName());
                customerReportsDto.setTotalSum(tradeProduct.getTrade().getTotalSum());
                customerReportsDto.setPayMethod(tradeProduct.getTrade().getPayMethod().getType());
                customerReportsDto.setPaymentStatus(tradeProduct.getTrade().getPaymentStatus().getStatus());
                customerReportsDtoList.add(customerReportsDto);
            }
        }

        customerReportsDtoList.sort(Comparator.comparing(CustomerReportsDto::getTotalSum).reversed());
        return new ApiResponse("Found", true, customerReportsDtoList);
    }

    public ApiResponse mostSaleProductsPageNation(UUID branchId, Integer page, Integer limit) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<TradeProduct> allByTradeBranchId = tradeProductRepository.findAllByTrade_BranchIdOrderByCreatedAt(branchId, pageable);

        List<TradeProduct> tradeProductList = allByTradeBranchId.getContent();
        Map<UUID, Double> productAmount = new HashMap<>();
        List<TradeProduct> allByProductId = new LinkedList<>();
        for (TradeProduct tradeProduct : tradeProductList) {
            List<TradeProduct> tradeProducts = new LinkedList<>();
            if (tradeProduct.getProduct() != null) {
                allByProductId = tradeProductRepository.findAllByProduct_Id(tradeProduct.getProduct().getId());
            } else {
                tradeProducts = tradeProductRepository.findAllByTrade_BranchIdAndProductTypePriceId(branchId, tradeProduct.getProductTypePrice().getId());
            }
            double amount = 0;
            if (tradeProduct.getProduct() == null) {
                for (TradeProduct product : tradeProducts) {
                    amount += product.getTradedQuantity();
                    productAmount.put(product.getProductTypePrice().getId(), amount);
                }
            }
            for (TradeProduct product : allByProductId) {
                amount += product.getTradedQuantity();
                productAmount.put(product.getProduct().getId(), amount);
            }
        }
        List<MostSaleProductsDto> mostSaleProductsDtoList = new LinkedList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            Optional<Product> product = productRepository.findById(entry.getKey());
            if (product.isPresent()) {
                MostSaleProductsDto mostSaleProductsDto = new MostSaleProductsDto();
                mostSaleProductsDto.setName(product.get().getName());
                mostSaleProductsDto.setAmount(entry.getValue());
                mostSaleProductsDto.setSalePrice(product.get().getSalePrice());
                mostSaleProductsDto.setBuyPrice(product.get().getBuyPrice());
                mostSaleProductsDto.setBarcode(product.get().getBarcode());
                mostSaleProductsDto.setMeasurement(product.get().getMeasurement().getName());
                mostSaleProductsDto.setBranchName(optionalBranch.get().getName());
                if (product.get().getPhoto() != null) {
                    mostSaleProductsDto.setAttachmentId(product.get().getPhoto().getId());
                }
                mostSaleProductsDtoList.add(mostSaleProductsDto);
            } else {
                Optional<ProductTypePrice> productTypePrice = productTypePriceRepository.findById(entry.getKey());
                MostSaleProductsDto mostSaleProductsDto = new MostSaleProductsDto();
                mostSaleProductsDto.setName(productTypePrice.get().getName());
                mostSaleProductsDto.setAmount(entry.getValue());
                mostSaleProductsDto.setSalePrice(productTypePrice.get().getSalePrice());
                mostSaleProductsDto.setBuyPrice(productTypePrice.get().getBuyPrice());
                mostSaleProductsDto.setBarcode(productTypePrice.get().getBarcode());
                mostSaleProductsDto.setMeasurement(productTypePrice.get().getProduct().getMeasurement().getName());
                mostSaleProductsDto.setBranchName(optionalBranch.get().getName());
                if (productTypePrice.get().getProduct().getPhoto() != null) {
                    mostSaleProductsDto.setAttachmentId(productTypePrice.get().getProduct().getPhoto().getId());
                }
                mostSaleProductsDtoList.add(mostSaleProductsDto);
            }


        }
        mostSaleProductsDtoList.sort(Comparator.comparing(MostSaleProductsDto::getAmount).reversed());
        Map<String, Object> data = new HashMap<>();
        data.put("content", mostSaleProductsDtoList);
        data.put("totalPages", allByTradeBranchId.getTotalPages());
        data.put("totalElements", allByTradeBranchId.getTotalElements());
        return new ApiResponse("Found", true, data);
    }

    public ApiResponse mostSaleProducts(UUID branchId, UUID categoryId, UUID brandId, Date startDate, Date endDate) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }

        List<TradeProduct> tradeProductList = new LinkedList<>();

        if (categoryId == null && brandId == null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByTrade_BranchId(branchId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        } else if (categoryId != null && brandId == null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByProduct_CategoryIdAndTrade_BranchId(categoryId, branchId);
            tradeProductList = tradeProductRepository.findAllByProductTypePrice_Product_CategoryIdAndTrade_BranchId(categoryId, branchId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        } else if (categoryId == null && brandId != null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByProduct_BrandIdAndTrade_BranchId(brandId, branchId);
            tradeProductList = tradeProductRepository.findAllByProductTypePrice_Product_BrandIdAndTrade_BranchId(brandId, branchId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        } else if (categoryId == null && brandId == null && startDate != null && endDate != null) {
            Timestamp from = new Timestamp(startDate.getTime());
            Timestamp to = new Timestamp(endDate.getTime());
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchId(from, to, branchId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        } else if (categoryId != null && brandId == null && startDate != null && endDate != null) {
            Timestamp from = new Timestamp(startDate.getTime());
            Timestamp to = new Timestamp(endDate.getTime());
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchIdAndProduct_CategoryId(from, to, branchId, categoryId);
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchIdAndProductTypePrice_Product_CategoryId(from, to, branchId, categoryId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        } else if (categoryId == null && brandId != null && startDate != null && endDate != null) {
            Timestamp from = new Timestamp(startDate.getTime());
            Timestamp to = new Timestamp(endDate.getTime());
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchIdAndProduct_BrandId(from, to, branchId, brandId);
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchIdAndProductTypePrice_Product_BrandId(from, to, branchId, brandId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        } else if (categoryId != null && brandId != null && startDate == null && endDate == null) {
            tradeProductList = tradeProductRepository.findAllByProduct_CategoryIdAndProduct_BrandIdAndTrade_BranchId(categoryId, brandId, branchId);
            tradeProductList = tradeProductRepository.findAllByProductTypePrice_Product_CategoryIdAndProductTypePrice_Product_BrandIdAndTrade_BranchId(categoryId, brandId, branchId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        } else if (categoryId != null && brandId != null && startDate != null && endDate != null) {
            Timestamp from = new Timestamp(startDate.getTime());
            Timestamp to = new Timestamp(endDate.getTime());
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchIdAndProduct_CategoryIdAndProduct_BrandId(from, to, branchId, categoryId, brandId);
            tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchIdAndProductTypePrice_Product_CategoryIdAndProductTypePrice_Product_BrandId(from, to, branchId, categoryId, brandId);
            if (tradeProductList.isEmpty()) {
                return new ApiResponse("Trade Not Found", false);
            }
        }

        Map<UUID, Double> productAmount = new HashMap<>();
        List<TradeProduct> allByProductId = new LinkedList<>();

        for (TradeProduct tradeProduct : tradeProductList) {

            List<TradeProduct> tradeProducts = new LinkedList<>();
            if (tradeProduct.getProduct() != null) {
                allByProductId = tradeProductRepository.findAllByProduct_Id(tradeProduct.getProduct().getId());
            } else {
                tradeProducts = tradeProductRepository.findAllByTrade_BranchIdAndProductTypePriceId(branchId, tradeProduct.getProductTypePrice().getId());
            }
            double amount = 0;
            if (tradeProduct.getProduct() == null) {
                for (TradeProduct product : tradeProducts) {
                    amount += product.getTradedQuantity();
                    productAmount.put(product.getProductTypePrice().getId(), amount);
                }
            }
            for (TradeProduct product : allByProductId) {
                amount += product.getTradedQuantity();
                productAmount.put(product.getProduct().getId(), amount);
            }
        }
        List<MostSaleProductsDto> mostSaleProductsDtoList = new LinkedList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            Optional<Product> product = productRepository.findById(entry.getKey());
            if (product.isPresent()) {
                MostSaleProductsDto mostSaleProductsDto = new MostSaleProductsDto();
                mostSaleProductsDto.setName(product.get().getName());
                mostSaleProductsDto.setAmount(entry.getValue());
                mostSaleProductsDto.setSalePrice(product.get().getSalePrice());
                mostSaleProductsDto.setBuyPrice(product.get().getBuyPrice());
                mostSaleProductsDto.setBarcode(product.get().getBarcode());
                mostSaleProductsDto.setMeasurement(product.get().getMeasurement().getName());
                mostSaleProductsDto.setBranchName(optionalBranch.get().getName());
                if (product.get().getPhoto() != null) {
                    mostSaleProductsDto.setAttachmentId(product.get().getPhoto().getId());
                }
                mostSaleProductsDtoList.add(mostSaleProductsDto);
            } else {
                Optional<ProductTypePrice> productTypePrice = productTypePriceRepository.findById(entry.getKey());
                MostSaleProductsDto mostSaleProductsDto = new MostSaleProductsDto();
                mostSaleProductsDto.setName(productTypePrice.get().getName());
                mostSaleProductsDto.setAmount(entry.getValue());
                mostSaleProductsDto.setSalePrice(productTypePrice.get().getSalePrice());
                mostSaleProductsDto.setBuyPrice(productTypePrice.get().getBuyPrice());
                mostSaleProductsDto.setBarcode(productTypePrice.get().getBarcode());
                mostSaleProductsDto.setMeasurement(productTypePrice.get().getProduct().getMeasurement().getName());
                mostSaleProductsDto.setBranchName(optionalBranch.get().getName());
                if (productTypePrice.get().getProduct().getPhoto() != null) {
                    mostSaleProductsDto.setAttachmentId(productTypePrice.get().getProduct().getPhoto().getId());
                }
                mostSaleProductsDtoList.add(mostSaleProductsDto);
            }


        }
        mostSaleProductsDtoList.sort(Comparator.comparing(MostSaleProductsDto::getAmount).reversed());
        return new ApiResponse("Found", true, mostSaleProductsDtoList);
    }

    public ApiResponse benefitByBranchReportsPageable(UUID branchId, String date, Date comingStartDate, Date comingEndDate, Integer page, Integer limit) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<TradeProduct> tradeProductPage = tradeProductRepository.findAllByTrade_BranchIdOrderByCreatedAt(branchId, pageable);
        List<TradeProduct> tradeBranchId = tradeProductPage.getContent();
        Map<UUID, Double> productAmount = new HashMap<>();
        for (TradeProduct tradeProduct : tradeBranchId) {
            double amount = 0;
            if (Objects.equals(date, "LAST_DAY")) {
                if (tradeProduct.getProduct() == null) {
                    List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_DAY), Timestamp.valueOf(END_OF_DAY), tradeProduct.getProductTypePrice().getId());
                    if (allByProductId.isEmpty()) {
                        return new ApiResponse("Not Found ", false);
                    }
                    for (TradeProduct product : allByProductId) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_DAY), Timestamp.valueOf(END_OF_DAY), tradeProduct.getProduct().getId());
                    if (allByProductId.isEmpty()) {
                        return new ApiResponse("Not Found ", false);
                    }
                    for (TradeProduct product : allByProductId) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "LAST_WEEK")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(WEEK_START_DAY.atStartOfDay()), Timestamp.valueOf(WEEK_END_DAY.atStartOfDay()), tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(WEEK_START_DAY.atStartOfDay()), Timestamp.valueOf(WEEK_END_DAY.atStartOfDay()), tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "LAST_MONTH")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_MONTH), Timestamp.valueOf(END_OF_MONTH), tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_MONTH), Timestamp.valueOf(END_OF_MONTH), tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "THIS_MONTH")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(THIS_MONTH), currentDay, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(THIS_MONTH), currentDay, tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "LAST_THIRTY_DAY")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(LAST_MONTH), currentDay, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(LAST_MONTH), currentDay, tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "THIS_YEAR")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_YEAR_FOR_THIS), currentDay, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_YEAR_FOR_THIS), currentDay, tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "LAST_YEAR")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_YEAR), Timestamp.valueOf(END_OF_YEAR), tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_YEAR), Timestamp.valueOf(END_OF_YEAR), tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (comingEndDate != null && comingStartDate != null) {
                List<TradeProduct> tradeProductList;
                Timestamp start = new Timestamp(comingStartDate.getTime());
                Timestamp end = new Timestamp(comingEndDate.getTime());
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(start, end, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(start, end, tradeProduct.getProduct().getId());
                    if (!tradeProductList.isEmpty()) {
                        for (TradeProduct product : tradeProductList) {
                            amount += product.getProfit();
                            productAmount.put(tradeProduct.getProduct().getId(), amount);
                        }
                    }
                }

                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else {
                List<TradeProduct> tradeProductList = new ArrayList<>();
                if (tradeProduct.getProduct() != null) {
                    tradeProductList = tradeProductRepository.findAllByProduct_Id(tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                } else if (tradeProduct.getProductTypePrice() != null) {
                    tradeProductList = tradeProductRepository.findAllByProductTypePriceId(tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            }
        }
        List<ProfitByProductDto> profitByProductDtoList = new LinkedList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            Optional<Product> optionalProduct = productRepository.findById(entry.getKey());
            if (optionalProduct.isPresent()) {
                ProfitByProductDto profitByProductDto = new ProfitByProductDto();
                profitByProductDto.setName(optionalProduct.get().getName());
                profitByProductDto.setProfit(entry.getValue());
                profitByProductDtoList.add(profitByProductDto);
            }
            Optional<ProductTypePrice> productTypePrice = productTypePriceRepository.findById(entry.getKey());
            if (productTypePrice.isPresent()) {
                ProfitByProductDto profitByProductDto = new ProfitByProductDto();
                profitByProductDto.setName(productTypePrice.get().getName());
                profitByProductDto.setProfit(entry.getValue());
                profitByProductDtoList.add(profitByProductDto);
            }

        }
        profitByProductDtoList.sort(Comparator.comparing(ProfitByProductDto::getProfit).reversed());
        Map<String, Object> data = new HashMap<>();
        data.put("content", profitByProductDtoList);
        data.put("totalPages", tradeProductPage.getTotalPages());
        data.put("totalElements", tradeProductPage.getTotalElements());
        return new ApiResponse("Found", true, data);
    }

    public ApiResponse dateBenefitAndLostByProductReports(UUID branchId, String date, Date comingStartDate, Date comingEndDate) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }

        List<TradeProduct> tradeBranchId = tradeProductRepository.findAllByTrade_BranchId(optionalBranch.get().getId());
        if (tradeBranchId.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }
        Map<UUID, Double> productAmount = new HashMap<>();
        for (TradeProduct tradeProduct : tradeBranchId) {
            double amount = 0;
            if (Objects.equals(date, "LAST_DAY")) {
                if (tradeProduct.getProduct() == null) {
                    List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_DAY), Timestamp.valueOf(END_OF_DAY), tradeProduct.getProductTypePrice().getId());
                    if (allByProductId.isEmpty()) {
                        return new ApiResponse("Not Found ", false);
                    }
                    for (TradeProduct product : allByProductId) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_DAY), Timestamp.valueOf(END_OF_DAY), tradeProduct.getProduct().getId());
                    if (allByProductId.isEmpty()) {
                        return new ApiResponse("Not Found ", false);
                    }
                    for (TradeProduct product : allByProductId) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "LAST_WEEK")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(WEEK_START_DAY.atStartOfDay()), Timestamp.valueOf(WEEK_END_DAY.atStartOfDay()), tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(WEEK_START_DAY.atStartOfDay()), Timestamp.valueOf(WEEK_END_DAY.atStartOfDay()), tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "LAST_MONTH")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_MONTH), Timestamp.valueOf(END_OF_MONTH), tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_MONTH), Timestamp.valueOf(END_OF_MONTH), tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "THIS_MONTH")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(THIS_MONTH), currentDay, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(THIS_MONTH), currentDay, tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "LAST_THIRTY_DAY")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(LAST_MONTH), currentDay, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(LAST_MONTH), currentDay, tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "THIS_YEAR")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_YEAR_FOR_THIS), currentDay, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_YEAR_FOR_THIS), currentDay, tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (Objects.equals(date, "LAST_YEAR")) {
                List<TradeProduct> tradeProductList;
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(Timestamp.valueOf(START_OF_YEAR), Timestamp.valueOf(END_OF_YEAR), tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(Timestamp.valueOf(START_OF_YEAR), Timestamp.valueOf(END_OF_YEAR), tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else if (comingEndDate != null && comingStartDate != null) {
                List<TradeProduct> tradeProductList;
                Timestamp start = new Timestamp(comingStartDate.getTime());
                Timestamp end = new Timestamp(comingEndDate.getTime());
                if (tradeProduct.getProduct() == null) {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePriceId(start, end, tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                } else {
                    tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductId(start, end, tradeProduct.getProduct().getId());
                    if (!tradeProductList.isEmpty()) {
                        for (TradeProduct product : tradeProductList) {
                            amount += product.getProfit();
                            productAmount.put(tradeProduct.getProduct().getId(), amount);
                        }
                    }
                }

                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            } else {
                List<TradeProduct> tradeProductList = new ArrayList<>();
                if (tradeProduct.getProduct() != null) {
                    tradeProductList = tradeProductRepository.findAllByProduct_Id(tradeProduct.getProduct().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProduct().getId(), amount);
                    }
                } else if (tradeProduct.getProductTypePrice() != null) {
                    tradeProductList = tradeProductRepository.findAllByProductTypePriceId(tradeProduct.getProductTypePrice().getId());
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(tradeProduct.getProductTypePrice().getId(), amount);
                    }
                }
                if (tradeProductList.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
            }
        }
        List<ProfitByProductDto> profitByProductDtoList = new ArrayList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            Optional<Product> optionalProduct = productRepository.findById(entry.getKey());
            if (optionalProduct.isPresent()) {
                ProfitByProductDto profitByProductDto = new ProfitByProductDto();
                profitByProductDto.setName(optionalProduct.get().getName());
                profitByProductDto.setProfit(entry.getValue());
                profitByProductDtoList.add(profitByProductDto);
            }
            Optional<ProductTypePrice> productTypePrice = productTypePriceRepository.findById(entry.getKey());
            if (productTypePrice.isPresent()) {
                ProfitByProductDto profitByProductDto = new ProfitByProductDto();
                profitByProductDto.setName(productTypePrice.get().getName());
                profitByProductDto.setProfit(entry.getValue());
                profitByProductDtoList.add(profitByProductDto);
            }

        }
        profitByProductDtoList.sort(Comparator.comparing(ProfitByProductDto::getProfit).reversed());

        return new ApiResponse("Found", true, profitByProductDtoList);
    }

    public ApiResponse benefitAndLostByCategoryReports(UUID branchId, String date, Date comingStartDate, Date comingEndDate) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }

        Branch branch = optionalBranch.get();
        List<Category> categoryList = categoryRepository.findAllByBusiness_Id(branch.getBusiness().getId());

        Map<UUID, Double> productAmount = new HashMap<>();

        for (Category category : categoryList) {
            double amount = 0;

            List<TradeProduct> tradeProductList;

            if (Objects.equals(date, "LAST_DAY")) {
                tradeProductList = getTradeProductListForDay(category.getId(), START_OF_DAY, END_OF_DAY);
            } else if (Objects.equals(date, "LAST_WEEK")) {
                tradeProductList = getTradeProductListForWeek(category.getId(), WEEK_START_DAY.atStartOfDay(), WEEK_END_DAY.atStartOfDay());
            } else if (Objects.equals(date, "LAST_MONTH")) {
                tradeProductList = getTradeProductListForMonth(category.getId(), START_OF_MONTH, END_OF_MONTH);
            } else if (Objects.equals(date, "THIS_MONTH")) {
                tradeProductList = getTradeProductListForMonth(category.getId(), THIS_MONTH, currentDay.toLocalDateTime());
            } else if (Objects.equals(date, "LAST_THIRTY_DAY")) {
                tradeProductList = getTradeProductListForMonth(category.getId(), LAST_MONTH, currentDay.toLocalDateTime());
            } else if (Objects.equals(date, "THIS_YEAR")) {
                tradeProductList = getTradeProductListForYear(category.getId(), START_OF_YEAR_FOR_THIS, currentDay.toLocalDateTime());
            } else if (Objects.equals(date, "LAST_YEAR")) {
                tradeProductList = getTradeProductListForYear(category.getId(), START_OF_YEAR, END_OF_YEAR);
            } else if (comingEndDate != null && comingStartDate != null) {
                tradeProductList = getTradeProductListForDateRange(category.getId(), comingStartDate, comingEndDate);
            } else {
                tradeProductList = getAllTradeProductsForCategory(category.getId());
            }

            for (TradeProduct product : tradeProductList) {
                amount += product.getProfit();
            }

            productAmount.put(category.getId(), amount);
        }

        List<ProfitByCategoryDto> profitByCategoryDtoList = new ArrayList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            ProfitByCategoryDto profitByCategoryDto = new ProfitByCategoryDto();
            Optional<Category> optionalCategory = categoryRepository.findById(entry.getKey());
            profitByCategoryDto.setCategoryName(optionalCategory.get().getName());
            profitByCategoryDto.setProfit(entry.getValue());
            profitByCategoryDtoList.add(profitByCategoryDto);
        }

        profitByCategoryDtoList.sort(Comparator.comparing(ProfitByCategoryDto::getProfit).reversed());
        return new ApiResponse("Found", true, profitByCategoryDtoList);
    }

    private List<TradeProduct> getTradeProductListForDay(UUID categoryId, LocalDateTime startDate, LocalDateTime endDate) {
        return tradeProductRepository.findAllByCreatedAtBetweenAndProduct_CategoryId(
                Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), categoryId);
    }

    private List<TradeProduct> getTradeProductListForWeek(UUID categoryId, LocalDateTime startDate, LocalDateTime endDate) {
        return tradeProductRepository.findAllByCreatedAtBetweenAndProduct_CategoryId(
                Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), categoryId);
    }

    private List<TradeProduct> getTradeProductListForMonth(UUID categoryId, LocalDateTime startDate, LocalDateTime endDate) {
        return tradeProductRepository.findAllByCreatedAtBetweenAndProduct_CategoryId(
                Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), categoryId);
    }

    private List<TradeProduct> getTradeProductListForYear(UUID categoryId, LocalDateTime startDate, LocalDateTime endDate) {
        return tradeProductRepository.findAllByCreatedAtBetweenAndProduct_CategoryId(
                Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), categoryId);
    }

    private List<TradeProduct> getTradeProductListForDateRange(UUID categoryId, Date startDate, Date endDate) {
        return tradeProductRepository.findAllByCreatedAtBetweenAndProduct_CategoryId(
                new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), categoryId);
    }

    private List<TradeProduct> getAllTradeProductsForCategory(UUID categoryId) {
        return tradeProductRepository.findAllByProduct_CategoryId(categoryId);
    }

    public ApiResponse benefitAndLostByBrandReports(UUID branchId, String date, Date comingStartDate, Date comingEndDate) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        UUID id = optionalBranch.get().getBusiness().getId();
        List<Brand> brandList = brandRepository.findAllByBusiness_Id(id);
        Map<UUID, Double> productAmount = new HashMap<>();
        for (Brand brand : brandList) {
            double amount = 0;
            if (Objects.equals(date, "LAST_DAY")) {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp.valueOf(START_OF_DAY), Timestamp.valueOf(END_OF_DAY), brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(brand.getId(), amount);
                    }
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(Timestamp.valueOf(START_OF_DAY), Timestamp.valueOf(END_OF_DAY), brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "LAST_WEEK")) {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp.valueOf(WEEK_START_DAY.atStartOfDay()), Timestamp.valueOf(WEEK_END_DAY.atStartOfDay()), brand.getId());
                if (!tradeProductList.isEmpty()) for (TradeProduct product : tradeProductList) {
                    amount += product.getProfit();
                    productAmount.put(brand.getId(), amount);
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(Timestamp.valueOf(WEEK_START_DAY.atStartOfDay()), Timestamp.valueOf(WEEK_END_DAY.atStartOfDay()), brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "LAST_MONTH")) {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp.valueOf(START_OF_MONTH), Timestamp.valueOf(END_OF_MONTH), brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(brand.getId(), amount);
                    }
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(Timestamp.valueOf(START_OF_MONTH), Timestamp.valueOf(END_OF_MONTH), brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "THIS_MONTH")) {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp.valueOf(THIS_MONTH), currentDay, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct tradeProduct : tradeProductList) {
                        for (TradeProduct product : tradeProductList) {
                            amount += product.getProfit();
                            productAmount.put(brand.getId(), amount);
                        }
                    }
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(Timestamp.valueOf(THIS_MONTH), currentDay, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "THIS_YEAR")) {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp.valueOf(START_OF_YEAR_FOR_THIS), currentDay, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct tradeProduct : tradeProductList) {
                        for (TradeProduct product : tradeProductList) {
                            amount += product.getProfit();
                            productAmount.put(brand.getId(), amount);
                        }
                    }
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(Timestamp.valueOf(START_OF_YEAR_FOR_THIS), currentDay, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "LAST_THIRTY_DAY")) {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp.valueOf(LAST_MONTH), currentDay, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(brand.getId(), amount);
                    }
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(Timestamp.valueOf(LAST_MONTH), currentDay, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else if (Objects.equals(date, "LAST_YEAR")) {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp.valueOf(START_OF_YEAR), Timestamp.valueOf(END_OF_YEAR), brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(brand.getId(), amount);
                    }
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(Timestamp.valueOf(START_OF_YEAR), Timestamp.valueOf(END_OF_YEAR), brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else if (comingEndDate != null && comingStartDate != null) {
                Timestamp start = new Timestamp(comingStartDate.getTime());
                Timestamp end = new Timestamp(comingEndDate.getTime());
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProduct_BrandId(start, end, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(brand.getId(), amount);
                    }
                }
                tradeProductList = tradeProductRepository.findAllByCreatedAtBetweenAndProductTypePrice_Product_BrandId(start, end, brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            } else {
                List<TradeProduct> tradeProductList;
                tradeProductList = tradeProductRepository.findAllByProduct_BrandId(brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(brand.getId(), amount);
                    }
                }
                tradeProductList = tradeProductRepository.findAllByProductTypePrice_Product_BrandId(brand.getId());
                if (!tradeProductList.isEmpty()) {
                    for (TradeProduct product : tradeProductList) {
                        amount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getProduct().getBrand().getId(), amount);
                    }
                }
            }
        }
        List<ProfitByCategoryDto> profitByCategoryDtoList = new ArrayList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            ProfitByCategoryDto profitByCategoryDto = new ProfitByCategoryDto();
            Optional<Brand> optionalBrand = brandRepository.findById(entry.getKey());
            profitByCategoryDto.setCategoryName(optionalBrand.get().getName());
            profitByCategoryDto.setProfit(entry.getValue());
            profitByCategoryDtoList.add(profitByCategoryDto);
        }
        profitByCategoryDtoList.sort(Comparator.comparing(ProfitByCategoryDto::getProfit).reversed());
        return new ApiResponse("Found", true, profitByCategoryDtoList);
    }

    public ApiResponse benefitAndLostByCustomerReports(UUID branchId, String date, Date comingStartDate, Date comingEndDate) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        Map<UUID, Double> productAmount = new HashMap<>();
        List<Customer> customerList = customerRepository.findAllByBranchesIdAndActiveIsTrueOrBranchesIdAndActiveIsNull(branchId, branchId);
        for (Customer customer : customerList) {
            double amount = 0;
            if (Objects.equals(date, "LAST_DAY")) {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp.valueOf(START_OF_DAY), Timestamp.valueOf(END_OF_DAY), customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {

                    amount += product.getProfit();
                    productAmount.put(product.getTrade().getCustomer().getId(), amount);
                }
            } else if (Objects.equals(date, "LAST_WEEK")) {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp.valueOf(WEEK_START_DAY.atStartOfDay()), Timestamp.valueOf(WEEK_END_DAY.atStartOfDay()), customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {
                    amount += product.getProfit();
                    productAmount.put(product.getProduct().getBrand().getId(), amount);
                }
            } else if (Objects.equals(date, "LAST_MONTH")) {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp.valueOf(START_OF_MONTH), Timestamp.valueOf(END_OF_MONTH), customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {

                    amount += product.getProfit();
                    productAmount.put(product.getProduct().getBrand().getId(), amount);
                }
            } else if (Objects.equals(date, "THIS_MONTH")) {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp.valueOf(THIS_MONTH), currentDay, customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {

                    amount += product.getProfit();
                    productAmount.put(product.getTrade().getCustomer().getId(), amount);
                }
            } else if (Objects.equals(date, "THIS_YEAR")) {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp.valueOf(START_OF_YEAR_FOR_THIS), currentDay, customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {
                    amount += product.getProfit();
                    productAmount.put(product.getTrade().getCustomer().getId(), amount);
                }
            } else if (Objects.equals(date, "LAST_THIRTY_DAY")) {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp.valueOf(LAST_MONTH), currentDay, customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {
                    amount += product.getProfit();
                    productAmount.put(product.getTrade().getCustomer().getId(), amount);
                }
            } else if (Objects.equals(date, "LAST_YEAR")) {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp.valueOf(START_OF_YEAR), Timestamp.valueOf(END_OF_YEAR), customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {
                    amount += product.getProfit();
                    productAmount.put(product.getTrade().getCustomer().getId(), amount);
                }
            } else if (comingEndDate != null && comingStartDate != null) {
                Timestamp start = new Timestamp(comingStartDate.getTime());
                Timestamp end = new Timestamp(comingEndDate.getTime());
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_CustomerId(start, end, customer.getId());
                if (allByProductId.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                for (TradeProduct product : allByProductId) {

                    amount += product.getProfit();
                    productAmount.put(product.getTrade().getCustomer().getId(), amount);
                }
            } else {
                List<TradeProduct> allByProductId = tradeProductRepository.findAllByTrade_CustomerId(customer.getId());
                if (!allByProductId.isEmpty()) {
                    for (TradeProduct product : allByProductId) {
                        amount += product.getProfit();
                        productAmount.put(product.getTrade().getCustomer().getId(), amount);
                    }
                }
            }
        }
        List<ProfitByCategoryDto> profitByCategoryDtoList = new ArrayList<>();
        for (Map.Entry<UUID, Double> entry : productAmount.entrySet()) {
            ProfitByCategoryDto profitByCategoryDto = new ProfitByCategoryDto();
            Optional<Customer> optionalCustomer = customerRepository.findById(entry.getKey());
            profitByCategoryDto.setCategoryName(optionalCustomer.get().getName());
            profitByCategoryDto.setProfit(entry.getValue());
            profitByCategoryDtoList.add(profitByCategoryDto);
        }
        profitByCategoryDtoList.sort(Comparator.comparing(ProfitByCategoryDto::getProfit).reversed());
        return new ApiResponse("Found", true, profitByCategoryDtoList);
    }

    public ApiResponse productionReports(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch Not Found", false);
        }
        List<Production> productionList = productionRepository.findAllByBranchIdAndDoneIsTrueOrderByCreatedAtDesc(branchId);

        return new ApiResponse("Found", true, productionList);
    }

    public ApiResponse productsReport(UUID customerId, UUID branchId, String date, Date startDate, Date endDate) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("not found branch", false);
        }

        if (customerId != null) {
            Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
            if (optionalCustomer.isPresent()) {
                List<ProductReportDto> productReport = getProductReport(customerId, branchId, date, startDate, endDate, true);
                if (productReport != null && productReport.isEmpty()) {
                    return new ApiResponse("Not Found", false);
                }
                return new ApiResponse("all", true, productReport);
            }
        }
        List<ProductReportDto> productReport = getProductReport(customerId, branchId, date, startDate, endDate, false);
        if (productReport != null && productReport.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }
        return new ApiResponse("all", true, productReport);
    }

    private List<ProductReportDto> getProductReport(UUID customerId, UUID branchId, String date, Date startDate, Date endDate, boolean isByCustomerId) {
        Map<UUID, Double> productAmount = new HashMap<>();
        List<ProductReportDto> all = new ArrayList<>();
        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);


        if (startDate != null && endDate != null) {
            startTimestamp = new Timestamp(startDate.getTime());
            endTimestamp = new Timestamp(endDate.getTime());
        }

        switch (date) {
            case ("LAST_DAY"):
                startTimestamp = Timestamp.valueOf(START_OF_DAY);
                endTimestamp = Timestamp.valueOf(END_OF_DAY);
                break;
            case ("LAST_WEEK"):
                startTimestamp = Timestamp.valueOf(WEEK_START_DAY.atStartOfDay());
                endTimestamp = Timestamp.valueOf(WEEK_END_DAY.atStartOfDay());
                break;
            case ("LAST_THIRTY_DAY"):
                startTimestamp = Timestamp.valueOf(END_OF_MONTH);
                endTimestamp = currentDay;
                break;
            case ("THIS_YEAR"):
                startTimestamp = Timestamp.valueOf(START_OF_YEAR_FOR_THIS);
                endTimestamp = currentDay;
                break;
            case ("LAST_YEAR"):
                startTimestamp = Timestamp.valueOf(START_OF_YEAR);
                endTimestamp = Timestamp.valueOf(END_OF_MONTH);
                break;
            case ("LAST_MONTH"):
                startTimestamp = Timestamp.valueOf(START_OF_MONTH);
                endTimestamp = Timestamp.valueOf(END_OF_MONTH);
                break;
            case ("THIS_MONTH"):
                startTimestamp = Timestamp.valueOf(THIS_MONTH);
                endTimestamp = currentDay;
                break;
            case ("ALL"):
                List<Trade> allByCustomerId = tradeRepository.findAllByCustomer_Id(customerId);
                for (Trade trade : allByCustomerId) {
                    List<TradeProduct> allTradeCustomerId = tradeProductRepository.findAllByTradeId(trade.getId());
                    for (TradeProduct tradeProduct : allTradeCustomerId) {
                        if (tradeProduct.getProduct() != null) {
                            List<TradeProduct> allByProductId = tradeProductRepository.findAllByProduct_IdAndTrade_CustomerId(tradeProduct.getProduct().getId(), customerId);
                            double totalAmount = 0;
                            for (TradeProduct product : allByProductId) {
                                totalAmount += product.getProfit();
                                productAmount.put(product.getProduct().getId(), totalAmount);
                            }
                        }
                    }
                    for (TradeProduct tradeProduct : allTradeCustomerId) {
                        if (tradeProduct.getProductTypePrice() != null) {
                            List<TradeProduct> allByProductTypePriceId = tradeProductRepository.findAllByProductTypePriceIdAndTrade_CustomerId(tradeProduct.getProductTypePrice().getId(), customerId);
                            double totalAmount = 0;
                            for (TradeProduct product : allByProductTypePriceId) {
                                totalAmount += product.getProfit();
                                productAmount.put(product.getProductTypePrice().getId(), totalAmount);
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        if (isByCustomerId && !date.equals("ALL")) {
            List<Trade> allByCustomerId = tradeRepository.findAllByCreatedAtBetweenAndCustomer_Id(startTimestamp, endTimestamp, customerId);
            for (Trade trade : allByCustomerId) {
                List<TradeProduct> allTradeCustomerId = tradeProductRepository.findAllByTradeId(trade.getId());
                for (TradeProduct tradeProduct : allTradeCustomerId) {
                    if (tradeProduct.getProduct() != null) {
                        List<TradeProduct> allByProductId = tradeProductRepository.findAllByProduct_IdAndTrade_CustomerId(tradeProduct.getProduct().getId(), customerId);
                        double totalAmount = 0;
                        for (TradeProduct product : allByProductId) {
                            totalAmount += product.getProfit();
                            productAmount.put(product.getProduct().getId(), totalAmount);
                        }
                    }
                }
                for (TradeProduct tradeProduct : allTradeCustomerId) {
                    if (tradeProduct.getProductTypePrice() != null) {
                        List<TradeProduct> allByProductTypePriceId = tradeProductRepository.findAllByProductTypePriceIdAndTrade_CustomerId(tradeProduct.getProductTypePrice().getId(), customerId);
                        double totalAmount = 0;
                        for (TradeProduct product : allByProductTypePriceId) {
                            totalAmount += product.getProfit();
                            productAmount.put(product.getProductTypePrice().getId(), totalAmount);
                        }
                    }
                }
            }
        } else if (!date.equals("ALL")) {
            if (optionalBranch.isEmpty()) {
                return null;
            }
            List<TradeProduct> allTradeBranch = tradeProductRepository.findAllByCreatedAtBetweenAndTrade_BranchId(startTimestamp, endTimestamp, branchId);
            for (TradeProduct tradeProduct : allTradeBranch) {
                if (tradeProduct.getProduct() != null) {
                    List<TradeProduct> allByProductId = tradeProductRepository.findAllByProduct_Id(tradeProduct.getProduct().getId());
                    double totalAmount = 0;
                    for (TradeProduct product : allByProductId) {
                        totalAmount += product.getProfit();
                        productAmount.put(product.getProduct().getId(), totalAmount);
                    }
                }
            }
            for (TradeProduct tradeProduct : allTradeBranch) {
                if (tradeProduct.getProductTypePrice() != null) {
                    List<TradeProduct> allByProductTypePriceId = tradeProductRepository.findAllByProductTypePriceId(tradeProduct.getProductTypePrice().getId());
                    double totalAmount = 0;
                    for (TradeProduct product : allByProductTypePriceId) {
                        totalAmount += product.getProfit();
                        productAmount.put(product.getProductTypePrice().getId(), totalAmount);
                    }
                }
            }
        }

        for (Map.Entry<UUID, Double> productAmounts : productAmount.entrySet()) {
            Optional<Product> optionalProduct = productRepository.findById(productAmounts.getKey());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                ProductReportDto productReportDto = new ProductReportDto();
                productReportDto.setName(product.getName());
                productReportDto.setBranch(product.getBranch().get(0).getName());
                productReportDto.setBarcode(product.getBarcode());
                productReportDto.setSalePrice(product.getSalePrice());
                productReportDto.setBuyPrice(product.getBuyPrice());
                productReportDto.setAmount(productAmounts.getValue());
                all.add(productReportDto);
            } else {
                Optional<ProductTypePrice> productTypePrice = productTypePriceRepository.findById(productAmounts.getKey());
                if (productTypePrice.isPresent()) {
                    ProductReportDto productReportDto = new ProductReportDto();
                    productReportDto.setName(productTypePrice.get().getName());
                    productReportDto.setBranch(optionalBranch.get().getName());
                    productReportDto.setBarcode(productTypePrice.get().getBarcode());
                    productReportDto.setSalePrice(productTypePrice.get().getSalePrice());
                    productReportDto.setBuyPrice(productTypePrice.get().getBuyPrice());
                    productReportDto.setAmount(productAmounts.getValue());
                    all.add(productReportDto);
                }
            }
        }
        return all;
    }

    public ApiResponse lidReport(UUID businessId) {
        List<Lid> allLid = lidRepository.findAllByBusiness_Id(businessId);
        List<LidStatus> allStatus = lidStatusRepository.findAllByBusiness_IdOrderBySortAsc(businessId);
        List<Source> allSource = sourceRepository.findAllByBusiness_Id(businessId);

        LidReportDto lidReportDto = new LidReportDto();
        lidReportDto.setTotalLid(allLid.size());

        List<StatusReportDto> statusList = new ArrayList<>();
        List<SourceReportDto> sourceList = new ArrayList<>();

        for (LidStatus status : allStatus) {
            List<Lid> all = lidRepository.findAllByLidStatusId(status.getId());
            StatusReportDto statusReportDto = new StatusReportDto();
            statusReportDto.setName(status.getName());
            statusReportDto.setAmount(all.size());
            statusList.add(statusReportDto);
        }

        for (Source source : allSource) {
            List<Lid> allBySourceId = lidRepository.findAllBySourceId(source.getId());
            SourceReportDto sourceReportDto = new SourceReportDto();
            sourceReportDto.setAmount(allBySourceId.size());
            sourceReportDto.setName(source.getName());
            sourceList.add(sourceReportDto);
        }
        List<Lid> done = lidRepository.findAllByLidStatus_OrginalName("Done");

        lidReportDto.setStatusReportDtos(statusList);
        lidReportDto.setSourceReportDtos(sourceList);
        lidReportDto.setTotalSale(done.size());

        return new ApiResponse("all", true, lidReportDto);
    }

    public ApiResponse bestSellerReport(UUID businessId) {

        List<User> allUserId = userRepository.findAllByBusiness_Id(businessId);
        List<TraderBestDto> traderBestDtoList = new ArrayList<>();

        for (User user : allUserId) {
            List<Trade> all = tradeRepository.findAllByTrader_Id(user.getId());
            if (!all.isEmpty()) {
                int size = all.size();

                TraderBestDto traderBestDto = new TraderBestDto();
                traderBestDto.setFio(user.getFirstName() + " " + user.getLastName());
                if (user.getPhoto() != null) {
                    traderBestDto.setPhotoId(user.getPhoto().getId());
                }
                double total = 0;
                for (Trade trade : all) {
                    total += trade.getTotalSum();
                }
                if (total > 0) {
                    traderBestDto.setAverage(total / size);
                }
                traderBestDtoList.add(traderBestDto);
            }
        }

        traderBestDtoList.sort(Comparator.comparing(TraderBestDto::getAverage).reversed());

        return new ApiResponse("all traders", true, traderBestDtoList);
    }

    public ApiResponse projectReport(UUID branchId, UUID businessId) {
        Branch branch = null;
        Business business = null;
        List<Task> taskList = null;
        if (branchId != null) {
            Optional<Branch> optionalBranch = branchRepository.findById(branchId);
            if (optionalBranch.isPresent()) {
                branch = optionalBranch.get();
            }
        }
        if (businessId != null) {
            Optional<Business> optionalBusiness = businessRepository.findById(businessId);
            if (optionalBusiness.isPresent()) {
                business = optionalBusiness.get();
            }
        }
        if (branch == null && business == null) {
            return new ApiResponse("Not Found", false);
        }
        List<Project> projectList = null;
        if (branch != null) {
            projectList = projectRepository.findAllByBranchId(branchId);
            taskList = taskRepository.findAllByBranchId(branchId);
        } else {
            projectList = projectRepository.findAllByBranch_BusinessId(businessId);
            taskList = taskRepository.findAllByBranch_BusinessId(businessId);

        }

        double projectAmount = 0;
        List<ProjectDTOS> projectDTOSList = new ArrayList<>();
        for (Project project : projectList) {

            int i = taskRepository.countAllByProjectId(project.getId());
            List<Task> tasks = taskRepository.findAllByProjectId(project.getId());
            double taskAmount = 0;
            for (Task task : tasks) {
                taskAmount += task.getTaskPrice();
            }
            ProjectDTOS projectDTOS = new ProjectDTOS();
            projectDTOS.setProjectName(project.getName());
            projectDTOS.setProjectTaskCount(i);
            projectDTOS.setProjectAmount(project.getBudget());
            projectDTOS.setTasksAmount(taskAmount);
            projectDTOSList.add(projectDTOS);

            projectAmount += project.getBudget();
        }

        double taskAmount = 0;
        for (Task task : taskList) {
            taskAmount += task.getTaskPrice();
        }


        ProjectReportDto projectReportDto = new ProjectReportDto();
        projectReportDto.setProjectQuantity(projectList.size());
        projectReportDto.setProjectAmount(projectAmount);
        projectReportDto.setTasksAmount(taskAmount);
        projectReportDto.setProjectDTOSList(projectDTOSList);

        return new ApiResponse("Found", true, projectReportDto);
    }

    public ApiResponse getLidTradeReport(UUID businessId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);

        if (businessId == null) {
            return new ApiResponse("business id is null", false);
        }

        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found by business id", false);
        }

        Page<Trade> allTrade = tradeRepository.findAllByBranch_Business_IdAndLidIsTrue(businessId, pageable);

        List<TradeLidDto> lidMapperDto = tradeLidMapper.toDto(allTrade.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("getAllTrade", lidMapperDto);
        response.put("currentPage", allTrade.getNumber());
        response.put("totalItems", allTrade.getTotalElements());
        response.put("totalPages", allTrade.getTotalPages());

        return new ApiResponse("all trade lid", true, response);
    }


    public ApiResponse top10Supplier(UUID branchId, Date startDate, Date endDate) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch not found", false);
        }
        List<Object[]> results;
        if (startDate != null && endDate != null) {
            Timestamp startTimestamp = new Timestamp(startDate.getTime());
            Timestamp endTimestamp = new Timestamp(endDate.getTime());
            results = purchaseRepository.findTop10SuppliersByPurchaseAndDate(branchId, startTimestamp, endTimestamp);
        } else {
            results = purchaseRepository.findTop10SuppliersByPurchase(branchId);
        }
        if (results.isEmpty()) {
            return new ApiResponse("Not found", false);
        }
        List<Map<String, Object>> responses = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> response = new HashMap<>();
            String supplierName = (String) result[0];
            String supplierPhoneNumber = (String) result[1];
            Double amount = (Double) result[2];
            response.put("name", supplierName);
            response.put("number", supplierPhoneNumber);
            response.put("amount", amount);
            responses.add(response);
        }

        return new ApiResponse("Found", true, responses);
    }

    public ApiResponse getCheckout(UUID branchId, UUID businessId) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        List<GetCheckoutDto> getCheckoutDtoList = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        double totalSumma = 0;
        double totalSumma1 = 0;
        Branch branch = optionalBranch.get();

        Timestamp fromToday = Timestamp.valueOf(TODAY_END.minusDays(1));
        Timestamp toToday = Timestamp.valueOf(TODAY_END);

        Optional<PaymentMethod> plastikKartaOptional = payMethodRepository.findByTypeAndBusiness_id("PlastikKarta", branch.getBusiness().getId());
        Optional<PaymentMethod> bankOrqaliOptional = payMethodRepository.findByTypeAndBusiness_id("BankOrqali", branch.getBusiness().getId());
        Optional<PaymentMethod> naqtOptional = payMethodRepository.findByTypeAndBusiness_id("Naqd", branch.getBusiness().getId());
        List<PaymentMethod> paymentMethodList = new ArrayList<>();
        plastikKartaOptional.ifPresent(paymentMethodList::add);
        bankOrqaliOptional.ifPresent(paymentMethodList::add);
        if (naqtOptional.isEmpty()) {
            return new ApiResponse("tulov turidagi naqd belgisi topilmayapdi", false);
        }
        PaymentMethod naqd = naqtOptional.get();

        double todayOutlay = 0;
        double todayProfit = 0;

        for (int i = 0; i < 15; i++) {
            Double totalSum = null;
            Double totalDebtSum = null;
            Double totalOutlaySum = null;
            Double totalRepaymentDebtSum = null;
            Double payType = null;
            double totalDebtSum1 = 0;
            double totalOutlaySum1 = 0;
            double totalRepaymentDebtSum1 = 0;
            double totalTradeSumma = 0;
            double totalPayType = 0;


            Timestamp from = Timestamp.valueOf(TODAY_END.minusDays(i + 1));
            Timestamp to = Timestamp.valueOf(TODAY_END.minusDays(i));

            if (businessId != null) {
                totalSum = tradeRepository.totalSumByBusiness(businessId, from, to);
                totalDebtSum = customerDebtRepository.totalCustomerDebtSumByBusiness(businessId, from, to);
                totalOutlaySum = outlayRepository.outlayByCreatedAtBetweenAndBusinessIdAndPaymentMethod(businessId, naqd.getId(), from, to);
                totalRepaymentDebtSum = repaymentDebtRepository.getTotalSumByBusinessWithPayDate(businessId, "Naqd", from, to);
                Double sum = repaymentDebtRepository.getTotalSumByBusiness(businessId, "Naqd", from, to);
                if (totalRepaymentDebtSum != null) {
                    totalRepaymentDebtSum += sum != null ? sum : 0;
                }
                for (PaymentMethod paymentMethod : paymentMethodList) {
                    payType = tradeRepository.totalPaymentByBusiness(paymentMethod.getId(), businessId, from, to);
                    totalPayType += payType != null ? payType : 0;
                }

            } else {
                totalSum = tradeRepository.totalSum(branchId, from, to);
                totalDebtSum = customerDebtRepository.totalCustomerDebtSum(branchId, from, to);
                totalOutlaySum = outlayRepository.outlayByCreatedAtBetweenAndPaymentMethodByBranch(branchId, naqd.getId(), from, to);
                totalRepaymentDebtSum = repaymentDebtRepository.getTotalSumWithPayDate(branchId, "Naqd", from, to);
                Double sum = repaymentDebtRepository.getTotalSum(branchId, "Naqd", from, to);
                if (totalRepaymentDebtSum != null) {
                    totalRepaymentDebtSum += sum != null ? sum : 0;
                }
                for (PaymentMethod paymentMethod : paymentMethodList) {
                    payType = tradeRepository.totalPayment(paymentMethod.getId(), branchId, from, to);
                    totalPayType += payType != null ? payType : 0;
                }
            }
            totalDebtSum1 = totalDebtSum != null ? totalDebtSum : 0;
            totalOutlaySum1 = totalOutlaySum != null ? totalOutlaySum : 0;
            totalRepaymentDebtSum1 = totalRepaymentDebtSum != null ? totalRepaymentDebtSum : 0;
            totalTradeSumma = totalSum != null ? totalSum : 0;

            GetCheckoutDto getCheckoutDto = new GetCheckoutDto();
//            if (totalTradeSumma != 0 ) {
            getCheckoutDto.setTotalDebt(totalRepaymentDebtSum1);
            getCheckoutDto.setTotalOutlay(totalOutlaySum1);
            getCheckoutDto.setTotalTradeSum(totalTradeSumma);
            getCheckoutDto.setTotalCash((totalTradeSumma - totalDebtSum1 - totalOutlaySum1 - totalPayType) + totalRepaymentDebtSum1);
            if (i == 0) {
                totalSumma += (totalTradeSumma - totalDebtSum1 - totalOutlaySum1 - totalPayType) + totalRepaymentDebtSum1;
            }
//            }
            getCheckoutDto.setTimestamp(to);
            getCheckoutDtoList.add(getCheckoutDto);
        }
        if (businessId != null) {
            Double aDouble = tradeRepository.totalProfitByBusinessId(businessId, fromToday, toToday);
            Double aDouble1 = outlayRepository.outlayByCreatedAtBetweenAndBusinessId(businessId, fromToday, toToday);
            todayOutlay = aDouble1 != null ? aDouble1 : 0;
            todayProfit = aDouble != null ? aDouble : 0;


        } else {
            Double aDouble = tradeRepository.totalProfit(branchId, fromToday, toToday);
            todayProfit = aDouble != null ? aDouble : 0;

            Double aDouble1 = outlayRepository.outlayByCreatedAtBetweenAndBranchId(fromToday, toToday, branchId);
            todayOutlay = aDouble1 != null ? aDouble1 : 0;

        }
        getCheckoutDtoList.sort(Comparator.comparing(GetCheckoutDto::getTimestamp));

        response.put("data", getCheckoutDtoList);
        response.put("totalSumma", totalSumma);
        response.put("totalOutlay", todayOutlay);
        response.put("totalProfit", todayProfit);

        return new ApiResponse("Kassadagi pul", true, response);

    }

    public ApiResponse getIncrease(UUID businessId, UUID branchId, String date, Date startDate, Date endDate) {

        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;

        Timestamp startTimestamp1 = null;
        Timestamp endTimestamp1 = null;

        double nowTotalProductSum = 0;
        double lastTotalProductSum = 0;

        double nowTotalSumDouble = 0;
        double lastTotalSumDouble = 0;

        double nowTotalProfitDouble = 0;
        double lastTotalProfitDouble = 0;

        Integer nowTotalCustomer = null;
        Integer lastTotalCustomer = null;

        int nowTotalCustomerInt = 0;
        int lastTotalCustomerInt = 0;

        if (startDate != null && endDate != null) {
            startTimestamp = new Timestamp(startDate.getTime());
            endTimestamp = new Timestamp(endDate.getTime());
        }

        if (date != null) {
            switch (date) {
                case "LAST_WEEK" -> {
                    LocalDate today = LocalDate.now();
                    LocalDate sevenDaysAgo = today.minusDays(7);

                    startTimestamp = Timestamp.valueOf(today.atStartOfDay());
                    endTimestamp = Timestamp.valueOf(sevenDaysAgo.atStartOfDay());

                    LocalDate eightDaysAgo = today.minusDays(8);
                    LocalDate fifteenDaysAgo = today.minusDays(15);

                    startTimestamp1 = Timestamp.valueOf(eightDaysAgo.atStartOfDay());
                    endTimestamp1 = Timestamp.valueOf(fifteenDaysAgo.atStartOfDay());
                }
                case "LAST_MONTH" -> {

                    LocalDate today = LocalDate.now();
                    LocalDate sevenDaysAgo = today.minusDays(30);

                    startTimestamp = Timestamp.valueOf(today.atStartOfDay());
                    endTimestamp = Timestamp.valueOf(sevenDaysAgo.atStartOfDay());

                    LocalDate eightDaysAgo = today.minusDays(31);
                    LocalDate fifteenDaysAgo = today.minusDays(61);

                    startTimestamp1 = Timestamp.valueOf(eightDaysAgo.atStartOfDay());
                    endTimestamp1 = Timestamp.valueOf(fifteenDaysAgo.atStartOfDay());

                }
                case "LAST_YEAR" -> {
                    LocalDate today = LocalDate.now();
                    LocalDate sevenDaysAgo = today.minusDays(365);

                    startTimestamp = Timestamp.valueOf(today.atStartOfDay());
                    endTimestamp = Timestamp.valueOf(sevenDaysAgo.atStartOfDay());

                    LocalDate eightDaysAgo = today.minusDays(366);
                    LocalDate fifteenDaysAgo = today.minusDays(731);

                    startTimestamp1 = Timestamp.valueOf(eightDaysAgo.atStartOfDay());
                    endTimestamp1 = Timestamp.valueOf(fifteenDaysAgo.atStartOfDay());
                }
                default -> {
                    startTimestamp = Timestamp.valueOf(START_OF_DAY);
                    endTimestamp = Timestamp.valueOf(END_OF_DAY);

                    LocalDate lastDay = START_OF_DAY.minusDays(1).toLocalDate();
                    LocalDate lastDayMinusOne = END_OF_DAY.minusDays(1).toLocalDate();

                    startTimestamp1 = Timestamp.valueOf(lastDay.atStartOfDay());
                    endTimestamp1 = Timestamp.valueOf(lastDayMinusOne.atStartOfDay());
                }
            }
        }

        if (branchId != null) {

            Double nowTotalSum = tradeRepository.totalSum(branchId, startTimestamp, endTimestamp);
            Double lastTotalSum = tradeRepository.totalSum(branchId, startTimestamp1, endTimestamp1);

            nowTotalSumDouble += nowTotalSum != null ? nowTotalSum : 0;
            lastTotalSumDouble += lastTotalSum != null ? lastTotalSum : 0;

            Double nowTotalProfit = tradeRepository.totalProfit(branchId, startTimestamp, endTimestamp);
            Double lastTotalProfit = tradeRepository.totalProfit(branchId, startTimestamp1, endTimestamp1);

            nowTotalProfitDouble += nowTotalProfit != null ? nowTotalProfit : 0;
            lastTotalProfitDouble += lastTotalProfit != null ? lastTotalProfit : 0;

            nowTotalCustomer = customerRepository.countAllByBranch_IdAndCreatedAtBetween(branchId, startTimestamp, endTimestamp);
            lastTotalCustomer = customerRepository.countAllByBranch_IdAndCreatedAtBetween(branchId, startTimestamp1, endTimestamp1);

            List<Warehouse> all = warehouseRepository.findAllByBranchId(branchId);
            for (Warehouse warehouse : all) {
                if (warehouse.getProductTypePrice() != null) {
                    Double nowTotalProductPriceSum = warehouseRepository.totalSumProductTypePrice(warehouse.getProductTypePrice().getId(), startTimestamp, endTimestamp);
                    Double lastTotalProductPriceSum = warehouseRepository.totalSumProductTypePrice(warehouse.getProductTypePrice().getId(), startTimestamp1, endTimestamp1);

                    nowTotalProductSum += nowTotalProductPriceSum != null ? nowTotalProductPriceSum : 0;
                    lastTotalProductSum += lastTotalProductPriceSum != null ? lastTotalProductPriceSum : 0;
                } else {
                    Double nowTotalProductPriceSum = warehouseRepository.totalSumProduct(warehouse.getProduct().getId(), startTimestamp, endTimestamp);
                    Double lastTotalProductPriceSum = warehouseRepository.totalSumProduct(warehouse.getProduct().getId(), startTimestamp1, endTimestamp1);

                    nowTotalProductSum += nowTotalProductPriceSum != null ? nowTotalProductPriceSum : 0;
                    lastTotalProductSum += lastTotalProductPriceSum != null ? lastTotalProductPriceSum : 0;
                }
            }
        } else {
            Double nowTotalSum = tradeRepository.totalSumByBusiness(businessId, startTimestamp, endTimestamp);
            Double lastTotalSum = tradeRepository.totalSumByBusiness(businessId, startTimestamp1, endTimestamp1);

            nowTotalSumDouble += nowTotalSum != null ? nowTotalSum : 0;
            lastTotalSumDouble += lastTotalSum != null ? lastTotalSum : 0;

            Double nowTotalProfit = tradeRepository.totalProfitByBusinessId(businessId, startTimestamp, endTimestamp);
            Double lastTotalProfit = tradeRepository.totalProfitByBusinessId(businessId, startTimestamp1, endTimestamp1);

            nowTotalProfitDouble += nowTotalProfit != null ? nowTotalProfit : 0;
            lastTotalProfitDouble += lastTotalProfit != null ? lastTotalProfit : 0;

            nowTotalCustomer = customerRepository.countAllByBusiness_IdAndCreatedAtBetween(businessId, startTimestamp, endTimestamp);
            lastTotalCustomer = customerRepository.countAllByBusiness_IdAndCreatedAtBetween(businessId, startTimestamp1, endTimestamp1);

            nowTotalCustomerInt += nowTotalCustomer != null ? nowTotalCustomer : 0;
            lastTotalCustomerInt += lastTotalCustomer != null ? lastTotalCustomer : 0;

            List<Warehouse> all = warehouseRepository.findAllByBranch_Business_Id(businessId);
            for (Warehouse warehouse : all) {
                if (warehouse.getProductTypePrice() != null) {
                    Double nowTotalProductPriceSum = warehouseRepository.totalSumProductTypePrice(warehouse.getProductTypePrice().getId(), startTimestamp, endTimestamp);
                    Double lastTotalProductPriceSum = warehouseRepository.totalSumProductTypePrice(warehouse.getProductTypePrice().getId(), startTimestamp1, endTimestamp1);

                    nowTotalProductSum += nowTotalProductPriceSum != null ? nowTotalProductPriceSum : 0;
                    lastTotalProductSum += lastTotalProductPriceSum != null ? lastTotalProductPriceSum : 0;
                } else {
                    Double nowTotalProductPriceSum = warehouseRepository.totalSumProduct(warehouse.getProduct().getId(), startTimestamp, endTimestamp);
                    Double lastTotalProductPriceSum = warehouseRepository.totalSumProduct(warehouse.getProduct().getId(), startTimestamp1, endTimestamp1);

                    nowTotalProductSum += nowTotalProductPriceSum != null ? nowTotalProductPriceSum : 0;
                    lastTotalProductSum += lastTotalProductPriceSum != null ? lastTotalProductPriceSum : 0;
                }
            }
        }

        GetStaticDto getStaticDto = new GetStaticDto();

        TotalCustomerDto totalCustomerDto = new TotalCustomerDto();
        if (nowTotalCustomerInt != 0 || lastTotalCustomerInt != 0) {
            totalCustomerDto.setAmount(nowTotalCustomerInt - lastTotalCustomerInt);
            totalCustomerDto.setPercentage(lastTotalCustomerInt == 0 ? 0 : (double) ((nowTotalCustomerInt - lastTotalCustomerInt) * 100) / lastTotalCustomerInt);
            totalCustomerDto.setSuccess(true);
        } else {
            totalCustomerDto.setMessage("O'tgan yoki hozirgi davr mobaynida yangi mijozlar mavjud emas");
            totalCustomerDto.setSuccess(false);
        }

        TotalSumDto totalSumDto = new TotalSumDto();
        if (nowTotalSumDouble != 0 || lastTotalSumDouble != 0) {
            totalSumDto.setSumma(nowTotalSumDouble - lastTotalSumDouble);
            totalSumDto.setPercentage(lastTotalSumDouble == 0 ? 0 : ((nowTotalSumDouble - lastTotalSumDouble) * 100) / lastTotalSumDouble);
            totalSumDto.setSuccess(true);
        } else {
            totalSumDto.setSuccess(false);
            totalSumDto.setMessage("O'tgan yoki hozirgi davr mobaynida yangi savdolar mavjud emas!");
        }

        TotalProfitSumDto totalProfitSumDto = new TotalProfitSumDto();
        if (nowTotalProfitDouble != 0 || lastTotalProfitDouble != 0) {
            totalProfitSumDto.setSumma(nowTotalProfitDouble - lastTotalProfitDouble);
            totalProfitSumDto.setPercentage(lastTotalProfitDouble == 0 ? 0 : ((nowTotalProfitDouble - lastTotalProfitDouble) * 100) / lastTotalProfitDouble);
            totalProfitSumDto.setSuccess(true);
        } else {
            totalProfitSumDto.setMessage("O'tgan yoki hozirgi davr mobaynida yangi savdolar mavjud emas!");
            totalProfitSumDto.setSuccess(false);
        }

        TotalProductSumDto totalProductSumDto = new TotalProductSumDto();
        if (nowTotalProductSum != 0 || lastTotalProductSum != 0) {
            totalProductSumDto.setSumma(nowTotalProductSum - lastTotalProductSum);
            totalProductSumDto.setPercentage(lastTotalProductSum == 0 ? 0 : ((nowTotalProductSum - lastTotalProductSum) * 100) / lastTotalProductSum);
            totalProductSumDto.setSuccess(true);
        } else {
            totalProductSumDto.setMessage("O'tgan yoki hozirgi davr mobaynida yangi mahsulotlar mavjud emas!");
            totalProductSumDto.setSuccess(false);
        }

        getStaticDto.setTotalCustomerDto(totalCustomerDto);
        getStaticDto.setTotalProductSumDto(totalProductSumDto);
        getStaticDto.setTotalSumDto(totalSumDto);
        getStaticDto.setTotalProfitSumDto(totalProfitSumDto);

        return new ApiResponse("found", true, getStaticDto);
    }

    public ApiResponse getChart(UUID branchId, UUID businessId) {

        double totalTradeSumma1 = 0;
        double totalDebtSum2 = 0;
        double totalPurchase2 = 0;
        double totalMyDebt2 = 0;

        List<GetChartDto> getChartDtoList = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        List<Branch> branchIds = new ArrayList<>();

        if (businessId != null) {
            List<Branch> allByBusinessId = branchRepository.findAllByBusiness_Id(businessId);
            branchIds.addAll(allByBusinessId);
        } else {
            Optional<Branch> optionalBranch = branchRepository.findById(branchId);
            if (optionalBranch.isEmpty()) {
                return new ApiResponse("not found", false);
            }
            Branch branch = optionalBranch.get();
            branchIds.add(branch);
        }

        for (int i = 0; i < 15; i++) {
            Double totalSum = null;
            Double totalDebtSum = null;
            Double totalPurchase = null;
            Double totalMyDebt = null;


            double totalTradeSumma = 0;
            double totalDebtSum1 = 0;
            double totalPurchase1 = 0;
            double totalMyDebt1 = 0;

            Timestamp from = Timestamp.valueOf(TODAY_END.minusDays(i + 1));
            Timestamp to = Timestamp.valueOf(TODAY_END.minusDays(i));
            for (Branch branch : branchIds) {
                totalSum = tradeRepository.totalSum(branch.getId(), from, to);
                totalDebtSum = tradeRepository.totalDebtSum(branch.getId(), from, to);
                totalPurchase = purchaseRepository.totalPurchase(branch.getId(), from, to);
                totalMyDebt = purchaseRepository.totalMyDebt(branch.getId(), from, to);
                totalTradeSumma = totalSum != null ? totalSum : 0;
                totalDebtSum1 = totalDebtSum != null ? totalDebtSum : 0;
                totalPurchase1 = totalPurchase != null ? totalPurchase : 0;
                totalMyDebt1 = totalMyDebt != null ? totalMyDebt : 0;


                GetChartDto getChartDto = new GetChartDto();
                getChartDto.setTimestamp(from);
                getChartDto.setTotalTrade(totalTradeSumma);
                getChartDto.setTotalDebt(totalDebtSum1);
                getChartDto.setTotalPurchase(totalPurchase1);
                getChartDto.setTotalMyDebt(totalMyDebt1);

                totalTradeSumma1 += totalTradeSumma;
                totalDebtSum2 += totalDebtSum1;
                totalPurchase2 += totalPurchase1;
                totalMyDebt2 += totalMyDebt1;

                getChartDtoList.add(getChartDto);
            }
        }
        response.put("data", getChartDtoList);
        response.put("totalPurchase", totalPurchase2);
        response.put("totalTrade", totalTradeSumma1);
        response.put("totalMyDebt", totalMyDebt2);
        response.put("totalDebt", totalDebtSum2);

        return new ApiResponse("kassadagi pul", true, response);
    }

    public ApiResponse getSellerForChart(UUID branchId, UUID businessId) {

        List<SellerForChartDto> dtoList = new ArrayList<>();

        for (int i = 0; i <= 7; i++) {
            Timestamp from = Timestamp.valueOf(TODAY_END.minusDays(i + 1));
            Timestamp to = Timestamp.valueOf(TODAY_END.minusDays(i));

            if (branchId != null) {
                SellerForChartDto sellerForChartDto = new SellerForChartDto();
                double tradeAmountByBranchId = tradeRepository.countAllByBranchIdAndCreatedAtBetween(branchId, from, to);
                sellerForChartDto.setCreateAt(from);
                sellerForChartDto.setTradeAmount(tradeAmountByBranchId);
                dtoList.add(sellerForChartDto);
            } else {
                SellerForChartDto sellerForChartDto = new SellerForChartDto();
                double tradeAmountByBusinessId = tradeRepository.countAllByBranch_BusinessIdAndCreatedAtBetween(businessId, from, to);
                sellerForChartDto.setCreateAt(from);
                sellerForChartDto.setTradeAmount(tradeAmountByBusinessId);
                dtoList.add(sellerForChartDto);
            }
        }

        if (dtoList.isEmpty()) {
            return new ApiResponse("list empty", false);
        }

        return new ApiResponse("all", true, dtoList);
    }


    public ApiResponse getBestSellingProduct(Boolean bySellingAmount, UUID branchId, Date startDate, Date endDate) {
        List<ReportForProduct> allProductByBranchId = null;
        List<ReportForProduct> allProductTypeManyByBranchId = null;
        if (startDate == null || endDate == null) {
            allProductByBranchId = tradeRepository.getAllProductByBranchId(branchId);
            allProductTypeManyByBranchId = tradeRepository.getAllProductTypeManyByBranchId(branchId);
        } else {
            Timestamp start = new Timestamp(startDate.getTime());
            Timestamp end = new Timestamp(endDate.getTime());
            allProductByBranchId = tradeRepository.getAllProductByBranchIdAndStartDateAndEndDate(branchId, start, end);
            allProductTypeManyByBranchId = tradeRepository.getAllProductTypeManyByBranchIdAndStartDateAndEndDate(branchId, start, end);
        }
        List<ProductReports> list1 = getDataDtos(allProductByBranchId);
        List<ProductReports> list2 = getDataDtos(allProductTypeManyByBranchId);
        List<ProductReports> combinedList = new LinkedList<>();
        combinedList.addAll(list1);
        combinedList.addAll(list2);
        List<ProductReports> separateList = new LinkedList<>(combinedList);
        if (bySellingAmount) {
            separateList.sort(Comparator.comparingDouble(ProductReports::getTsPrice).reversed());
        } else {
            separateList.sort(Comparator.comparingInt(ProductReports::getQuantity).reversed());
        }
        return new ApiResponse("all", true, separateList);
    }

    private List<ProductReports> getDataDtos(List<ReportForProduct> typeManyProducts) {
        Map<String, ProductReports> productMap = new HashMap<>();

        for (ReportForProduct data : typeManyProducts) {
            String productName = data.getProductName();
            String branchName = data.getBranchName();
            Timestamp time = data.getCreatedDate();
            Integer tradedQuantity = data.getQuantity();
            String categoryName = data.getCategoryName();
            Integer kpi = data.getKpi();
            Double tsPrice = data.getTsPrice();

            String key = productName + "-" + branchName;

            if (!productMap.containsKey(key)) {
                ProductReports newReports = new ProductReports();
                newReports.setProductName(productName);
                newReports.setBranchName(branchName);
                newReports.setQuantity(tradedQuantity);
                newReports.setCategoryName(categoryName);
                newReports.setCreateDate(time);
                newReports.setKpi(kpi);
                newReports.setTsPrice(tsPrice);

                productMap.put(key, newReports);
            } else {
                ProductReports existingData = productMap.get(key);
                existingData.setQuantity((existingData.getQuantity() == null ? 0 : existingData.getQuantity()) + (tradedQuantity == null ? 0 : tradedQuantity));
                existingData.setKpi((existingData.getKpi() == null ? 0 : existingData.getKpi()) + (kpi == null ? 0 : kpi));
                existingData.setTsPrice((existingData.getTsPrice() == null ? 0 : existingData.getTsPrice()) + (kpi == null ? 0 : kpi));
            }
        }

        return new ArrayList<>(productMap.values());
    }

    public ApiResponse getAllOutlayInfo(UUID branchId, Date startDate, Date endDate, User user, Boolean isDollar) {
        if (startDate == null || endDate == null) {
            List<OutlayInfoResult> results = new LinkedList<>();
            double total = 0;
            for (PaymentMethod paymentMethod : payMethodRepository.findAllByBusiness_Id(user.getBusiness().getId())) {
                double s = 0;
                for (Outlay outlay : checkIsDollar(branchId, paymentMethod, isDollar)) {
                    s = s + outlay.getTotalSum();
                }
                total = total + s;
                results.add(new OutlayInfoResult(paymentMethod.getType(), s));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("data", results);
            data.put("totalAmount", total);
            data.put("dollar", isDollar);
            return new ApiResponse("Umumiy xarajatlar", true, data);
        } else {
            List<OutlayInfoResult> results = new LinkedList<>();
            double total = 0;
            for (PaymentMethod paymentMethod : payMethodRepository.findAllByBusiness_Id(user.getBusiness().getId())) {
                double s = 0;
                for (Outlay outlay : checkIsDollarForSearch(branchId, paymentMethod, isDollar, startDate, endDate)) {
                    s = s + outlay.getTotalSum();
                }
                total = total + s;
                results.add(new OutlayInfoResult(paymentMethod.getType(), s));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("data", results);
            data.put("totalAmount", total);
            data.put("dollar", isDollar);
            return new ApiResponse("Umumiy xarajatlar", true, data);
        }
    }

    private List<Outlay> checkIsDollar(UUID branchId, PaymentMethod paymentMethod, Boolean isDollar) {
        if (isDollar) {
            return outlayRepository.findAllByBranch_IdAndPaymentMethod_IdAndDollarOutlayIsTrue(branchId, paymentMethod.getId());
        }
        return outlayRepository.findAllByBranch_IdAndPaymentMethod_IdAndDollarOutlayIsFalse(branchId, paymentMethod.getId());
    }

    private List<Outlay> checkIsDollarForSearch(UUID branchId, PaymentMethod paymentMethod, Boolean isDollar, Date startDate, Date endDate) {
        if (isDollar) {
            return outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndTrue(branchId, paymentMethod.getId(), startDate, endDate);
        }
        return outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetween(branchId, paymentMethod.getId(), startDate, endDate);
    }

    public ApiResponse getCashManyInfo(User user, UUID branchId, Date startDate, Date endDate) {
        PaymentMethod naqd = payMethodRepository.findByBusiness_IdAndTypeContainingIgnoreCaseOrderByCreatedAtDesc(user.getBusiness().getId(), "Naqd");
        if (naqd == null) {
            return new ApiResponse("Naqd pul to'lovi topilmadi!");
        }

        if (startDate == null && endDate == null) {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
            endDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        }
        Date finalStartDate = startDate;
        Date finalEndDate = endDate;
        if (branchId == null) {
            List<Branch> branches = branchRepository.findAllByBusiness_Id(user.getBusiness().getId());
            double sumOutlay = branches.stream()
                    .mapToDouble(branch -> {
                        List<Outlay> outlayIsFalse = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetween(branch.getId(), naqd.getId(), finalStartDate, finalEndDate);
                        return outlayIsFalse.stream().mapToDouble(Outlay::getTotalSum).sum();
                    })
                    .sum();
            double dollarOutlay = branches.stream()
                    .mapToDouble(branch -> {
                        List<Outlay> outlayIsFalse = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndTrue(branch.getId(), naqd.getId(), finalStartDate, finalEndDate);
                        return outlayIsFalse.stream().mapToDouble(Outlay::getTotalSum).sum();
                    })
                    .sum();
            double sumKitchen = branches.stream()
                    .mapToDouble(branch -> {
                        List<Outlay> outlays = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branch.getId(), naqd.getId(), finalStartDate, finalEndDate, false, OUTLAY_STATUS.KITCHEN.name());
                        return outlays.stream().mapToDouble(Outlay::getTotalSum).sum();
                    }).sum();
            double sumConstruction = branches.stream()
                    .mapToDouble(branch -> {
                        List<Outlay> outlays = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branch.getId(), naqd.getId(), finalStartDate, finalEndDate, false, OUTLAY_STATUS.CONSTRUCTION.name());
                        return outlays.stream().mapToDouble(Outlay::getTotalSum).sum();
                    }).sum();
            double dollarKitchen = branches.stream()
                    .mapToDouble(branch -> {
                        List<Outlay> outlays = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branch.getId(), naqd.getId(), finalStartDate, finalEndDate, true, OUTLAY_STATUS.KITCHEN.name());
                        return outlays.stream().mapToDouble(Outlay::getTotalSum).sum();
                    }).sum();
            double dollarConstruction = branches.stream()
                    .mapToDouble(branch -> {
                        List<Outlay> outlays = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branch.getId(), naqd.getId(), finalStartDate, finalEndDate, true, OUTLAY_STATUS.CONSTRUCTION.name());
                        return outlays.stream().mapToDouble(Outlay::getTotalSum).sum();
                    }).sum();
            double sumTransportIncome = branches.stream()
                    .mapToDouble(branch -> carInvoiceRepository.getTotalAmountByBranchIdAndTypeIgnoreCaseAndPaymentMethodIdAndCreatedAtBetween(branch.getId(), CarInvoiceType.INCOME.name(), naqd.getId(), finalStartDate, finalEndDate)).sum();
            double sumTransportEXPENSIVE = branches.stream()
                    .mapToDouble(branch -> carInvoiceRepository.getTotalAmountByBranchIdAndTypeIgnoreCaseAndPaymentMethodIdAndCreatedAtBetween(branch.getId(), CarInvoiceType.EXPENSIVE.name(), naqd.getId(), finalStartDate, finalEndDate)).sum();
            double sumTrade = branches.stream()
                    .mapToDouble(branch -> tradeRepository.findAllByBranch_IdAndPayMethodIdAndDollarTradeIsFalseAndDifferentPaymentIsFalseAndCreatedAtBetween(branch.getId(), naqd.getId(), finalStartDate, finalEndDate)
                            .stream()
                            .filter(trade -> trade.getTotalSum() <= trade.getPaidSum())
                            .mapToDouble(Trade::getPaidSum)
                            .sum())
                    .sum();

            double totalRepaymentSum = branches.stream()
                    .flatMap(branch -> repaymentDebtRepository.findAllByIsDollarAndBranchIdAndStartDateAndEndDate(false, branch.getId(), finalStartDate, finalEndDate, naqd.getId()).stream())
                    .mapToDouble(RepaymentDebt::getDebtSum)
                    .sum();

            double totalRepaymentDollar = branches.stream()
                    .flatMap(branch -> repaymentDebtRepository.findAllByIsDollarAndBranchIdAndStartDateAndEndDate(true, branch.getId(), finalStartDate, finalEndDate, naqd.getId()).stream())
                    .mapToDouble(RepaymentDebt::getDebtSumDollar)
                    .sum();

            double totalDollar = branches.stream()
                    .flatMapToDouble(branch ->
                            debtCanculsRepository.findAllByTradeBranch_IdAndCreatedAtBetween(branch.getId(), finalStartDate, finalEndDate)
                                    .stream()
                                    .mapToDouble(DebtCanculs::getDollarPrice))
                    .sum();

            double totalSum = branches.stream()
                    .flatMapToDouble(branch ->
                            debtCanculsRepository.findAllByTradeBranch_IdAndCreatedAtBetween(branch.getId(), finalStartDate, finalEndDate)
                                    .stream()
                                    .mapToDouble(DebtCanculs::getDebtPrice))
                    .sum();
            double sumMinus = sumOutlay + sumTransportEXPENSIVE;
            double sumPlus = sumTrade + sumTransportIncome;
            Map<String, Object> construction = new HashMap<>();
            construction.put("sum", sumConstruction);
            construction.put("dollar", dollarConstruction);
            Map<String, Object> kitchen = new HashMap<>();
            kitchen.put("sum", sumKitchen);
            kitchen.put("dollar", dollarKitchen);
            Map<String, Object> transport = new HashMap<>();
            transport.put("income", sumTransportIncome);
            transport.put("outlay", sumTransportEXPENSIVE);
            Map<String, Object> data = new HashMap<>();
            data.put("sum", ((sumPlus - sumMinus) - totalSum) + totalRepaymentSum);
            data.put("dollar", (totalDollar - dollarOutlay) + totalRepaymentDollar);
            data.put("transport", transport);
            data.put("kitchen", kitchen);
            data.put("construction", construction);
            return new ApiResponse("Kassadagi pul", true, data);
        } else {
            Branch branch = branchRepository.findById(branchId).orElse(null);
            if (branch != null) {
                double sumOutlay = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetween(branch.getId(), naqd.getId(), finalStartDate, finalEndDate)
                        .stream()
                        .mapToDouble(Outlay::getTotalSum)
                        .sum();
                double sumKitchen = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branchId, naqd.getId(), finalStartDate, finalEndDate, false, OUTLAY_STATUS.KITCHEN.name())
                        .stream().mapToDouble(Outlay::getTotalSum)
                        .sum();
                double sumConstruction = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branchId, naqd.getId(), finalStartDate, finalEndDate, false, OUTLAY_STATUS.CONSTRUCTION.name())
                        .stream().mapToDouble(Outlay::getTotalSum)
                        .sum();
                double dollarKitchen = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branchId, naqd.getId(), finalStartDate, finalEndDate, true, OUTLAY_STATUS.KITCHEN.name())
                        .stream().mapToDouble(Outlay::getTotalSum)
                        .sum();
                double dollarConstruction = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(branchId, naqd.getId(), finalStartDate, finalEndDate, true, OUTLAY_STATUS.CONSTRUCTION.name())
                        .stream().mapToDouble(Outlay::getTotalSum)
                        .sum();
                double sumTransportIncome = carInvoiceRepository.getTotalAmountByBranchIdAndTypeIgnoreCaseAndPaymentMethodIdAndCreatedAtBetween(branch.getId(), CarInvoiceType.INCOME.name(), naqd.getId(), finalStartDate, finalEndDate);
                double sumTransportEXPENSIVE = carInvoiceRepository.getTotalAmountByBranchIdAndTypeIgnoreCaseAndPaymentMethodIdAndCreatedAtBetween(branch.getId(), CarInvoiceType.EXPENSIVE.name(), naqd.getId(), finalStartDate, finalEndDate);
                double sumTrade = tradeRepository.findAllByBranch_IdAndPayMethodIdAndDollarTradeIsFalseAndDifferentPaymentIsFalseAndCreatedAtBetween(branch.getId(), naqd.getId(), finalStartDate, finalEndDate)
                        .stream()
                        .filter(trade -> trade.getTotalSum() <= trade.getPaidSum())
                        .mapToDouble(Trade::getPaidSum)
                        .sum();
                double dollarOutlay = outlayRepository.findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndTrue(branch.getId(), naqd.getId(), finalStartDate, finalEndDate)
                        .stream()
                        .mapToDouble(Outlay::getTotalSum)
                        .sum();
                double sumMinus = sumOutlay + sumTransportEXPENSIVE;
                double sumPlus = sumTrade + sumTransportIncome;
                List<DebtCanculs> allByTradeBranchId = debtCanculsRepository.findAllByTradeBranch_IdAndCreatedAtBetween(branchId, finalStartDate, finalEndDate);

                double totalDollar = allByTradeBranchId.stream()
                        .mapToDouble(DebtCanculs::getDollarPrice)
                        .sum();

                List<RepaymentDebt> allByIsDollarAndBranchIdAndStartDateAndEndDate = repaymentDebtRepository.findAllByIsDollarAndBranchIdAndStartDateAndEndDate(false, branchId, finalStartDate, finalEndDate, naqd.getId());

                double totalRepaymentSum = allByIsDollarAndBranchIdAndStartDateAndEndDate.stream()
                        .mapToDouble(RepaymentDebt::getDebtSum)
                        .sum();

                List<RepaymentDebt> allByIsDollarAndBranchIdAndStartDateAndEndDate1 = repaymentDebtRepository.findAllByIsDollarAndBranchIdAndStartDateAndEndDate(true, branchId, finalStartDate, finalEndDate, naqd.getId());

                double totalRepaymentDollar = allByIsDollarAndBranchIdAndStartDateAndEndDate1.stream()
                        .mapToDouble(RepaymentDebt::getDebtSumDollar)
                        .sum();

                double totalSum = allByTradeBranchId.stream()
                        .mapToDouble(DebtCanculs::getDebtPrice)
                        .sum();
                Map<String, Object> construction = new HashMap<>();
                construction.put("sum", sumConstruction);
                construction.put("dollar", dollarConstruction);
                Map<String, Object> kitchen = new HashMap<>();
                kitchen.put("sum", sumKitchen);
                kitchen.put("dollar", dollarKitchen);
                Map<String, Object> transport = new HashMap<>();
                transport.put("income", sumTransportIncome);
                transport.put("outlay", sumTransportEXPENSIVE);
                Map<String, Object> data = new HashMap<>();
                data.put("sum", ((sumPlus - sumMinus) - totalSum) + totalRepaymentSum);
                data.put("dollar", (totalDollar - dollarOutlay) + totalRepaymentDollar);
                data.put("transport", transport);
                data.put("kitchen", kitchen);
                data.put("construction", construction);
                return new ApiResponse("Kassadagi pul", true, data);
            } else {
                return new ApiResponse("Filial topilmadi!", false);
            }
        }
    }

    public ApiResponse getTotalTradeCount(User user, UUID branchId, Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
            endDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        }
        Date finalStartDate = startDate;
        Date finalEndDate = endDate;
        List<Trade> tradeList;
        if (branchId == null) {
            tradeList = branchRepository.findAllByBusiness_Id(user.getBusiness().getId())
                    .stream()
                    .flatMap(branch -> tradeRepository.findAllByBranch_IdAndCreatedAtBetweenOrderByCreatedAtDesc(branch.getId(), finalStartDate, finalEndDate).stream())
                    .collect(Collectors.toList());
        } else {
            tradeList = tradeRepository.findAllByBranch_IdAndCreatedAtBetweenOrderByCreatedAtDesc(branchId, finalStartDate, finalEndDate);
        }
        long tradeCount = tradeList.size();

        List<TradeInfoResult> results = new LinkedList<>();
        List<Measurement> measurements = measurementRepository.findAllByBusiness_Id(user.getBusiness().getId());

        for (Trade trade : tradeList) {
            for (Measurement measurement : measurements) {
                double totalQuantity = 0;

                List<TradeProduct> tradeProducts = tradeProductRepository.findAllByTradeIdAndCreatedAtBetweenAndProduct_MeasurementIdOrderByCreatedAtDesc(trade.getId(), finalStartDate, finalEndDate, measurement.getId());

                for (TradeProduct tradeProduct : tradeProducts) {
                    totalQuantity += tradeProduct.getTradedQuantity();
                }

                if (totalQuantity > 0) {
                    results.add(new TradeInfoResult(measurement.getName(), totalQuantity));
                }
            }
        }

        List<TradeInfoResult> uniqueResults = new LinkedList<>();

        for (TradeInfoResult result : results) {
            String currentMethodName = result.getMethodName();
            Double totalSum = result.getTotalSum();

            boolean exists = false;

            for (TradeInfoResult uniqueResult : uniqueResults) {
                if (uniqueResult.getMethodName().equals(currentMethodName)) {
                    uniqueResult.setTotalSum(uniqueResult.getTotalSum() + totalSum);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                uniqueResults.add(new TradeInfoResult(currentMethodName, totalSum));
            }
        }
        uniqueResults.sort((result1, result2) -> Double.compare(result2.getTotalSum(), result1.getTotalSum()));
        double sum = tradeList.stream()
                .flatMap(trade -> tradeProductRepository.findAllByTradeId(trade.getId()).stream())
                .mapToDouble(tradeProduct -> {
                    double tradedQuantity = tradeProduct.getTradedQuantity();
                    double totalSalePrice = tradeProduct.getTotalSalePrice();
                    double v = 0;

                    if (tradeProduct.getProduct() == null) {
                        ProductTypePrice productTypePrice = tradeProduct.getProductTypePrice();
                        v =  totalSalePrice-(productTypePrice.getBuyPrice() * tradedQuantity);
                    } else {
                        Product product = tradeProduct.getProduct();
                        v =  totalSalePrice-(product.getBuyPrice() * tradedQuantity) ;
                    }

                    return v;
                })
                .sum();


        Map<String, Object> data = new HashMap<>();
        data.put("trade_count", tradeCount);
        data.put("trade_product_count", uniqueResults);
        data.put("benefit",sum);
        return new ApiResponse("Xaridlar soni hisoboti", true, data);
    }
}
