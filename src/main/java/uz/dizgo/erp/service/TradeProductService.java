package uz.dizgo.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.TradeProduct;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.TradeProductRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TradeProductService{

    @Autowired
    TradeProductRepository tradeProductRepository;

    public ApiResponse getAllTrade(UUID businessId) {
        List<TradeProduct> tradeProductList = tradeProductRepository.findAllByProduct_Business_IdOrderByTradedQuantity(businessId);
        if (tradeProductList.isEmpty()){
            return new ApiResponse("Traded Product Not Found");
        }

        return new ApiResponse("Found",true,tradeProductList);
    }

}
