package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.mapper.ExchangeProductBranchMapper;
import uz.pdp.springsecurity.mapper.ExchangeProductMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ExchangeBranchGetOneDto;
import uz.pdp.springsecurity.payload.ExchangeProductBranchDTO;
import uz.pdp.springsecurity.payload.ExchangeProductDTO;
import uz.pdp.springsecurity.repository.*;

import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExchangeProductBranchService {

    @Autowired
    ExchangeProductBranchRepository exchangeProductBranchRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ExchangeStatusRepository exchangeStatusRepository;

    @Autowired
    ExchangeProductRepository exchangeProductRepository;

    private final ExchangeProductBranchMapper mapper;

    private final ExchangeProductMapper exchangeProductMapper;

    private final WarehouseRepository warehouseRepository;

    private final WarehouseService warehouseService;


    public ApiResponse create(ExchangeProductBranchDTO exchangeProductBranchDTO) {

        UUID businessId = exchangeProductBranchDTO.getBusinessId();
        UUID shippedBranchId = exchangeProductBranchDTO.getShippedBranchId();
        UUID receivedBranchId = exchangeProductBranchDTO.getReceivedBranchId();
        UUID exchangeStatusId = exchangeProductBranchDTO.getExchangeStatusId();

        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        Optional<Branch> optionalShippedBranch = branchRepository.findById(shippedBranchId);
        Optional<Branch> optionalReceivedBranch = branchRepository.findById(receivedBranchId);
        Optional<ExchangeStatus> optionalExchangeStatus = exchangeStatusRepository.findById(exchangeStatusId);
//        Optional<Warehouse> optionalWarehouseShippedBranch = warehouseRepository.findByBranchId(shippedBranchId);
//        Optional<Warehouse> optionalWarehouseReceivedBranch = warehouseRepository.findByBranchId(receivedBranchId);

//        if (optionalWarehouseShippedBranch.isEmpty()) {
//            return new ApiResponse("not found shippedBranch warehouse");
//        }
//        if (optionalWarehouseReceivedBranch.isEmpty()) {
//            return new ApiResponse("not found receivedBranch warehouse");
//        }
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found business", false);
        }
        if (optionalShippedBranch.isEmpty()) {
            return new ApiResponse("not found shipped Branch", false);
        }
        if (optionalReceivedBranch.isEmpty()) {
            return new ApiResponse("not found received Branch", false);
        }
        if (optionalExchangeStatus.isEmpty()) {
            return new ApiResponse("not found exchange status", false);
        }

        ExchangeProductBranch exchangeProductBranch = mapper.toEntity(exchangeProductBranchDTO);

        return warehouseService.createOrUpdateExchangeProductBranch(exchangeProductBranchDTO, exchangeProductBranch, false);
    }

    /*public ApiResponse paySalary(UUID id, ExchangeProductBranchDTO exchangeProductBranchDTO) {

        UUID businessId = exchangeProductBranchDTO.getBusinessId();
        UUID shippedBranchId = exchangeProductBranchDTO.getShippedBranchId();
        UUID receivedBranchId = exchangeProductBranchDTO.getReceivedBranchId();
        UUID exchangeStatusId = exchangeProductBranchDTO.getExchangeStatusId();
        Optional<ExchangeProductBranch> optionalExchangeProductBranch = exchangeProductBranchRepository.findById(id);

        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        Optional<Branch> optionalShippedBranch = branchRepository.findById(shippedBranchId);
        Optional<Branch> optionalReceivedBranch = branchRepository.findById(receivedBranchId);
        Optional<ExchangeStatus> optionalExchangeStatus = exchangeStatusRepository.findById(exchangeStatusId);
        Optional<Warehouse> optionalWarehouseShippedBranch = warehouseRepository.findByBranchId(shippedBranchId);
        Optional<Warehouse> optionalWarehouseReceivedBranch = warehouseRepository.findByBranchId(receivedBranchId);

        if (optionalWarehouseShippedBranch.isEmpty()) {
            return new ApiResponse("not found shippedBranch warehouse");
        }
        if (optionalWarehouseReceivedBranch.isEmpty()) {
            return new ApiResponse("not found receivedBranch warehouse");
        }
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found business", false);
        }
        if (optionalShippedBranch.isEmpty()) {
            return new ApiResponse("not found shipped Branch", false);
        }
        if (optionalReceivedBranch.isEmpty()) {
            return new ApiResponse("not found received Branch", false);
        }
        if (optionalExchangeStatus.isEmpty()) {
            return new ApiResponse("not found exchange status", false);
        }
        if (optionalExchangeProductBranch.isEmpty()) {
            return new ApiResponse("not found exchange product exchange", false);
        }

        mapper.update(exchangeProductBranchDTO, optionalExchangeProductBranch.get());
        return warehouseService
                .createOrUpdateExchangeProductBranch(exchangeProductBranchDTO, optionalExchangeProductBranch.get(), true);
    }*/


    public ApiResponse getOne(UUID id) {
        Optional<ExchangeProductBranch> optional = exchangeProductBranchRepository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found exchange product branch ", false);
        }
        ExchangeProductBranch exchangeProductBranch = optional.get();
        List<ExchangeBranchGetOneDto> getOneDtoList = new ArrayList<>();
        for (ExchangeProduct exchangeProduct : exchangeProductBranch.getExchangeProductList()) {
            ExchangeBranchGetOneDto branchGetOneDto = new ExchangeBranchGetOneDto();
            if (exchangeProduct.getProduct() != null) {
                branchGetOneDto.setProductName(exchangeProduct.getProduct().getName());
                branchGetOneDto.setBarCode(exchangeProduct.getProduct().getBarcode());
            } else {
                branchGetOneDto.setProductName(exchangeProduct.getProductTypePrice().getName());
                branchGetOneDto.setBarCode(exchangeProduct.getProductTypePrice().getBarcode());
            }
            branchGetOneDto.setExchangeProductQuantity(exchangeProduct.getExchangeProductQuantity());
            getOneDtoList.add(branchGetOneDto);
        }
        return new ApiResponse("found", true, getOneDtoList);
    }

    public ApiResponse deleteOne(UUID id) {
        if (exchangeProductBranchRepository.findById(id).isEmpty()) return new ApiResponse("NOT FOUND", false);
        exchangeProductBranchRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getByDate(Date exchangeDate, UUID business_id) {
        List<ExchangeProductBranch> allByExchangeDate = exchangeProductBranchRepository.findAllByExchangeDateAndBusiness_IdAndDeleteIsFalse(exchangeDate, business_id);
        return new ApiResponse("FOUND", true, mapper.toDtoList(allByExchangeDate));
    }

    public ApiResponse getByStatusId(UUID exchangeStatusId, UUID branch_id) {
        List<ExchangeProductBranch> allByExchangeStatus_id = exchangeProductBranchRepository.findAllByExchangeStatus_IdAndBusiness_IdAndDeleteIsFalse(exchangeStatusId, branch_id);
        if (allByExchangeStatus_id.isEmpty()) return new ApiResponse("NOT FOUND", false);

        return new ApiResponse("FOUND", true, mapper.toDtoList(allByExchangeStatus_id));
    }

    public ApiResponse getByBusinessId(UUID businessId) {
        List<ExchangeProductBranch> allByBusinessId = exchangeProductBranchRepository.findAllByBusiness_IdAndDeleteIsFalse(businessId);
        allByBusinessId.addAll(exchangeProductBranchRepository.findAllByBusiness_IdAndDeleteIsNull(businessId));
        if (allByBusinessId.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBusinessId);
    }

    public ApiResponse getByBranchId(UUID businessId) {
        List<ExchangeProductBranch> allByBusinessId = exchangeProductBranchRepository.findAllByShippedBranch_IdAndDeleteIsFalse(businessId);
        if (allByBusinessId.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBusinessId);
    }

    public ApiResponse getByShippedBranchId(UUID shippedBranch_id) {
        List<ExchangeProductBranch> allByShippedBranch_id = exchangeProductBranchRepository.findAllByShippedBranch_IdAndDeleteIsFalse(shippedBranch_id);
        if (allByShippedBranch_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByShippedBranch_id);
    }

    public ApiResponse getByReceivedBranchId(UUID receivedBranch_id) {
        List<ExchangeProductBranch> allByShippedBranch_id = exchangeProductBranchRepository.findAllByReceivedBranch_IdAndDeleteIsFalse(receivedBranch_id);
        if (allByShippedBranch_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByShippedBranch_id);
    }
}
