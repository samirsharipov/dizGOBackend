package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.entity.TradeProduct;
import uz.pdp.springsecurity.payload.ProductReportDto;

@Mapper(componentModel = "spring")
public interface ProductsReportMapper {}
