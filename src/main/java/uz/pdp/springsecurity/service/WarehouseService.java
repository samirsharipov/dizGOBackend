package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.Type;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.ConstantProduct;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final ExchangeProductRepository exchangeProductRepository;
    private final ProductTypeComboRepository productTypeComboRepository;
    private final ExchangeProductBranchRepository exchangeProductBranchRepository;
    private final FifoCalculationService fifoCalculationService;
    private final NotificationService notificationService;
    private final ProductAboutRepository productAboutRepository;

    public double createOrEditWareHouse(PurchaseProduct purchaseProduct, double quantity) {
        Branch branch = purchaseProduct.getPurchase().getBranch();
        Product product = purchaseProduct.getProduct();
        ProductTypePrice productTypePrice = purchaseProduct.getProductTypePrice();
        // save product history
        productAboutRepository.save(new ProductAbout(
                product,
                productTypePrice,
                branch,
                ConstantProduct.PURCHASE,
                quantity,
                UUID.randomUUID()
        ));
        return createOrEditWareHouseHelper(branch, product, productTypePrice, quantity);
    }

    public double createOrEditWareHouse(Production production) {
        Branch branch = production.getBranch();
        Product product = production.getProduct();
        ProductTypePrice productTypePrice = production.getProductTypePrice();
        // save product history
        productAboutRepository.save(new ProductAbout(
                product,
                productTypePrice,
                branch,
                ConstantProduct.PRODUCTION,
                production.getQuantity(),
                UUID.randomUUID()
        ));
        return createOrEditWareHouseHelper(branch, product, productTypePrice, production.getQuantity());
    }

    public double createOrEditWareHouseHelper(Branch branch, Product product, ProductTypePrice productTypePrice, Double quantity) {
        Warehouse warehouse;
        double amount = 0;
        if (product != null) {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branch.getId(), product.getId());
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
                if (warehouse.getAmount() < 0) {
                    amount = - warehouse.getAmount();
                }
                warehouse.setAmount(warehouse.getAmount() + quantity);
            } else {
                warehouse = new Warehouse();
                warehouse.setBranch(branch);
                warehouse.setProduct(product);
                warehouse.setAmount(quantity);
            }
        } else {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(branch.getId(), productTypePrice.getId());
            if (optionalWarehouse.isPresent()) {
                warehouse = optionalWarehouse.get();
                if (warehouse.getAmount() < 0) {
                    amount = -warehouse.getAmount();
                }
                warehouse.setAmount(warehouse.getAmount() + quantity);
            } else {
                warehouse = new Warehouse();
                warehouse.setBranch(branch);
                warehouse.setProductTypePrice(productTypePrice);
                warehouse.setAmount(quantity);
            }
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
            } else if (productTypePriceRepository.existsById(entry.getKey())) {
                Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(branch.getId(), entry.getKey());
                if (optionalWarehouse.isPresent()) warehouse = optionalWarehouse.get();
            } else return false;
            if (warehouse == null) return false;
            if (warehouse.getAmount() < entry.getValue()) return false;
        }
        return true;
    }

    public TradeProduct createOrEditTrade(Branch branch, TradeProduct tradeProduct, TradeProductDto tradeProductDto, UUID tradeId) {
        double amount = tradeProduct.getTradedQuantity() - tradeProductDto.getTradedQuantity();
        if (tradeProductDto.getType().equalsIgnoreCase("single")) {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branch.getId(), tradeProductDto.getProductId());
            Warehouse warehouse;
            if (optionalWarehouse.isEmpty()) {
                Optional<Product> optionalProduct = productRepository.findById(tradeProductDto.getProductId());
                if (optionalProduct.isEmpty()) return null;
                warehouse = new Warehouse(
                        optionalProduct.get(),
                        null,
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

            // TODO: 7/1/2023 create
//            productHistoryService.create(branch, warehouse.getProduct(), warehouse.getProductTypePrice(), false, -amount, warehouse.getAmount(), 0);
        } else if (tradeProductDto.getType().equalsIgnoreCase("many")) {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(branch.getId(), tradeProductDto.getProductTypePriceId());
            Warehouse warehouse;
            if (optionalWarehouse.isEmpty()) {
                Optional<ProductTypePrice> optionalProduct = productTypePriceRepository.findById(tradeProductDto.getProductTypePriceId());
                if (optionalProduct.isEmpty()) return null;
                warehouse = new Warehouse(
                        null,
                        optionalProduct.get(),
                        branch,
                        0,
                        new Date()
                );
            } else {
                warehouse = optionalWarehouse.get();
            }
            warehouse.setAmount((Math.round((warehouse.getAmount() + amount) * 100) / 100.));
            warehouse.setLastSoldDate(new Date());
            Warehouse save = warehouseRepository.save(warehouse);
            if (warehouse.getAmount() <= warehouse.getProductTypePrice().getProduct().getMinQuantity()) {
                notificationService.lessProduct(warehouse.getProductTypePrice().getId(), false, save.getAmount());
            }
            tradeProduct.setProductTypePrice(warehouse.getProductTypePrice());

            // TODO: 7/1/2023 create
//            productHistoryService.create(branch, warehouse.getProduct(), warehouse.getProductTypePrice(), false, -amount, warehouse.getAmount(), 0);
        } else {
            Optional<Product> optionalProduct = productRepository.findById(tradeProductDto.getProductId());
            if (optionalProduct.isEmpty()) return null;
            List<ProductTypeCombo> comboList = productTypeComboRepository.findAllByMainProductId(tradeProductDto.getProductId());
            for (ProductTypeCombo combo : comboList) {
                if (combo.getContentProduct() != null) {
                    Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branch.getId(), combo.getContentProduct().getId());
                    Warehouse warehouse;
                    if (optionalWarehouse.isEmpty()) {
                        Optional<Product> optionalProduct1 = productRepository.findById(tradeProductDto.getProductId());
                        if (optionalProduct1.isEmpty()) continue;
                        warehouse = new Warehouse(
                                optionalProduct1.get(),
                                null,
                                branch,
                                0,
                                new Date()
                        );
                    } else {
                        warehouse = optionalWarehouse.get();
                    }
                    warehouse.setAmount(Math.round((warehouse.getAmount() + amount * combo.getAmount()) * 100) / 100.);
                    warehouse.setLastSoldDate(new Date());
                    warehouseRepository.save(warehouse);

                    // TODO: 7/1/2023 create
//                    productHistoryService.create(branch, tradeProduct.getProduct(), tradeProduct.getProductTypePrice(), false, -amount, warehouse.getAmount(), 0);
                }else {
                    Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(branch.getId(), tradeProductDto.getProductTypePriceId());
                    Warehouse warehouse;
                    if (optionalWarehouse.isEmpty()) {
                        Optional<ProductTypePrice> optionalProduct1 = productTypePriceRepository.findById(tradeProductDto.getProductTypePriceId());
                        if (optionalProduct1.isEmpty()) continue;
                        warehouse = new Warehouse(
                                null,
                                optionalProduct1.get(),
                                branch,
                                0,
                                new Date()
                        );
                    } else {
                        warehouse = optionalWarehouse.get();
                    }
                    warehouse.setAmount(Math.round((warehouse.getAmount() + amount * combo.getAmount()) * 100) / 100.);
                    warehouse.setLastSoldDate(new Date());
                    Warehouse save = warehouseRepository.save(warehouse);
                    if (warehouse.getAmount() <= warehouse.getProductTypePrice().getProduct().getMinQuantity()) {
                        notificationService.lessProduct(warehouse.getProductTypePrice().getId(), false, save.getAmount());
                    }

                    // TODO: 7/1/2023 create
//                    productHistoryService.create(branch, tradeProduct.getProduct(), tradeProduct.getProductTypePrice(), false, -amount, warehouse.getAmount(), 0);
                }
            }
            tradeProduct.setProduct(optionalProduct.get());
        }
        tradeProduct.setTotalSalePrice(tradeProductDto.getTotalSalePrice());
        tradeProduct.setTradedQuantity(tradeProductDto.getTradedQuantity());
        tradeProduct.setSubMeasurement(tradeProductDto.isSubMeasurement());
        System.err.println("SALOOOOOOOOOOM");
        // save product history
        productAboutRepository.save(new ProductAbout(
                tradeProduct.getProduct(),
                tradeProduct.getProductTypePrice(),
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
        if (contentProductDto.getProductId() != null) {
            optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(contentProduct.getProduction().getBranch().getId(), contentProductDto.getProductId());
            if (optionalWarehouse.isEmpty()) return null;
            warehouse = optionalWarehouse.get();
            warehouse.setAmount(warehouse.getAmount() - contentProductDto.getQuantity());
            Warehouse save = warehouseRepository.save(warehouse);
            if (warehouse.getAmount() <= warehouse.getProduct().getMinQuantity()) {
                notificationService.lessProduct(warehouse.getProduct().getId(),true,save.getAmount());
            }
            contentProduct.setProduct(warehouse.getProduct());
        } else {
            optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(contentProduct.getProduction().getBranch().getId(), contentProductDto.getProductTypePriceId());
            if (optionalWarehouse.isEmpty()) return null;
            warehouse = optionalWarehouse.get();
            warehouse.setAmount(warehouse.getAmount() - contentProductDto.getQuantity());
            Warehouse save = warehouseRepository.save(warehouse);
            if (warehouse.getAmount() <= warehouse.getProductTypePrice().getProduct().getMinQuantity()) {
                notificationService.lessProduct(warehouse.getProductTypePrice().getId(), false, save.getAmount());
            }
            contentProduct.setProductTypePrice(warehouse.getProductTypePrice());
        }

        // save product history
        productAboutRepository.save(new ProductAbout(
                contentProduct.getProduct(),
                contentProduct.getProductTypePrice(),
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
        if (contentProductDto.getProductId() != null) {
            Optional<Product> optional = productRepository.findById(contentProductDto.getProductId());
            if (optional.isEmpty()) return 0;
            contentProduct.setProduct(optional.get());
        } else {
            Optional<ProductTypePrice> optional = productTypePriceRepository.findById(contentProductDto.getProductTypePriceId());
            if (optional.isEmpty()) return 0;
            contentProduct.setProductTypePrice(optional.get());
        }

        // save product history
        productAboutRepository.save(new ProductAbout(
                contentProduct.getProduct(),
                contentProduct.getProductTypePrice(),
                contentProduct.getProduction().getBranch(),
                ConstantProduct.BRAK,
                contentProduct.getQuantity(),
                UUID.randomUUID()
        ));
        return createOrEditWareHouseHelper(contentProduct.getProduction().getBranch(), contentProduct.getProduct(), contentProduct.getProductTypePrice(), contentProductDto.getQuantity());
    }

    public ApiResponse createOrUpdateExchangeProductBranch(ExchangeProductBranchDTO branchDTO, ExchangeProductBranch exchangeProductBranch, boolean update) {

        List<ExchangeProduct> exchangeProductList = new ArrayList<>();
        for (ExchangeProductDTO exchangeProductDTO : branchDTO.getExchangeProductDTOS()) {
            ExchangeProduct exchangeProduct = new ExchangeProduct();
            exchangeProduct.setExchangeProductQuantity(exchangeProductDTO.getExchangeProductQuantity());
            if (exchangeProductDTO.getProductExchangeId() != null) {
                Optional<Product> optionalProduct = productRepository.findById(exchangeProductDTO.getProductExchangeId());
                optionalProduct.ifPresent(exchangeProduct::setProduct);
            } else {
                Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository
                        .findById(exchangeProductDTO.getProductTypePriceId());
                optionalProductTypePrice.ifPresent(exchangeProduct::setProductTypePrice);
            }
            exchangeProductList.add(exchangeProduct);
            exchangeProductRepository.save(exchangeProduct);
        }

        Branch shippedBranch = exchangeProductBranch.getShippedBranch();
        Branch receivedBranch = exchangeProductBranch.getReceivedBranch();

        for (ExchangeProduct exchangeProduct : exchangeProductList) {
            if (exchangeProduct.getProduct() != null) {
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
                        return new ApiResponse("Omborda mahsulot yetarli emas!",false);
                    }
                }else {
                    return new ApiResponse( exchangeProduct.getProduct().getName() + " ushbu mahsulotdan obmorda mavjud emas!",false);
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
            } else {
                Optional<Warehouse> optionalShippedBranchWarehouse = warehouseRepository
                        .findByBranchIdAndProductTypePriceId(shippedBranch.getId(), exchangeProduct.getProductTypePrice().getId());
                Optional<Warehouse> optionalReceivedBranchWarehouse = warehouseRepository
                        .findByBranchIdAndProductTypePriceId(receivedBranch.getId(), exchangeProduct.getProductTypePrice().getId());
                if (optionalShippedBranchWarehouse.isPresent()) {
                    Warehouse warehouse = optionalShippedBranchWarehouse.get();
                    if (warehouse.getAmount() >= exchangeProduct.getExchangeProductQuantity()) {
                        warehouse.setAmount(warehouse.getAmount() - exchangeProduct.getExchangeProductQuantity());
                        Warehouse save = warehouseRepository.save(warehouse);
                        if (warehouse.getAmount() <= warehouse.getProductTypePrice().getProduct().getMinQuantity()) {
                            notificationService.lessProduct(warehouse.getProductTypePrice().getId(), false, save.getAmount());
                        }
                    } else {
                        return new ApiResponse("Omborda mahsulot yetarli emas!");
                    }
                }
                if (optionalReceivedBranchWarehouse.isPresent()) {
                    Warehouse warehouse = optionalReceivedBranchWarehouse.get();
                    warehouse.setAmount(warehouse.getAmount() + exchangeProduct.getExchangeProductQuantity());
                    warehouseRepository.save(warehouse);
                } else {
                    List<Branch> branchList = exchangeProduct.getProductTypePrice().getProduct().getBranch();
                    boolean b = false;

                    for (Branch branch : branchList) {
                        if (branch.getId().equals(receivedBranch.getId())) {
                            b = true;
                            break;
                        }
                    }

                    Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository.findById(exchangeProduct.getProductTypePrice().getId());
                    if (!b) {
                        branchList.add(receivedBranch);
                        Product product = exchangeProduct.getProductTypePrice().getProduct();
                        product.setBranch(branchList);
                        productRepository.save(product);
                    }

                    Warehouse warehouse = new Warehouse();
                    warehouse.setBranch(receivedBranch);
                    warehouse.setAmount(exchangeProduct.getExchangeProductQuantity());
                    optionalProductTypePrice.ifPresent(warehouse::setProductTypePrice);
                    warehouseRepository.save(warehouse);
                }
            }

            // save product history
            productAboutRepository.save(new ProductAbout(
                    exchangeProduct.getProduct(),
                    exchangeProduct.getProductTypePrice(),
                    shippedBranch,
                    ConstantProduct.TO_BRANCH,
                    -exchangeProduct.getExchangeProductQuantity(),
                    UUID.randomUUID()
            ));

            // save product history
            productAboutRepository.save(new ProductAbout(
                    exchangeProduct.getProduct(),
                    exchangeProduct.getProductTypePrice(),
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
            if (warehouse.getProduct() != null) {
                if (warehouse.getProduct().getMinQuantity() >= warehouse.getAmount()) {
                    warehouses.add(warehouse);
                }
            } else {
                if (warehouse.getProductTypePrice().getProduct().getMinQuantity() >= warehouse.getAmount()) {
                    warehouses.add(warehouse);
                }
            }
        }

        for (Warehouse warehouse : warehouses) {
            GetLessProductDto getLessProductDto = new GetLessProductDto();
            if (warehouse.getProductTypePrice() != null) {
                getLessProductDto.setName(warehouse.getProductTypePrice().getName());
                if (warehouse.getProductTypePrice().getProduct().getPhoto() != null) {
                    getLessProductDto.setAttachmentId(warehouse.getProductTypePrice().getProduct().getPhoto().getId());
                }
            } else {
                getLessProductDto.setName(warehouse.getProduct().getName());
                if (warehouse.getProduct().getPhoto() != null) {
                    getLessProductDto.setAttachmentId(warehouse.getProduct().getPhoto().getId());
                }
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
            if (warehouse.getProduct() != null)
                dto.setName(warehouse.getProduct().getName());
            else
                dto.setName(warehouse.getProductTypePrice().getName());
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
            if (warehouse.getProduct() != null) {
                if (warehouse.getProduct().getType().equals(Type.SINGLE)) {
                    productSalePrice += warehouse.getAmount() * warehouse.getProduct().getSalePrice();
                }
            } else {
                productSalePrice += warehouse.getAmount() * warehouse.getProductTypePrice().getSalePrice();
            }
        }
        return productSalePrice;
    }
}
