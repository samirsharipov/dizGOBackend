package uz.dizgo.erp.service.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Product;
import uz.dizgo.erp.entity.ProductActivityLog;
import uz.dizgo.erp.entity.template.ProductActionType;
import uz.dizgo.erp.payload.ProductDTO;
import uz.dizgo.erp.repository.logger.ProductActivityLogRepository;
import uz.dizgo.erp.utils.DiffUtil;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductActivityLogger {

    private final ProductActivityLogRepository logRepository;
    private final ObjectMapper objectMapper;

    public void logCreate(Product product) {
        Map<String, Object> newData = DiffUtil.toFlatMap(product);
        log(product.getId(), ProductActionType.CREATE, null, newData, null);
    }

    public void logUpdate(UUID productId, ProductDTO oldProduct, ProductDTO newProduct) {
        Map<String, Map<String, Object>> diff = DiffUtil.getDiff(oldProduct, newProduct);
        log(productId, ProductActionType.UPDATE,
                diff.get("old_data"), diff.get("new_data"), null);
    }

    public void logDelete(Product product) {
        log(product.getId(), ProductActionType.DELETE, product, null, null);
    }

    public void logPurchase(UUID productId,Product oldProduct, Product newProduct, Map<String, Object> extraData) {
        Map<String, Map<String, Object>> diff = DiffUtil.getDiff(oldProduct, newProduct);
        log(productId, ProductActionType.PURCHASE,
                diff.get("old_data"), diff.get("new_data"), extraData
        );
    }

    public void logTrade(UUID productId, Map<String, Object> extraData) {
        log(productId, ProductActionType.TRADE, null, null, extraData);
    }

    private void log(UUID productId, ProductActionType actionType,
                     Object oldData, Object newData, Object extraData) {
        try {
            ProductActivityLog log = new ProductActivityLog();
            log.setProductId(productId);
            log.setActionType(actionType);
            log.setOldData(oldData != null ? objectMapper.writeValueAsString(oldData) : "{}");
            log.setNewData(newData != null ? objectMapper.writeValueAsString(newData) : "{}");
            log.setExtraData(extraData != null ? objectMapper.writeValueAsString(extraData) : "{}");

            logRepository.save(log);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Logni JSON ga o‘tkazishda xatolik", e);
        }
    }
}
