package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.ConstantProduct;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final ExchangeProductRepository exchangeProductRepository;
    private final ExchangeProductBranchRepository exchangeProductBranchRepository;
    private final FifoCalculationService fifoCalculationService;
    private final NotificationService notificationService;
    private final ProductAboutRepository productAboutRepository;

    public double createOrEditWareHouse(PurchaseProduct purchaseProduct, double quantity) {
        Branch branch = purchaseProduct.getPurchase().getBranch();
        Product product = purchaseProduct.getProduct();
        productAboutRepository.save(new ProductAbout(
                product,
                branch,
                ConstantProduct.PURCHASE,
                quantity,
                UUID.randomUUID()
        ));
        return createOrEditWareHouseHelper(branch, product, quantity);
    }

    public double createOrEditWareHouse(Production production) {
        Branch branch = production.getBranch();
        Product product = production.getProduct();
        // save product history
        productAboutRepository.save(new ProductAbout(
                product,
                branch,
                ConstantProduct.PRODUCTION,
                production.getQuantity(),
                UUID.randomUUID()
        ));
        return createOrEditWareHouseHelper(branch, product, production.getQuantity());
    }

    public double createOrEditWareHouseHelper(Branch branch, Product product, Double quantity) {
        Warehouse warehouse;
        double amount = 0;
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branch.getId(), product.getId());
        if (optionalWarehouse.isPresent()) {
            warehouse = optionalWarehouse.get();
            if (warehouse.getAmount() < 0) {
                amount = -warehouse.getAmount();
            }
            warehouse.setAmount(warehouse.getAmount() + quantity);
        } else {
            warehouse = new Warehouse();
            warehouse.setBranch(branch);
            warehouse.setProduct(product);
            warehouse.setAmount(quantity);
        }

        warehouseRepository.save(warehouse);
        // TODO: 7/1/2023 create
        // DAILY PRODUCT HISTORY
