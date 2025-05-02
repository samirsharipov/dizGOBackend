package uz.dizgo.erp.mapper;

import uz.dizgo.erp.entity.Product;

import java.util.UUID;

public class ProductMapper {
    public static void update(Product from , Product to){
        to.setId(from.getId());
        to.setName(from.getName());
        to.setDescription(from.getDescription());
        to.setLongDescription(from.getLongDescription());
        to.setKeywords(from.getKeywords());
        to.setAttributes(from.getAttributes());
        to.setPluCode(from.getPluCode());
        to.setBuyPrice(from.getBuyPrice());
        to.setSalePrice(from.getSalePrice());
        to.setMXIKCode(from.getMXIKCode());
        to.setKpi(from.getKpi());
        to.setMinQuantity(from.getMinQuantity());
        to.setMeasurement(from.getMeasurement());
    }
}
