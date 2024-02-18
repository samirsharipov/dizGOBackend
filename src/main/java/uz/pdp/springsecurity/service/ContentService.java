package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.mapper.CostMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentProductRepository contentProductRepository;
    private final ProductRepository productRepository;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final BranchRepository branchRepository;
    private final WarehouseRepository warehouseRepository;
    private final CostRepository costRepository;
    private final CostTypeRepository costTypeRepository;
    private final CostMapper costMapper;

    @Transactional
    public ApiResponse add(ContentDto contentDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(contentDto.getBranchId());
        if (optionalBranch.isEmpty()) return new ApiResponse("NOT FOUND BRANCH", false);
        Content content = new Content();
        content.setBranch(optionalBranch.get());
        return createOrEdit(content, contentDto);
    }

    @Transactional
    public ApiResponse edit(UUID contentId, ContentDto contentDto) {
        Optional<Content> optionalContent = contentRepository.findById(contentId);
        return optionalContent.map(content -> createOrEdit(content, contentDto)).orElseGet(() -> new ApiResponse("NOT FOUND CONTENT", false));
    }

    private ApiResponse createOrEdit(Content content, ContentDto contentDto) {
        if (contentDto.getProductId() != null) {
            Optional<Product> optional = productRepository.findById(contentDto.getProductId());
            if (optional.isEmpty()) return new ApiResponse("NOT FOUND PRODUCT", false);
            content.setProduct(optional.get());
        } else if (contentDto.getProductTypePriceId() != null) {
            Optional<ProductTypePrice> optional = productTypePriceRepository.findById(contentDto.getProductTypePriceId());
            if (optional.isEmpty()) return new ApiResponse("NOT FOUND PRODUCT TYPE PRICE", false);
            content.setProductTypePrice(optional.get());
        } else return new ApiResponse("NOT FOUND PRODUCT", false);

        content.setQuantity(contentDto.getQuantity());
        content.setCostEachOne(contentDto.isCostEachOne());
        content.setContentPrice(contentDto.getContentPrice());
        content.setCost(contentDto.getCost());
        content.setTotalPrice(contentDto.getTotalPrice());
        contentRepository.save(content);
        contentProductRepository.deleteAllByContentId(content.getId());
        costRepository.deleteAllByContentId(content.getId());

        ApiResponse apiResponse = saveCostList(content, contentDto.getCostDtoList());
        if (!apiResponse.isSuccess())
            return apiResponse;
        return saveContentProductList(content, contentDto.getContentProductDtoList());
    }

    private ApiResponse saveContentProductList(Content content, List<ContentProductDto> contentProductDtoList) {
        List<ContentProduct> contentProductList = new ArrayList<>();
        for (ContentProductDto contentProductDto : contentProductDtoList) {
            ContentProduct contentProduct = createOrEditContentProduct(new ContentProduct(), contentProductDto);
            if (contentProduct == null)
                return new ApiResponse("NOT FOUND PRODUCT OR PRODUCT TYPE PRICE (ONE OF THEM SHOULD BE NULL)", false);
            contentProduct.setContent(content);
            contentProductList.add(contentProduct);
        }
        contentProductRepository.saveAll(contentProductList);
        return new ApiResponse("SUCCESS", true);
    }

    private ContentProduct createOrEditContentProduct(ContentProduct contentProduct, ContentProductDto contentProductDto) {
        if (contentProductDto.getProductId() != null) {
            Optional<Product> optional = productRepository.findById(contentProductDto.getProductId());
            if (optional.isEmpty()) return null;
            contentProduct.setProduct(optional.get());
        } else {
            Optional<ProductTypePrice> optional = productTypePriceRepository.findById(contentProductDto.getProductTypePriceId());
            if (optional.isEmpty()) return null;
            contentProduct.setProductTypePrice(optional.get());
        }
        contentProduct.setQuantity(contentProductDto.getQuantity());
        contentProduct.setTotalPrice(contentProductDto.getTotalPrice());
        contentProduct.setByProduct(contentProductDto.isByProduct());
        return contentProduct;
    }

    private ApiResponse saveCostList(Content content, List<CostDto> costDtoList) {
        List<Cost> list = new ArrayList<>();
        for (CostDto dto : costDtoList) {
            Optional<CostType> optionalCostType = costTypeRepository.findById(dto.getCostTypeId());
            if (optionalCostType.isEmpty())
                return new ApiResponse("NOT FOUND COST_TYPE", false);
            list.add(new Cost(
                    content,
                    optionalCostType.get(),
                    dto.getSum()
            ));
        }
        costRepository.saveAll(list);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getAll(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) return new ApiResponse("NOT FOUND BRANCH", false);
        List<Content> contentList = contentRepository.findAllByBranchId(branchId);
        if (contentList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse(true, contentList);
    }

    public ApiResponse getAllPageable(UUID branchId, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) return new ApiResponse("NOT FOUND BRANCH", false);
        Page<Content> contents = null;
        if (name != null) {
            contents = contentRepository.findAllByProduct_NameContainingIgnoreCaseAndBranchIdOrProductTypePrice_NameContainingIgnoreCaseAndBranchId(name, branchId, name, branchId, pageable);
        } else {
            contents = contentRepository.findAllByBranch_Id(branchId, pageable);
        }
        if (contents == null) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse(true, contents);
    }

    public ApiResponse getOne(UUID contentId) {
        Optional<Content> optionalContent = contentRepository.findById(contentId);
        if (optionalContent.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Content content = optionalContent.get();
        List<ContentProduct> contentProductList = contentProductRepository.findAllByContentId(contentId);
        if (contentProductList.isEmpty()) return new ApiResponse("NOT FOUND CONTENT PRODUCTS", false);
        GetOneContentProductionDto getOneContentProductionDto = new GetOneContentProductionDto(
                content,
                costMapper.toDtoList(costRepository.findAllByContentId(contentId)),
                contentProductList
        );
        for (ContentProduct contentProduct : contentProductList) {
            if (contentProduct.getProduct() != null) {
                UUID productId = contentProduct.getProduct().getId();
                Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(content.getBranch().getId(), productId);
                if (optionalWarehouse.isPresent()) {
                    Warehouse warehouse = optionalWarehouse.get();
                    double amount = warehouse.getAmount();
                    contentProduct.setProductWarehouseAmount(amount);
                }
            } else if (contentProduct.getProductTypePrice() != null) {
                UUID productId = contentProduct.getProductTypePrice().getId();
                Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(content.getBranch().getId(), productId);
                if (optionalWarehouse.isPresent()) {
                    Warehouse warehouse = optionalWarehouse.get();
                    double amount = warehouse.getAmount();
                    contentProduct.setProductWarehouseAmount(amount);
                }
            }
        }
        return new ApiResponse(true, getOneContentProductionDto);
    }

    public ApiResponse deleteOne(UUID contentId) {
        if (!contentRepository.existsById(contentId)) return new ApiResponse("NOT FOUND", false);
        contentRepository.deleteById(contentId);
        return new ApiResponse("SUCCESS", true);
    }
}
