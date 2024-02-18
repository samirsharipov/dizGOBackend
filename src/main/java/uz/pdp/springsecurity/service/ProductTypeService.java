package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductTypeService {

    private final ProductTypeRepository typeRepository;

    private final ProductTypeValueRepository valueRepository;

    private final BusinessRepository businessRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    ProductTypePriceRepository productTypePriceRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AttachmentRepository attachmentRepository;


    public ApiResponse addProductType(ProductTypePostDto postDto) {

        Optional<Business> optionalBusiness = businessRepository.findById(postDto.getBusinessId());

        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not_found_business_id", false);
        }


        ProductType productType = new ProductType();
        productType.setName(postDto.getName());
        productType.setBusiness(optionalBusiness.get());
        ProductType type = typeRepository.save(productType);
        UUID typeId = type.getId();

        List<String> values = postDto.getValues();

        Optional<ProductType> optional = typeRepository.findById(typeId);

        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        List<ProductTypeValue> productTypeValues = new ArrayList<>();
        for (String value : values) {
            ProductTypeValue typeValue = new ProductTypeValue();
            typeValue.setName(value);
            typeValue.setProductType(optional.get());
            productTypeValues.add(typeValue);
        }
        valueRepository.saveAll(productTypeValues);

        return new ApiResponse("successfully_added", true);
    }

//    public ApiResponse getProductTypeByProductId(UUID productId){
//        List<ProductTypeViewDto> productTypeViewDtoList=new ArrayList<>();
//        Optional<Product> optionalProduct = productRepository.findById(productId);
//        List<ProductTypePrice> priceList = productTypePriceRepository.findAllByProductIdAndActiveTrue(productId);
//        if (optionalProduct.isEmpty()){
//            return new ApiResponse("NOT FOUND", false);
//        }
//        Product product = optionalProduct.get();
//
//        if (priceList.isEmpty()){
//            return new ApiResponse("PRICE NOT FOUND", false);
//        }
//        for (ProductTypePrice productTypePrice : priceList){
//            ProductTypeViewDto productTypeViewDto=new ProductTypeViewDto();
//            if (productTypePrice.getProduct().getId().equals(productId)){
//                productTypeViewDto.setName(product.getName());
//                productTypeViewDto.setBuyPrice(productTypePrice.getBuyPrice());
//                productTypeViewDto.setSalePrice(productTypePrice.getSalePrice());
//                productTypeViewDto.setBarcode(productTypePrice.getBarcode());
//                Optional<Attachment> optionalAttachment = attachmentRepository.findById(productTypePrice.getId());
//                optionalAttachment.ifPresent(attachment -> productTypeViewDto.setPhotoId(attachment.getId()));
//            }
//            productTypeViewDtoList.add(productTypeViewDto);
//            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(product.getId());
//            optionalWarehouse.ifPresent(warehouse -> productTypeViewDto.setAmount(warehouse.getAmount()));
//        }
//
//        return new ApiResponse("FOUND", true,productTypeViewDtoList);
//    }


    public ApiResponse getProductType(User user) {
        UUID uuid = user.getBusiness().getId();
        List<ProductTypeGetDto> productTypePostDto = new ArrayList<>();

        List<ProductType> all = typeRepository.findAllByBusinessId(uuid);
        if (all.isEmpty()){
            return new ApiResponse("Not Found ",false);
        }

        for (ProductType productType : all) {
            ProductTypeGetDto getDto = new ProductTypeGetDto();
            getDto.setProductTypeId(productType.getId());
            getDto.setName(productType.getName());
            getDto.setBusinessId(productType.getBusiness().getId());
            List<ProductTypeValue> values = valueRepository.findAllByProductTypeId(productType.getId());

            List<ProductTypeValueDto> productTypeValueDto = new ArrayList<>();

            for (ProductTypeValue value : values) {
                ProductTypeValueDto typeValueDto = new ProductTypeValueDto();
                typeValueDto.setId(value.getId());
                typeValueDto.setName(value.getName());
                productTypeValueDto.add(typeValueDto);
            }

            getDto.setValues(productTypeValueDto);
            productTypePostDto.add(getDto);
        }

        if (productTypePostDto.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        return new ApiResponse(true, productTypePostDto);
    }


    public ApiResponse getProductTypeById(UUID id) {
        ProductTypeGetDto getDto = new ProductTypeGetDto();
        Optional<ProductType> typeOptional = typeRepository.findById(id);

        if (typeOptional.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        ProductType productType = typeOptional.get();

        List<ProductTypeValue> valueList = valueRepository.findAllByProductTypeId(productType.getId());
        Map<UUID, String> values = new HashMap<>();

        List<ProductTypeValueDto> productTypeValueDto = new ArrayList<>();

        for (ProductTypeValue typeValue : valueList) {
            ProductTypeValueDto typeValueDto = new ProductTypeValueDto();
            typeValueDto.setId(typeValue.getId());
            typeValueDto.setName(typeValue.getName());
            productTypeValueDto.add(typeValueDto);
        }
        getDto.setValues(productTypeValueDto);
        getDto.setName(typeOptional.get().getName());
        getDto.setBusinessId(typeOptional.get().getBusiness().getId());
        getDto.setProductTypeId(typeOptional.get().getId());


        return new ApiResponse(true, getDto);
    }


    public ApiResponse updateProductType(ProductTypePostDto postDto, UUID id) {
        Optional<ProductType> optionalProductType = typeRepository.findById(id);

        if (optionalProductType.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        ProductType productType = optionalProductType.get();
        UUID typeId = productType.getId();

        List<ProductTypeValue> allValue = valueRepository.findAllByProductTypeId(typeId);


        Optional<Business> optionalBusiness = businessRepository.findById(postDto.getBusinessId());

        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found business");
        }


        productType.setName(postDto.getName());
        productType.setBusiness(optionalBusiness.get());
        typeRepository.save(productType);

        List<String> values = postDto.getValues();
        int dtoSize = values.size();
        int dataSize = allValue.size();

        if (dataSize <= dtoSize) {
            for (int i = 0; i < allValue.size(); i++) {
                allValue.get(i).setName(values.get(i));
                valueRepository.save(allValue.get(i));
            }
        }

        if (dataSize < dtoSize) {
            List<ProductTypeValue> productTypeValueList = new ArrayList<>();
            for (String value : values) {
                boolean exist = true;
                for (ProductTypeValue typeValue : allValue) {
                    if (value.equals(typeValue.getName())) {
                        exist = false;
                        break;
                    }
                }
                if (exist) {
                    ProductTypeValue productTypeValue = new ProductTypeValue();
                    productTypeValue.setProductType(productType);
                    productTypeValue.setName(value);
                    productTypeValueList.add(productTypeValue);
                }
            }
            valueRepository.saveAll(productTypeValueList);
        }
        return new ApiResponse("successfully edited", true);
    }

    public ApiResponse deleteProductTypeById(UUID id) {
        Optional<ProductType> typeOptional = typeRepository.findById(id);

        if (typeOptional.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        ProductType productType = typeOptional.get();

        List<ProductTypeValue> valueList = valueRepository.findAllByProductTypeId(productType.getId());
        List<UUID> valuesId = new ArrayList<>();
        for (ProductTypeValue typeValue : valueList) {
            valuesId.add(typeValue.getId());
        }

        valueRepository.deleteAllById(valuesId);
        typeRepository.deleteById(productType.getId());

        return new ApiResponse("successfully deleted", true);
    }
}
