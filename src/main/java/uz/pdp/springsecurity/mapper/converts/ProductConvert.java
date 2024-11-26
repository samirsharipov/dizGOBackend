package uz.pdp.springsecurity.mapper.converts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.entity.Warehouse;
import uz.pdp.springsecurity.payload.ProductGetDto;
import uz.pdp.springsecurity.repository.WarehouseRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConvert {

    private final WarehouseRepository warehouseRepository;

    public ProductGetDto convertToDto(Product product) {
        ProductGetDto dto = new ProductGetDto();
        fillBasicInfo(dto, product);
        fillTechnicalInfo(dto, product);
        fillAgreementInfo(dto, product);
        fillRelationsInfo(dto, product);
        fillWarehouseAndBranchesInfo(dto, product);
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

    private void fillRelationsInfo(ProductGetDto dto, Product product) {
        dto.setBrandName(getEntityName(product.getBrand()));
        dto.setBrandId(getEntityId(product.getBrand()));
        dto.setCategoryName(getEntityName(product.getCategory()));
        dto.setCategoryId(getEntityId(product.getCategory()));
        dto.setMeasurementUnit(getEntityName(product.getMeasurement()));
        dto.setMeasurementUnitId(getEntityId(product.getMeasurement()));
        dto.setPhotoId(getEntityId(product.getPhoto()));
    }

    private void fillWarehouseAndBranchesInfo(ProductGetDto dto, Product product) {
        Optional<Warehouse> optionalWarehouse =
                warehouseRepository.findByProduct_Id(product.getId());
        dto.setWarehouseCount(optionalWarehouse.map(Warehouse::getAmount).orElse(0d));

        dto.setBusinessName(getEntityName(product.getBusiness()));
        dto.setBranches(product.getBranch() != null
                ? product.getBranch().stream().map(Branch::getName).collect(Collectors.toList())
                : null);
        dto.setBranchIds(product.getBranch() != null
                ? product.getBranch().stream().map(Branch::getId).collect(Collectors.toList())
                : null);
    }

    // Bog'langan ob'ektlardan nomini olish
    private String getEntityName(Object entity) {
        if (entity == null) return null;
        try {
            return (String) entity.getClass().getMethod("getName").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    // Bog'langan ob'ektlardan ID olish
    private <T> T getEntityId(Object entity) {
        if (entity == null) return null;
        try {
            return (T) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }
}