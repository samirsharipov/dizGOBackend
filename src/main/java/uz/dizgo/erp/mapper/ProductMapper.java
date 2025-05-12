package uz.dizgo.erp.mapper;

import uz.dizgo.erp.entity.Product;
import uz.dizgo.erp.payload.ProductDTO;


public class ProductMapper {
    public static void update(Product from, ProductDTO to) {
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
        to.setMeasurement(from.getMeasurement().getName());
        to.setCategory(from.getCategory().getName());
        to.setBrand(from.getBrand().getName());
    }
}
