package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Reason;
import uz.dizgo.erp.entity.ReturnProduct;
import uz.dizgo.erp.entity.Trade;
import uz.dizgo.erp.entity.TradeProduct;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ReturnProductDto;
import uz.dizgo.erp.payload.ReturnProductGetDto;
import uz.dizgo.erp.repository.ReasonRepository;
import uz.dizgo.erp.repository.ReturnProductRepository;
import uz.dizgo.erp.repository.TradeProductRepository;
import uz.dizgo.erp.repository.TradeRepository;


import java.util.*;

@Service
@RequiredArgsConstructor
public class ReturnProductService {

    private final ReturnProductRepository repository;
    private final ReasonRepository reasonRepository;
    private final TradeProductRepository tradeProductRepository;
    private final TradeRepository tradeRepository;

    public ApiResponse create(List<ReturnProductDto> returnProductDTOList) {

        for (ReturnProductDto returnProductDTO : returnProductDTOList) {

            ApiResponse reasonCheck = checkIfPresent(reasonRepository.findById(returnProductDTO.getReasonId()), "Reason not found");
            if (!reasonCheck.isSuccess()) return reasonCheck;

            ApiResponse tradeProductCheck = checkIfPresent(tradeProductRepository.findById(returnProductDTO.getTradeProductId()), "Trade not found");
            if (!tradeProductCheck.isSuccess()) return tradeProductCheck;

            ReturnProduct returnProduct = fromDto(returnProductDTO, (Reason) reasonCheck.getObject());
            TradeProduct tradeProduct = (TradeProduct) tradeProductCheck.getObject();

            // Qaytarish turi asosida jarayonni boshqarish
            if (returnProductDTO.isRefunded()) {
                handleMonetaryRefund(returnProductDTO, tradeProduct);
            } else {
                tradeProduct.setBacking(returnProductDTO.getRefundAmount());
                handleProductExchange(tradeProduct);
            }
            repository.save(returnProduct);
        }
        return new ApiResponse("Return product created", true);
    }

    private ApiResponse checkIfPresent(Optional<?> optional, String errorMessage) {
        return optional.map(o -> new ApiResponse("Found", true, o))
                .orElseGet(() -> new ApiResponse(errorMessage, false));
    }

    private void handleMonetaryRefund(ReturnProductDto returnProductDTO, TradeProduct tradeProduct) {
        tradeProduct.setRefund(true);
        tradeProduct.setBacking(returnProductDTO.getRefundAmount());
        Trade trade = tradeProduct.getTrade();

        double refundMoney = tradeProduct.getProduct().getSalePrice() * returnProductDTO.getRefundAmount();
//        double profitPerUnit = tradeProduct.getProfit() / tradeProduct.getQuantity();

        trade.setTotalSum(trade.getTotalSum() - refundMoney);
//        trade.setProfit(trade.getProfit() - (profitPerUnit * returnProductDTO.getRefundAmount()));

        // To'lov va qarzlarni yangilash kerak bo'lsa, bu yerda bajariladi.
        // trade.setPaidSum(trade.getPaidSum() - refundMoney);

        tradeRepository.save(trade);
        tradeProductRepository.save(tradeProduct);
    }

    private void handleProductExchange(TradeProduct tradeProduct) {
        tradeProduct.setRefund(false);
        tradeProductRepository.save(tradeProduct);
    }

    private static ReturnProduct fromDto(ReturnProductDto returnProductDTO, Reason reason) {
        ReturnProduct returnProduct = new ReturnProduct();
        returnProduct.setInvoice(returnProductDTO.getInvoice());
        returnProduct.setProductId(returnProductDTO.getProductId());
        returnProduct.setQuantity(returnProductDTO.getQuantity());
        returnProduct.setReason(reason);
        returnProduct.setReasonText(returnProductDTO.getReasonText());
        returnProduct.setRefundAmount(returnProductDTO.getRefundAmount());
        returnProduct.setMonetaryRefund(returnProductDTO.isRefunded());
        returnProduct.setBusinessId(reason.getBusinessId());
        returnProduct.setActive(true);
        return returnProduct;
    }

    public ApiResponse getAll(UUID businessId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ReturnProductGetDto> all = repository.findByBusinessId(businessId, pageable);
        if (all.isEmpty()) {
            return new ApiResponse("empty", false);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("list", all.stream().toList());
        response.put("currentPage", all.getNumber());
        response.put("totalPage", all.getTotalPages());
        response.put("totalItem", all.getTotalElements());

        return new ApiResponse("found", true, response);
    }
}