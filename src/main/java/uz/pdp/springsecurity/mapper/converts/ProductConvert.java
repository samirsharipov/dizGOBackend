package uz.pdp.springsecurity.mapper.converts;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.payload.ProductGetDto;
import uz.pdp.springsecurity.repository.CategoryTranslateRepository;
import uz.pdp.springsecurity.repository.MeasurementTranslateRepository;
import uz.pdp.springsecurity.repository.WarehouseRepository;
import uz.pdp.springsecurity.security.JwtProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConvert {

    private final WarehouseRepository warehouseRepository;
    private final CategoryTranslateRepository categoryTranslateRepository;
    private final MeasurementTranslateRepository measurementTranslateRepository;
    Logger log = LoggerFactory.getLogger(JwtProvider.class);

    public ProductGetDto convertToDto(Product product, UUID branchId, String languageCode) {
        ProductGetDto dto = new ProductGetDto();
        fillBasicInfo(dto, product);
        fillTechnicalInfo(dto, product);
        fillAgreementInfo(dto, product);
        fillRelationsInfo(dto, product, languageCode);
        fillWarehouseAndBranchesInfo(dto, product, branchId);
        return dto;
    }

    private void fillBasicInfo(ProductGetDto dto, Product product) {
        dto.setId(product.getId());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setLongDescription(product.getLongDescription());
        dto.setKeywords(product.getKeywords());
        dto.setAttributes(product.getAttributes());
    }

    private void fillTechnicalInfo(ProductGetDto dto, Product product) {
        dto.setUniqueSKU(product.getUniqueSKU());
        dto.setSalePrice(product.getSalePrice());
        dto.setBuyPrice(product.getBuyPrice());
        dto.setLength(product.getLength());
        dto.setWidth(product.getWidth());
        dto.setHeight(product.getHeight());
        dto.setWeight(product.getWeight());
        dto.setBarcode(product.getBarcode());
        dto.setPluCode(product.getPluCode());
        dto.setMxikCode(product.getMXIKCode());
        dto.setMinQuantity(product.getMinQuantity());
        dto.setGrossPrice(product.getSalePrice());
        dto.setKpi(product.getKpi());
        dto.setKpiPercent(product.getKpiPercent());

    }

    private void fillAgreementInfo(ProductGetDto dto, Product product) {
        dto.setHsCode12(product.getHsCode12());
        dto.setHsCode22(product.getHsCode22());
        dto.setHsCode32(product.getHsCode32());
        dto.setHsCode44(product.getHsCode44());
        dto.setAgreementExportsID(product.getAgreementExportsID());
        dto.setAgreementExportsPID(product.getAgreementExportsPID());
        dto.setAgreementLocalID(product.getAgreementLocalID());
        dto.setAgreementLocalPID(product.getAgreementLocalPID());
        dto.setShippingClass(product.getShippingClass());
    }


    private void fillWarehouseAndBranchesInfo(ProductGetDto dto, Product product, UUID branchId) {


        if (branchId != null) {
            Optional<Warehouse> optionalWarehouse =
                    warehouseRepository.findByProduct_IdAndBranchId(product.getId(), branchId);
            dto.setWarehouseCount(optionalWarehouse.map(Warehouse::getAmount).orElse(0d));
        } else {
            List<Warehouse> warehouseList =
                    warehouseRepository.findAllByProduct_Id(product.getId());
            double totalAmount = 0.0;
            for (Warehouse warehouse : warehouseList) {
                totalAmount += warehouse.getAmount();
            }
            dto.setWarehouseCount(totalAmount);
        }


        dto.setBusinessName(getEntityName(product.getBusiness()));
        dto.setBranches(product.getBranch() != null
                ? product.getBranch().stream().map(Branch::getName).collect(Collectors.toList())
                : null);
        dto.setBranchIds(product.getBranch() != null
                ? product.getBranch().stream().map(Branch::getId).collect(Collectors.toList())
                : null);
    }
    private void fillRelationsInfo(ProductGetDto dto, Product product, String languageCode) {
        dto.setBrandName(getEntityName(product.getBrand()));
        dto.setBrandId(getEntityId(product.getBrand()));
        dto.setCategoryName(getTranslationName(languageCode, getEntityId(product.getCategory()), getEntityName(product.getCategory()), CategoryTranslate.class));
        dto.setCategoryId(getEntityId(product.getCategory()));
        dto.setMeasurementUnit(getTranslationName(languageCode, getEntityId(product.getMeasurement()), getEntityName(product.getMeasurement()), MeasurementTranslate.class));
        dto.setMeasurementUnitId(getEntityId(product.getMeasurement()));
        dto.setPhotoId(getEntityId(product.getPhoto()));
    }

    private String getEntityName(Object entity) {
        return entity == null ? null : invokeGetter(entity, "getName");
    }

    private <T> T getEntityId(Object entity) {
        return entity == null ? null : invokeGetter(entity, "getId");
    }

    private <T> T invokeGetter(Object entity, String methodName) {
        try {
            Object result = entity.getClass().getMethod(methodName).invoke(entity);

            // Cast qilishdan oldin natijani tekshiramiz
            if (result != null && entity.getClass().getMethod(methodName).getReturnType().isAssignableFrom(result.getClass())) {
                return (T) result; // Xavfsiz cast qilish
            }
            return null;
        } catch (Exception e) {
            // Logni yozish
            log.error("Error invoking getter {} on entity {}", methodName, entity, e);
            return null;
        }
    }


    // General method for retrieving translated names
    private String getTranslationName(String languageCode, UUID entityId, String defaultName, Class<?> translationClass) {

       if (languageCode == null) {
           return defaultName;
       }
           Optional<?> optionalTranslation = getTranslation(entityId, languageCode, translationClass);

        return optionalTranslation.map(translation -> {
            try {
                return (String) translation.getClass().getMethod("getName").invoke(translation);
            } catch (Exception e) {
                return defaultName;
            }
        }).orElse(defaultName);
    }

    // Method to handle translation retrieval for different entities
    private Optional<?> getTranslation(UUID entityId, String languageCode, Class<?> translationClass) {
        if (CategoryTranslate.class.equals(translationClass)) {
            return categoryTranslateRepository.findByCategory_IdAndLanguage_Code(entityId, languageCode);
        } else if (MeasurementTranslate.class.equals(translationClass)) {
            return measurementTranslateRepository.findByMeasurement_idAndLanguage_Code(entityId, languageCode);
        } else {
            return Optional.empty();
        }
    }
}
