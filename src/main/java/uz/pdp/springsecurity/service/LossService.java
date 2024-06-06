package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.ConstantProduct;
import org.springframework.data.domain.Pageable;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LossService {
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final LossRepository lossRepository;
    private final LossProductRepository lossProductRepository;
    private final WarehouseService warehouseService;
    private final FifoCalculationService fifoCalculationService;
    private final ProductAboutRepository productAboutRepository;
    private final WarehouseRepository warehouseRepository;

    public ApiResponse create(LossDTO lossDTO) {
        System.out.println("Received DTO: " + lossDTO);

        Optional<Branch> optionalBranch = branchRepository.findById(lossDTO.getBranchId());
        if (optionalBranch.isEmpty())
            return new ApiResponse("BRANCH NOT FOUND", false);

        Optional<User> optionalUser = userRepository.findById(lossDTO.getUserId());
        if (optionalUser.isEmpty())
            return new ApiResponse("USER NOT FOUND", false);

        Loss loss = new Loss(
                optionalUser.get(),
                optionalBranch.get(),
                lossDTO.getDate(),
                lossDTO.getComment() // Kommentni qabul qilish
        );

        List<LossProduct> lossProductList = new ArrayList<>();
        ApiResponse apiResponse = toLossProductList(loss, lossProductList, lossDTO.getLossProductDtoList());
        if (!apiResponse.isSuccess())
            return apiResponse;

        lossRepository.save(loss);
        lossProductRepository.saveAll(lossProductList);
        return new ApiResponse("SUCCESS", true);
    }

    private ApiResponse toLossProductList(Loss loss, List<LossProduct> lossProductList, List<LossProductDto> lossProductDtoList) {
        for (LossProductDto dto : lossProductDtoList) {
            double quantity = dto.getQuantity();
            String status = findStatus(quantity);
            LossProduct lossProduct = new LossProduct(
                    loss,
                    quantity,
                    status
            );
            if (dto.getProductId() != null) {
                Optional<Product> optionalProduct = productRepository.findById(dto.getProductId());
                if (optionalProduct.isEmpty())
                    return new ApiResponse("PRODUCT NOT FOUND", false);
                lossProduct.setProduct(optionalProduct.get());
                double amount;
                if (warehouseRepository.amountByProductSingle(dto.getProductId()) == null) {
                    amount = 0;
                } else {
                    amount = warehouseRepository.amountByProductSingle(dto.getProductId());
                }
                lossProduct.setLastAmount(amount);
            } else {
                Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository.findById(dto.getProductTypePriceId());
                if (optionalProductTypePrice.isEmpty())
                    return new ApiResponse("PRODUCT NOT FOUND", false);
                lossProduct.setProductTypePrice(optionalProductTypePrice.get());
                lossProduct.setLastAmount(warehouseRepository.amountByProductTypePrice(dto.getProductTypePriceId()));
            }
            warehouseService.createOrEditWareHouseHelper(loss.getBranch(), lossProduct.getProduct(), lossProduct.getProductTypePrice(), quantity);
            fifoCalculationService.createLossProduct(loss.getBranch(), lossProduct);
            lossProductList.add(lossProduct);
            // product haqida saqlash
            productAboutRepository.save(new ProductAbout(
                    lossProduct.getProduct(),
                    lossProduct.getProductTypePrice(),
                    loss.getBranch(),
                    ConstantProduct.LOSE,
                    lossProduct.getQuantity(),
                    UUID.randomUUID()
            ));
        }
        return new ApiResponse("SUCCESS", true);
    }

    private String findStatus(double quantity) {
        if (quantity < 0) {
            return "BENEFIT";
        }
        return "HARM";
    }

    public ApiResponse get(UUID branchId, int page, int size) {
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);

        Pageable pageable = PageRequest.of(page, size); // To'g'ri Pageable interfeysi
        Page<Loss> lossPage = lossRepository.findAllByBranchIdOrderByCreatedAtDesc(branchId, pageable);

        return makeResult(lossPage);
    }

    private List<LossGetDto> toLossGetDtoList(List<Loss> lossList) {
        List<LossGetDto> list = new ArrayList<>();
        for (Loss loss : lossList) {
            list.add(new LossGetDto(
                    loss.getId(),
                    loss.getUser().getFirstName() + " " + loss.getUser().getLastName(),
                    loss.getDate(),
                    loss.getComment() // Qo'shilgan maydon
            ));
        }
        return list;
    }

    public ApiResponse getOne(UUID lossId, UUID branchId) {
        if (!lossRepository.existsById(lossId))
            return new ApiResponse("LOSS NOT FOUND", false);
        if (!branchRepository.existsById(branchId)) {
            return new ApiResponse("BRANCH NOT FOUND", false);
        }
        List<LossProduct> lossProductList = lossProductRepository.findAllByLoss_Branch_IdAndLoss_Id(branchId, lossId);
        List<LossProductGetDto> list = new ArrayList<>();
        for (LossProduct l : lossProductList) {
            LossProductGetDto dto = new LossProductGetDto();
            if (l.getProduct() != null) {
                dto.setName(l.getProduct().getName());
                dto.setMeasurement(l.getProduct().getMeasurement().getName());
                dto.setLastAmount(l.getLastAmount());
                dto.setStatus(l.getStatus());
                dto.setBuyPrice(l.getProduct().getBuyPrice());
            } else {
                dto.setName(l.getProductTypePrice().getName());
                dto.setMeasurement(l.getProductTypePrice().getProduct().getMeasurement().getName());
                dto.setLastAmount(l.getLastAmount());
                dto.setStatus(l.getStatus());
                dto.setBuyPrice(l.getProductTypePrice().getBuyPrice());
            }
            dto.setQuantity(l.getQuantity());
            list.add(dto);
        }
        return new ApiResponse("SUCCESS", true, list);
    }

    public ApiResponse getSearchByDate(UUID branchId, Date startDate, Date endDate, Integer page, Integer size) {
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        Page<Loss> lossPage = lossRepository.findAllByBranchIdAndCreatedAtBetweenOrderByCreatedAtDesc(branchId, startDate, endDate, (org.springframework.data.domain.Pageable) pageable);

        return makeResult(lossPage);
    }

    private ApiResponse makeResult(Page<Loss> lossPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("list", toLossGetDtoList(lossPage.getContent()));
        response.put("currentPage", lossPage.getNumber());
        response.put("totalItems", lossPage.getTotalElements());
        response.put("totalPages", lossPage.getTotalPages());
        return new ApiResponse("SUCCESS", true, response);
    }

    public ApiResponse getTotalSumLos(UUID branchId, Date startDate, Date endDate) {
        Branch branch = branchRepository.findById(branchId).orElse(null);
        if (branch == null) {
            return new ApiResponse("Filial topilmadi", false);
        } else {
            Double harmBuyPriceMany = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductTypePriceBuyPrice(branchId, "HARM", startDate, endDate);
            Double harmBuyPriceSingle = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductBuyPrice(branchId, "HARM", startDate, endDate);
            Double benefitBuyPriceMany = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductTypePriceBuyPrice(branchId, "BENEFIT", startDate, endDate);
            Double benefitBuyPriceSingle = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductBuyPrice(branchId, "BENEFIT", startDate, endDate);
            Double harmSalePriceMany = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductTypePriceSalePrice(branchId, "HARM", startDate, endDate);
            Double harmSalePriceSingle = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductSalePrice(branchId, "HARM", startDate, endDate);
            Double benefitSalePriceMany = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductTypePriceSalePrice(branchId, "BENEFIT", startDate, endDate);
            Double benefitSalePriceSingle = lossProductRepository.calculateTotalCostByStatusAndDateRangeAndProductSalePrice(branchId, "BENEFIT", startDate, endDate);
            Map<String, Object> sale = new HashMap<>();
            sale.put("harm", (harmSalePriceMany == null ? 0 : harmSalePriceMany) + (harmSalePriceSingle == null ? 0 : harmSalePriceSingle));
            sale.put("benefit", (benefitSalePriceMany == null ? 0 : benefitSalePriceMany) + (benefitSalePriceSingle == null ? 0 : benefitSalePriceSingle));
            Map<String, Object> buy = new HashMap<>();
            buy.put("harm", (harmBuyPriceMany == null ? 0 : harmBuyPriceMany) + (harmBuyPriceSingle == null ? 0 : harmBuyPriceSingle));
            buy.put("benefit", (benefitBuyPriceMany == null ? 0 : benefitBuyPriceMany) + (benefitBuyPriceSingle == null ? 0 : benefitBuyPriceSingle));
            Map<String, Object> data = new HashMap<>();
            data.put("sale", sale);
            data.put("buy", buy);
            return new ApiResponse("Found", true, data);
        }
    }

    public ApiResponse getProductsByBranch(UUID branchId, Date startDate, Date endDate, Integer page, Integer limit) {
        Branch branch = branchRepository.findById(branchId).orElse(null);
        if (branch == null) {
            return new ApiResponse("Filial topilmadi!", false);
        }
        Pageable pageable = (Pageable) PageRequest.of(page - 1, limit);
        Page<LossProduct> lossProductPage = null;
        if (startDate == null || endDate == null) {
            lossProductPage = lossProductRepository.findAllByLoss_Branch_IdOrderByCreatedAtDesc(branchId, (org.springframework.data.domain.Pageable) pageable);
        } else {
            lossProductPage = lossProductRepository.findAllByLoss_Branch_IdAndCreatedAtBetweenOrderByCreatedAtDesc(branchId, startDate, endDate, (org.springframework.data.domain.Pageable) pageable);
        }
        List<LossProductGetDto> list = new ArrayList<>();
        for (LossProduct l : lossProductPage.getContent()) {
            LossProductGetDto dto = new LossProductGetDto();
            if (l.getProduct() != null) {
                dto.setName(l.getProduct().getName());
                dto.setMeasurement(l.getProduct().getMeasurement().getName());
                dto.setLastAmount(l.getLastAmount());
                dto.setStatus(l.getStatus());
                dto.setBuyPrice(l.getProduct().getBuyPrice());
            } else {
                dto.setName(l.getProductTypePrice().getName());
                dto.setMeasurement(l.getProductTypePrice().getProduct().getMeasurement().getName());
                dto.setLastAmount(l.getLastAmount());
                dto.setStatus(l.getStatus());
                dto.setBuyPrice(l.getProductTypePrice().getBuyPrice());
            }
            dto.setQuantity(l.getQuantity());
            list.add(dto);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("totalPages", lossProductPage.getTotalPages());
        return new ApiResponse("LossProducts", true, data);
    }
}
