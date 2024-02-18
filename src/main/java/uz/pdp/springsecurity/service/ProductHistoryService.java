package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ProductAmountDto;
import uz.pdp.springsecurity.payload.ProductHistoryGetDto;
import uz.pdp.springsecurity.repository.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {
    private final ProductHistoryRepository productHistoryRepository;
    private final BranchRepository branchRepository;
    private final WarehouseRepository warehouseRepository;
    private final TradeProductRepository tradeProductRepository;
    private final ContentProductRepository contentProductRepository;
    LocalDateTime TODAY_START = LocalDate.now().atStartOfDay();


    // TODO: 7/1/2023 create
    /*public void create(Branch branch, Product product, ProductTypePrice productTypePrice, boolean plus, double plusAmount, double amount, int count) {
        Optional<ProductHistory> optionalProductHistory;
        if (product != null)
            optionalProductHistory = productHistoryRepository.findByBranchIdAndProductIdAndCreatedAtBetween(branch.getId(), product.getId(), Timestamp.valueOf(TODAY_START), Timestamp.valueOf(TODAY_START.plusDays(1)));
        else if (productTypePrice != null)
            optionalProductHistory = productHistoryRepository.findByBranchIdAndProductTypePriceIdAndCreatedAtBetween(branch.getId(), productTypePrice.getId(), Timestamp.valueOf(TODAY_START), Timestamp.valueOf(TODAY_START.plusDays(1)));
        else
            return;

        if (optionalProductHistory.isPresent()) {
            edit(optionalProductHistory.get(), plus, plusAmount, amount);
        } else {
            if (count > 0) return;
            if (productHistoryRepository.existsAllByBranchIdAndCreatedAtBetween(branch.getId(), Timestamp.valueOf(TODAY_START), Timestamp.valueOf(TODAY_START.plusDays(1)))) {
                productHistoryRepository.save(new ProductHistory(
                        product,
                        productTypePrice,
                        branch,
                        amount,
                        0,
                        0
                ));
            } else {
                boolean check = createAll(branch);
                if (!check) return;
            }
            create(branch, product, productTypePrice, plus, plusAmount, amount, 1);
        }
    }*/

    private void edit(ProductHistory history, boolean plus, double plusAmount, double amount) {
        if (plus)
            history.setPlusAmount(history.getPlusAmount() + plusAmount);
        else
            history.setMinusAmount(history.getMinusAmount() + plusAmount);
        history.setAmount(amount);
        productHistoryRepository.save(history);
    }

    private boolean createAll(Branch branch) {
        List<Warehouse> warehouseList = warehouseRepository.findAllByBranchId(branch.getId());
        for (Warehouse warehouse : warehouseList) {
            productHistoryRepository.save(new ProductHistory(
                    warehouse.getProduct(),
                    warehouse.getProductTypePrice(),
                    branch,
                    warehouse.getAmount(),
                    0,
                    0
            ));
        }
        return !warehouseList.isEmpty();
    }

    public ApiResponse get(UUID branchId, Date date, int page, int size) {
        return new ApiResponse("MA'LUMOT TOPILMADI", false);
        /*Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty())
            return new ApiResponse("BRANCH NOT FOUND", false);
        Pageable pageable = PageRequest.of(page, size);
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime from = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0);
        Page<ProductHistory> historyPage = productHistoryRepository.findAllByBranchIdAndCreatedAtBetween(branchId, Timestamp.valueOf(from), Timestamp.valueOf(from.plusDays(1)), pageable);
        List<ProductHistoryGetDto> dtoList = new ArrayList<>();
        for (ProductHistory history : historyPage.getContent()) {
            ProductHistoryGetDto dto = new ProductHistoryGetDto();
            if (history.getProduct() != null) {
                dto.setName(history.getProduct().getName());
                dto.setBarcode(history.getProduct().getBarcode());
                dto.setMeasurement(history.getProduct().getMeasurement().getName());
            } else {
                dto.setName(history.getProductTypePrice().getName());
                dto.setBarcode(history.getProductTypePrice().getBarcode());
                dto.setMeasurement(history.getProductTypePrice().getProduct().getMeasurement().getName());
            }
            dto.setDate(history.getCreatedAt());
            dto.setAmount(history.getAmount());
            dto.setPlusAmount(history.getPlusAmount());
            dto.setMinusAmount(history.getMinusAmount());
            dtoList.add(dto);
        }
        if (historyPage.getTotalElements() == 0){
            if (localDate.getDayOfYear() == TODAY_START.getDayOfYear()) {
                boolean check = createAll(optionalBranch.get());
                if (check)
                    return get(branchId, date, page, size);
            }
            return new ApiResponse("MA'LUMOT TOPILMADI", false);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("dtoList", dtoList);
        response.put("currentPage", historyPage.getNumber());
        response.put("totalItem", historyPage.getTotalElements());
        response.put("totalPage", historyPage.getTotalPages());
        return new ApiResponse("SUCCESS", true, response);*/
    }

    public ApiResponse amount(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Warehouse> warehousePage = warehouseRepository.findAllByBranch_IdAndProduct_ActiveTrue(branchId, pageable);
        List<ProductAmountDto> productAmountDtoList = new ArrayList<>();
        for (Warehouse warehouse : warehousePage.getContent()) {
            LocalDateTime createdAtLocal = warehouse.getCreatedAt().toLocalDateTime();
            int days = TODAY_START.getDayOfYear() - createdAtLocal.getDayOfYear() + 1;
            if (days < 0) days = 365 + days;
            if (days > 30) days = 30;
            Double quantityD;
            Double quantityD2;
            ProductAmountDto dto = new ProductAmountDto();
            if (warehouse.getProduct() != null) {
                dto.setName(warehouse.getProduct().getName());
                dto.setMeasurement(warehouse.getProduct().getMeasurement().getName());
                quantityD = tradeProductRepository.quantityByBranchIdAndProductIdAndCreatedAtBetween(Timestamp.valueOf(TODAY_START.minusDays(days)), Timestamp.valueOf(TODAY_START.plusDays(1)), warehouse.getProduct().getId() ,branchId);
                quantityD2 = contentProductRepository.quantityByBranchIdAndProductIdAndCreatedAtBetween(Timestamp.valueOf(TODAY_START.minusDays(days)), Timestamp.valueOf(TODAY_START.plusDays(1)), warehouse.getProduct().getId() ,branchId);
            } else {
                dto.setName(warehouse.getProductTypePrice().getName());
                dto.setMeasurement(warehouse.getProductTypePrice().getProduct().getMeasurement().getName());
                quantityD = tradeProductRepository.quantityByBranchIdAndProductTypePriceIdAndCreatedAtBetween(Timestamp.valueOf(TODAY_START.minusDays(days)), Timestamp.valueOf(TODAY_START.plusDays(1)), warehouse.getProductTypePrice().getId() ,branchId);
                quantityD2 = contentProductRepository.quantityByBranchIdAndProductTypePriceIdAndCreatedAtBetween(Timestamp.valueOf(TODAY_START.minusDays(days)), Timestamp.valueOf(TODAY_START.plusDays(1)), warehouse.getProductTypePrice().getId() ,branchId);
            }
            double quantity = quantityD == null? 0: quantityD;
            quantity += quantityD2 == null? 0: quantityD2;
            double average = quantity / days;
            dto.setAverage(Math.round(average * 10) / 10.);
            dto.setDay(average == 0 ? 0 : Math.round(warehouse.getAmount() / average));
            dto.setMonth(Math.round(average * (30 - TODAY_START.getDayOfMonth()) * 10) / 10.);
            dto.setAmount(Math.round(warehouse.getAmount() * 10) / 10.);
            productAmountDtoList.add(dto);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("getProductAmountList", productAmountDtoList);
        response.put("currentPage", warehousePage.getNumber());
        response.put("totalItems", warehousePage.getTotalElements());
        response.put("totalPages", warehousePage.getTotalPages());
        return new ApiResponse("SUCCESS", true, response);
    }
}
