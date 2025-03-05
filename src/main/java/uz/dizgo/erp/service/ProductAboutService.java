package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Product;
import uz.dizgo.erp.entity.ProductAbout;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ProductAboutDto;
import uz.dizgo.erp.payload.ProductPageDto;
import uz.dizgo.erp.payload.ProductPageDtoMany;
import uz.dizgo.erp.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductAboutService {
    private final ProductAboutRepository productAboutRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final TradeProductRepository tradeProductRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final ProductionRepository productionRepository;
    private final FifoCalculationRepository fifoCalculationRepository;
    private final ContentProductRepository contentProductRepository;

    public ApiResponse getOne(UUID productId, UUID branchId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            return new ApiResponse("PRODUCT NOT FOUND", false);
        Product product = optionalProduct.get();
        ProductPageDto dto = new ProductPageDto();
        setProductPageDto(branchId, dto, product);
        List<ProductPageDtoMany> productPageDtoManyList = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        response.put("productPageDto", dto);
        response.put("typeList", productPageDtoManyList);
        return new ApiResponse(true, response);
    }

    private void setProductPageDto(UUID branchId, ProductPageDto dto, Product product) {
        Double amountD;
        Double returnAmountD;

        Double soldAmountD;
        Double contentAmountD;

        Double purchaseAmountD;
        Double productionAmountD;
        Double byProductAmountD;

        Double buyPriceD;
        Double salePriceD;
        Double soldPriceD;
        Double profitD;

        if (branchId != null) {

            amountD = warehouseRepository.amountByProductSingleAndBranchId(product.getId(), branchId);
            returnAmountD = tradeProductRepository.backingByProductSingleAndBranchId(product.getId(), branchId);

            soldAmountD = tradeProductRepository.soldQuantityByProductSingleAndBranchId(product.getId(), branchId);
            contentAmountD = contentProductRepository.contentAmountByProductSingleAndBranchId(product.getId(), branchId);

            purchaseAmountD = purchaseProductRepository.quantityByProductSingleAndBranchId(product.getId(), branchId);
            byProductAmountD = contentProductRepository.byProductByProductSingleAndBranchId(product.getId(), branchId);
            productionAmountD = productionRepository.quantityByProductSingleAndBranchId(product.getId(), branchId);

            soldPriceD = tradeProductRepository.soldPriceByProductSingleAndBranchId(product.getId(), branchId);
            profitD = tradeProductRepository.profitByProductSingleAndBranchId(product.getId(), branchId);
            buyPriceD = fifoCalculationRepository.buyPriceByProductSingleAndBranchId(product.getId(), branchId);
            salePriceD = warehouseRepository.salePriceByProductSingleAndBranchId(product.getId(), branchId);

        } else {

            amountD = warehouseRepository.amountByProductSingle(product.getId());
            returnAmountD = tradeProductRepository.backingByProductSingle(product.getId());

            soldAmountD = tradeProductRepository.soldQuantityByProductSingle(product.getId());
            contentAmountD = contentProductRepository.contentAmountByProductSingle(product.getId());

            purchaseAmountD = purchaseProductRepository.quantityByProductSingle(product.getId());
            byProductAmountD = contentProductRepository.byProductByProductSingle(product.getId());
            productionAmountD = productionRepository.quantityByProductSingle(product.getId());

            soldPriceD = tradeProductRepository.soldPriceByProductSingle(product.getId());
            profitD = tradeProductRepository.profitByProductSingle(product.getId());
            buyPriceD = fifoCalculationRepository.buyPriceByProductSingle(product.getId());
            salePriceD = warehouseRepository.salePriceByProductSingle(product.getId());

        }

        double amount = amountD == null ? 0 : amountD;
        double returnAmount = returnAmountD == null ? 0 : returnAmountD;

        double soldAmount = soldAmountD == null ? 0 : soldAmountD;
        soldAmount += contentAmountD == null ? 0 : contentAmountD;

        double purchaseAmount = purchaseAmountD == null ? 0 : purchaseAmountD;
        double productionAmount = productionAmountD == null ? 0 : productionAmountD;
        double byProductAmount = byProductAmountD == null ? 0 : byProductAmountD;

        double salePrice = salePriceD == null ? 0 : salePriceD;
        double buyPrice = buyPriceD == null ? 0 : buyPriceD;
        double soldPrice = soldPriceD == null ? 0 : soldPriceD;
        double profit = profitD == null ? 0 : profitD;

        int days = (int) (System.currentTimeMillis() - product.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24) + 1;
        double average = soldAmount / days;

        dto.setAverage(average);
        if (average != 0) dto.setDay(Math.round(amount / average));
        dto.setWeek(average * 7);
        dto.setMonth(average * 30);

        dto.setAmount(amount);
        dto.setSoldAmount(soldAmount);
        dto.setPurchaseAmount(purchaseAmount + productionAmount + byProductAmount);
        dto.setReturnAmount(returnAmount);

        dto.setSalePrice(salePrice);
        dto.setProfit(profit);
        dto.setBuyPrice(buyPrice);
        dto.setSoldPrice(soldPrice);
    }

    public ApiResponse getOneAmount(UUID productId, UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductAbout> aboutPage;

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return new ApiResponse("PRODUCT NOT FOUND", false);
        }
        Product product = optionalProduct.get();
        if (branchId != null) {
            aboutPage = productAboutRepository.findAllByProductIdAndBranchIdOrderByCreatedAtDesc(productId, branchId, pageable);
        } else {
            aboutPage = productAboutRepository.findAllByProductIdOrderByCreatedAtDesc(productId, pageable);
        }

        List<ProductAboutDto> aboutDtoList = new ArrayList<>();
        setProductAboutDtoList(aboutDtoList, aboutPage.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("aboutDtoList", aboutDtoList);
        response.put("currentPage", aboutPage.getNumber());
        response.put("totalItem", aboutPage.getTotalElements());
        response.put("totalPage", aboutPage.getTotalPages());
        return new ApiResponse(true, response);
    }

    private void setProductAboutDtoList(List<ProductAboutDto> list, List<ProductAbout> productAboutList) {
        for (ProductAbout about : productAboutList) {
            ProductAboutDto dto = new ProductAboutDto(
                    about.getCreatedAt(),
                    about.getDescription(),
                    about.getAmount()
            );
            dto.setName(about.getProduct().getName());
            dto.setMeasurement(about.getProduct().getMeasurement().getName());
            list.add(dto);
        }
    }
}
