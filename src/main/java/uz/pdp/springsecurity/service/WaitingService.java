package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.Type;
import uz.pdp.springsecurity.mapper.WaitingMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final WaitingRepository waitingRepository;
    private final ProductRepository productRepository;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final WaitingMapper waitingMapper;
    private final WarehouseRepository warehouseRepository;

    public ApiResponse create(WaitingDTO waitingDTO) {
        Optional<Branch> optionalBranch = branchRepository.findById(waitingDTO.getBranchId());
        if (optionalBranch.isEmpty()) return new ApiResponse("BRANCH NOT FOUND", false);

        Optional<User> optionalUser = userRepository.findById(waitingDTO.getUserId());
        if (optionalUser.isEmpty()) return new ApiResponse("USER NOT FOUND", false);

        Customer customer = null;
        if (waitingDTO.getCustomerId() != null) {
            Optional<Customer> optionalCustomer = customerRepository.findById(waitingDTO.getCustomerId());
            if (optionalCustomer.isPresent())
                customer = optionalCustomer.get();
        }

        List<WaitingProduct> waitingProductList = new ArrayList<>();
        ApiResponse apiResponse = createHelper(waitingProductList, waitingDTO.getWaitingProductDtoList());
        if (!apiResponse.isSuccess())
            return apiResponse;

        waitingRepository.save(new Waiting(
                optionalUser.get(),
                optionalBranch.get(),
                customer,
                waitingDTO.getDollar(),
                waitingDTO.getGross(),
                waitingDTO.getTotalSum(),
                waitingDTO.getQuantity(),
                waitingDTO.getDescription(),
                waitingProductList));
        return new ApiResponse("SUCCESS", true);
    }

    private ApiResponse createHelper(List<WaitingProduct> waitingProductList, List<WaitingProductDto> waitingProductDtoList) {
        for (WaitingProductDto dto : waitingProductDtoList) {
            WaitingProduct waitingProduct = new WaitingProduct(
                    dto.getQuantity(),
                    dto.getTotalPrice(),
                    dto.getSalePrice(),
                    dto.isSubMeasurement()
            );
            Optional<Product> optionalProduct = productRepository.findById(dto.getProductId());
            Optional<ProductTypePrice> optionalProductTypePrice = productTypePriceRepository.findById(dto.getProductId());

            if (optionalProduct.isPresent())
                waitingProduct.setProduct(optionalProduct.get());
            else if (optionalProductTypePrice.isPresent()) {
                waitingProduct.setProductTypePrice(optionalProductTypePrice.get());
            }else {
                return new ApiResponse("PRODUCT NOT FOUND", false);
            }
            waitingProductList.add(waitingProduct);
        }
        if (waitingProductList.isEmpty())
            return new ApiResponse("PRODUCT NOT FOUND", false);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse get(UUID branchId) {
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);
        List<Waiting> waitingList = waitingRepository.findAllByBranchId(branchId);
        if (waitingList.isEmpty())
            return new ApiResponse("WAITING NOT FOUND", false);
        return new ApiResponse("SUCCESS", true, toWaitingGetDtoList(waitingList));
    }

    public ApiResponse delete(UUID id) {
        if (!waitingRepository.existsById(id))
            return new ApiResponse("WAITING NOT FOUND", false);
        waitingRepository.deleteById(id);
        return new ApiResponse("SUCCESS", true);
    }

    public List<WaitingGetDto> toWaitingGetDtoList(List<Waiting> waitingList) {
        List<WaitingGetDto> waitingGetDtoList = new ArrayList<>();
        for (Waiting waiting : waitingList) {
            WaitingGetDto waitingGetDto = waitingMapper.toGetDto(waiting);
            waitingGetDto.setWaitingProductGetDtoList(toWaitingProductGetDtoList(waiting));
            waitingGetDtoList.add(waitingGetDto);
        }
        return waitingGetDtoList;
    }

    private List<WaitingProductGetDto> toWaitingProductGetDtoList(Waiting waiting) {
        List<WaitingProductGetDto> list = new ArrayList<>();
        for (WaitingProduct waitingProduct : waiting.getWaitingProductList()) {
            list.add(toWaitingProductGetDto(waiting.getBranch().getId(), waitingProduct));
        }
        return list;
    }

    private WaitingProductGetDto toWaitingProductGetDto(UUID branchId, WaitingProduct waitingProduct) {
        WaitingProductGetDto dto = new WaitingProductGetDto();
        dto.setQuantity(waitingProduct.getQuantity());
        dto.setTotalPrice(waitingProduct.getTotalPrice());
        dto.setSalePrice(waitingProduct.getSalePrice());
        dto.setSubMeasurement(waitingProduct.getSubMeasurement() != null && waitingProduct.getSubMeasurement());

        Optional<Warehouse> optionalWarehouse = Optional.empty();
        if (waitingProduct.getProduct() != null) {
            dto.setProductId(waitingProduct.getProduct().getId());
            dto.setProductName(waitingProduct.getProduct().getName());
            dto.setType(waitingProduct.getProduct().getType().name());
            dto.setMeasurement(waitingProduct.getProduct().getMeasurement().getName());
            if (waitingProduct.getProduct().getMeasurement().getSubMeasurement() != null){
                dto.setSubMeasurementName(waitingProduct.getProduct().getMeasurement().getSubMeasurement().getName());
                dto.setSubMeasurementValue(waitingProduct.getProduct().getMeasurement().getValue());
            }
            optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branchId, waitingProduct.getProduct().getId());
        } else if (waitingProduct.getProductTypePrice() != null) {
            dto.setProductTypePriceId(waitingProduct.getProductTypePrice().getId());
            dto.setProductName(waitingProduct.getProductTypePrice().getName());
            dto.setType(Type.MANY.name());
            dto.setMeasurement(waitingProduct.getProductTypePrice().getProduct().getMeasurement().getName());
            if (waitingProduct.getProductTypePrice().getProduct().getMeasurement().getSubMeasurement() != null){
                dto.setSubMeasurementName(waitingProduct.getProductTypePrice().getProduct().getMeasurement().getSubMeasurement().getName());
                dto.setSubMeasurementValue(waitingProduct.getProductTypePrice().getProduct().getMeasurement().getValue());
            }
            optionalWarehouse = warehouseRepository.findByBranchIdAndProductTypePriceId(branchId, waitingProduct.getProductTypePrice().getId());
        }
        dto.setAmount(optionalWarehouse.map(Warehouse::getAmount).orElse(0.0));
        return dto;
    }
}