//        if (quantity > 0){
//            productHistoryService.create(branch, product, productTypePrice, true, quantity, warehouse.getAmount(), 0);
//        } else {
//            productHistoryService.create(branch, product, productTypePrice, false, -quantity, warehouse.getAmount(), 0);
//        }

        return amount;
    }

    public Boolean checkBeforeTrade(Branch branch, HashMap<UUID, Double> map) {
        for (Map.Entry<UUID, Double> entry : map.entrySet()) {
            Warehouse warehouse = null;
            if (productRepository.existsById(entry.getKey())) {
                Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branch.getId(), entry.getKey());
                if (optionalWarehouse.isPresent()) warehouse = optionalWarehouse.get();
            } else
                return false;
            if (warehouse == null)
                return false;
            if (warehouse.getAmount() < entry.getValue())
                return false;
        }
        return true;
    }

    public TradeProduct createOrEditTrade(Branch branch, TradeProduct tradeProduct, TradeProductDto tradeProductDto, UUID tradeId) {
        double amount = tradeProduct.getTradedQuantity() - tradeProductDto.getTradedQuantity();
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branch.getId(), tradeProductDto.getProductId());
        Warehouse warehouse;
        if (optionalWarehouse.isEmpty()) {
            Optional<Product> optionalProduct = productRepository.findById(tradeProductDto.getProductId());
            if (optionalProduct.isEmpty()) return null;
            warehouse = new Warehouse(
                    optionalProduct.get(),
                    branch,
                    0,
                    new Date()
            );
        } else {
            warehouse = optionalWarehouse.get();
        }
        warehouse.setAmount(Math.round((warehouse.getAmount() + amount) * 100) / 100.);
        warehouse.setLastSoldDate(new Date());
        Warehouse save = warehouseRepository.save(warehouse);
        if (warehouse.getAmount() <= warehouse.getProduct().getMinQuantity()) {
            notificationService.lessProduct(warehouse.getProduct().getId(), true, save.getAmount());
        }
        tradeProduct.setProduct(warehouse.getProduct());

        tradeProduct.setTotalSalePrice(tradeProductDto.getTotalSalePrice());
        tradeProduct.setTradedQuantity(tradeProductDto.getTradedQuantity());
        tradeProduct.setSubMeasurement(tradeProductDto.isSubMeasurement());
        System.err.println("SALOOOOOOOOOOM");
        // save product history
        productAboutRepository.save(new ProductAbout(
                tradeProduct.getProduct(),
                branch,
                ConstantProduct.TRADE,
                -tradeProduct.getTradedQuantity(),
                tradeId
        ));
        System.err.println(tradeProductDto.getTradeProductId());
        return tradeProduct;
    }

    public ContentProduct createContentProduct(ContentProduct contentProduct, ContentProductDto contentProductDto) {
        Optional<Warehouse> optionalWarehouse;
        Warehouse warehouse;
        optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(contentProduct.getProduction().getBranch().getId(), contentProductDto.getProductId());
        if (optionalWarehouse.isEmpty()) return null;
        warehouse = optionalWarehouse.get();
        warehouse.setAmount(warehouse.getAmount() - contentProductDto.getQuantity());
        Warehouse save = warehouseRepository.save(warehouse);
        if (warehouse.getAmount() <= warehouse.getProduct().getMinQuantity()) {
            notificationService.lessProduct(warehouse.getProduct().getId(), true, save.getAmount());
        }
        contentProduct.setProduct(warehouse.getProduct());

        // save product history
        productAboutRepository.save(new ProductAbout(
                contentProduct.getProduct(),
                contentProduct.getProduction().getBranch(),
                ConstantProduct.CONTENT,
                -contentProduct.getQuantity(),
                UUID.randomUUID()
        ));

        // TODO: 7/1/2023 create
//        productHistoryService.create(warehouse.getBranch(), contentProduct.getProduct(), contentProduct.getProductTypePrice(), false, contentProductDto.getQuantity(), warehouse.getAmount(), 0);
        return contentProduct;
    }

    public double createByProduct(ContentProduct contentProduct, ContentProductDto contentProductDto) {
        Optional<Product> optional = productRepository.findById(contentProductDto.getProductId());
        if (optional.isEmpty()) return 0;
        contentProduct.setProduct(optional.get());


        // save product history
        productAboutRepository.save(new ProductAbout(
                contentProduct.getProduct(),
                contentProduct.getProduction().getBranch(),
                ConstantProduct.BRAK,
                contentProduct.getQuantity(),
                UUID.randomUUID()
        ));
        return createOrEditWareHouseHelper(contentProduct.getProduction().getBranch(), contentProduct.getProduct(), contentProductDto.getQuantity());
    }

    public ApiResponse createOrUpdateExchangeProductBranch(ExchangeProductBranchDTO branchDTO, ExchangeProductBranch exchangeProductBranch, boolean update) {

        List<ExchangeProduct> exchangeProductList = new ArrayList<>();
        for (ExchangeProductDTO exchangeProductDTO : branchDTO.getExchangeProductDTOS()) {
            ExchangeProduct exchangeProduct = new ExchangeProduct();
            exchangeProduct.setExchangeProductQuantity(exchangeProductDTO.getExchangeProductQuantity());
            Optional<Product> optionalProduct = productRepository.findById(exchangeProductDTO.getProductExchangeId());
            optionalProduct.ifPresent(exchangeProduct::setProduct);

            exchangeProductList.add(exchangeProduct);
            exchangeProductRepository.save(exchangeProduct);
        }

        Branch shippedBranch = exchangeProductBranch.getShippedBranch();
        Branch receivedBranch = exchangeProductBranch.getReceivedBranch();

        for (ExchangeProduct exchangeProduct : exchangeProductList) {
            Optional<Warehouse> optionalShippedBranchWarehouse = warehouseRepository
                    .findByBranchIdAndProductId(shippedBranch.getId(), exchangeProduct.getProduct().getId());
            Optional<Warehouse> optionalReceivedBranchWarehouse = warehouseRepository
                    .findByBranchIdAndProductId(receivedBranch.getId(), exchangeProduct.getProduct().getId());
            if (optionalShippedBranchWarehouse.isPresent()) {
                Warehouse warehouse = optionalShippedBranchWarehouse.get();
                if (warehouse.getAmount() >= exchangeProduct.getExchangeProductQuantity()) {
                    warehouse.setAmount(warehouse.getAmount() - exchangeProduct.getExchangeProductQuantity());
                    Warehouse save = warehouseRepository.save(warehouse);
                    if (warehouse.getAmount() <= warehouse.getProduct().getMinQuantity()) {
                        notificationService.lessProduct(warehouse.getProduct().getId(), true, save.getAmount());
                    }
                } else {
                    return new ApiResponse("Omborda mahsulot yetarli emas!", false);
                }
            } else {
                return new ApiResponse(exchangeProduct.getProduct().getName() + " ushbu mahsulotdan obmorda mavjud emas!", false);
            }
            if (optionalReceivedBranchWarehouse.isPresent()) {
                Warehouse warehouse = optionalReceivedBranchWarehouse.get();
                warehouse.setAmount(warehouse.getAmount() + exchangeProduct.getExchangeProductQuantity());
                warehouseRepository.save(warehouse);
            } else {
                List<Branch> branchList = exchangeProduct.getProduct().getBranch();
                Warehouse warehouse = new Warehouse();
                boolean b = false;
                for (Branch branch : branchList) {
                    if (branch.getId().equals(receivedBranch.getId())) {
                        b = true;
                        break;
                    }
                }
                Optional<Product> optionalProduct = productRepository.findById(exchangeProduct.getProduct().getId());
                if (!b) {
                    branchList.add(receivedBranch);
                    Product product = optionalProduct.get();
                    product.setBranch(branchList);
                    productRepository.save(product);
                }
                warehouse.setBranch(receivedBranch);
                warehouse.setAmount(exchangeProduct.getExchangeProductQuantity());
                optionalProduct.ifPresent(warehouse::setProduct);
                warehouseRepository.save(warehouse);
            }

            // save product history
            productAboutRepository.save(new ProductAbout(
                    exchangeProduct.getProduct(),
                    shippedBranch,
                    ConstantProduct.TO_BRANCH,
                    -exchangeProduct.getExchangeProductQuantity(),
                    UUID.randomUUID()
            ));

            // save product history
            productAboutRepository.save(new ProductAbout(
                    exchangeProduct.getProduct(),
                    receivedBranch,
                    ConstantProduct.FROM_BRANCH,
                    exchangeProduct.getExchangeProductQuantity(),
                    UUID.randomUUID()
            ));

        }
        List<ExchangeProduct> exchangeProducts = exchangeProductRepository.saveAll(exchangeProductList);
        exchangeProductBranch.setExchangeProductList(exchangeProducts);
        exchangeProductBranchRepository.save(exchangeProductBranch);
        fifoCalculationService.createExchange(exchangeProductBranch);
        return new ApiResponse("successfully saved", true);
    }

    public ApiResponse getLessProduct(UUID businessId, UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Warehouse> allWarehouse;

        List<GetLessProductDto> getLessProductDtoList = new ArrayList<>();
        List<Warehouse> warehouses = new ArrayList<>();

        if (branchId != null) {
            allWarehouse = warehouseRepository
                    .findAllByBranchIdAndAmountNotOrderByAmountAsc(branchId, 0, pageable);
        } else {
            allWarehouse = warehouseRepository
                    .findAllByBranch_BusinessIdAndAmountNotOrderByAmountAsc(businessId, 0, pageable);
        }

        List<Warehouse> warehouseList = allWarehouse.toList();

        for (Warehouse warehouse : warehouseList) {
            if (warehouse.getProduct().getMinQuantity() >= warehouse.getAmount()) {
                warehouses.add(warehouse);
            }
        }

        for (Warehouse warehouse : warehouses) {
            GetLessProductDto getLessProductDto = new GetLessProductDto();

            getLessProductDto.setName(warehouse.getProduct().getName());
            if (warehouse.getProduct().getPhoto() != null) {
                getLessProductDto.setAttachmentId(warehouse.getProduct().getPhoto().getId());
            }

            getLessProductDto.setAmount(warehouse.getAmount());
            getLessProductDtoList.add(getLessProductDto);
        }

        Page<Warehouse> newPage = new PageImpl<>(warehouses);

        Map<String, Object> response = new HashMap<>();
        response.put("getLessProduct", getLessProductDtoList);
        response.put("currentPage", newPage.getNumber());
        response.put("totalItems", newPage.getTotalElements());
        response.put("totalPages", newPage.getTotalPages());
        return new ApiResponse("all", true, response);
    }

    public ApiResponse getLessSoldProduct(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Warehouse> warehousePage = warehouseRepository.findAllByBranchIdAndAmountIsNotOrderByLastSoldDate(branchId, 0, pageable);
        List<GetLessProductDto> getLessProductDtoList = new ArrayList<>();
        for (Warehouse warehouse : warehousePage.getContent()) {
            GetLessProductDto dto = new GetLessProductDto();
            dto.setName(warehouse.getProduct().getName());
            dto.setAmount(warehouse.getAmount());
            dto.setLastSoldDate(warehouse.getLastSoldDate());
            dto.setMoney(warehouse.getAmount() * warehouse.getProduct().getBuyPrice());
            getLessProductDtoList.add(dto);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("getLessProduct", getLessProductDtoList);
        response.put("currentPage", warehousePage.getNumber());
        response.put("totalItem", warehousePage.getTotalElements());
        response.put("totalPage", warehousePage.getTotalPages());
        return new ApiResponse("all", true, response);
    }

    public double getProductSalePriceByBranch(UUID branchId) {
        double productSalePrice = 0;
        for (Warehouse warehouse : warehouseRepository.findAllByBranchId(branchId)) {
            productSalePrice += warehouse.getAmount() * warehouse.getProduct().getSalePrice();
        }
        return productSalePrice;
    }
}
