package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.HistoryName;
import uz.pdp.springsecurity.mapper.CostMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.AppConstant;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionService {
    private final ProductionRepository productionRepository;
    private final ContentProductRepository contentProductRepository;
    private final ProductRepository productRepository;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final BranchRepository branchRepository;
    private final WarehouseService warehouseService;
    private final FifoCalculationService fifoCalculationService;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final SalaryCountService salaryCountService;
    private final PrizeService prizeService;
    private final LostProductionRepository lostProductionRepository;
    private final CostTypeRepository costTypeRepository;
    private final CostRepository costRepository;
    private final CostMapper costMapper;
    private final HistoryRepository historyRepository;

    public ApiResponse add(ProductionDto productionDto) {

        Optional<Branch> optionalBranch = branchRepository.findById(productionDto.getBranchId());
        if (optionalBranch.isEmpty()) return new ApiResponse("NOT FOUND BRANCH", false);
        Branch branch = optionalBranch.get();
        if (productionDto.getInvalid() >= productionDto.getTotalQuantity())
            return new ApiResponse("WRONG QUANTITY", false);
        if (productionDto.getDate() == null) return new ApiResponse("NOT FOUND DATE", false);

        ApiResponse apiResponse = checkBeforeProduction(branch, productionDto.getContentProductDtoList());
        if (!apiResponse.isSuccess()) return apiResponse;

        double cost = productionDto.isCostEachOne() ? productionDto.getTotalQuantity() : 1;
        Production production = new Production(
                branch,
                productionDto.getDate(),
                productionDto.getTotalQuantity(),
                productionDto.getTotalQuantity() - productionDto.getInvalid(),
                productionDto.getInvalid(),
                productionDto.getTotalPrice(),
                productionDto.getContentPrice(),
                productionDto.getCost() * cost,
                productionDto.isCostEachOne(),
                productionDto.getDescription(),
                productionDto.getSelectedSmen()
        );
        if (productionDto.getProductId() != null) {
            Optional<Product> optional = productRepository.findById(productionDto.getProductId());
            if (optional.isEmpty()) return new ApiResponse("NOT FOUND PRODUCT", false);
            production.setProduct(optional.get());
        } else {
            Optional<ProductTypePrice> optional = productTypePriceRepository.findById(productionDto.getProductTypePriceId());
            if (optional.isEmpty()) return new ApiResponse("NOT FOUND PRODUCT TYPE PRICE", false);
            production.setProductTypePrice(optional.get());
        }
        productionRepository.save(production);

        ApiResponse apiResponseCost = saveCostList(production, productionDto.getCostDtoList());
        if (!apiResponseCost.isSuccess()) return apiResponseCost;

        List<ContentProduct> contentProductList = new ArrayList<>();
        double contentPrice = saveContentProductList(contentProductList, production, productionDto.getContentProductDtoList());
        contentPrice -= saveLossContentProductDtoList(contentProductList, production, productionDto.getLossContentProductDtoList());

        if (contentProductList.isEmpty()) return new ApiResponse("NOT FOUND CONTENT PRODUCTS", false);
        contentProductRepository.saveAll(contentProductList);
        production.setContentPrice(contentPrice);
        production.setTotalPrice(production.getCost() + contentPrice);

        if (production.getProduct() != null) {
            Product product = production.getProduct();
            product.setBuyPrice(production.getTotalPrice() / production.getQuantity());
            productRepository.save(product);
        } else {
            ProductTypePrice productTypePrice = production.getProductTypePrice();
            productTypePrice.setBuyPrice(production.getTotalPrice() / production.getQuantity());
            productTypePriceRepository.save(productTypePrice);
        }

        for (ContentProduct contentProduct : contentProductList) {
            if (contentProduct.isByProduct()) {
                double minusAmount = warehouseService.createOrEditWareHouseHelper(branch, contentProduct.getProduct(), contentProduct.getProductTypePrice(), contentProduct.getQuantity());
                fifoCalculationService.createByProduct(production, contentProduct, minusAmount);
            }
        }
        productionRepository.save(production);
        double minusAmount = warehouseService.createOrEditWareHouse(production);
        fifoCalculationService.createProduction(production, minusAmount);

//        HISTORY
        String name = production.getProduct() != null ? production.getProduct().getName() : production.getProductTypePrice().getName();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyRepository.save(new History(
                HistoryName.ISHLAB_CHIQARISH,
                user,
                branch,
                production.getQuantity() + " " + name + AppConstant.ADD_PRODUCTION
        ));


        return new ApiResponse("SUCCESS", true);
    }

    private double saveContentProductList(List<ContentProduct> contentProductList, Production production, List<ContentProductDto> contentProductDtoList) {
        double contentPrice = 0;
        for (ContentProductDto contentProductDto : contentProductDtoList) {
            if (contentProductDto.getQuantity() == 0) continue;
            ContentProduct contentProduct = new ContentProduct();
            contentProduct.setProduction(production);
            contentProduct.setQuantity(contentProductDto.getQuantity());
            contentProduct.setTotalPrice(contentProductDto.getTotalPrice());
            contentProduct.setByProduct(contentProductDto.isByProduct());
            contentProduct.setLossProduct(false);
            if (contentProductDto.isByProduct()) {
                if (contentProductDto.getProductId() != null) {
                    Optional<Product> optional = productRepository.findById(contentProductDto.getProductId());
                    if (optional.isEmpty()) continue;
                    contentProduct.setProduct(optional.get());
                } else {
                    Optional<ProductTypePrice> optional = productTypePriceRepository.findById(contentProductDto.getProductTypePriceId());
                    if (optional.isEmpty()) continue;
                    contentProduct.setProductTypePrice(optional.get());
                }
                contentPrice -= contentProduct.getTotalPrice();
            } else {
                contentProduct = warehouseService.createContentProduct(contentProduct, contentProductDto);
                if (contentProduct == null) continue;
                contentProduct = fifoCalculationService.createContentProduct(production.getBranch(), contentProduct);
                contentPrice += contentProduct.getTotalPrice();
            }
            contentProductList.add(contentProduct);
        }
        return contentPrice;
    }

    private ApiResponse saveCostList(Production production, List<CostDto> costDtoList) {
        List<Cost> list = new ArrayList<>();
        for (CostDto dto : costDtoList) {
            Optional<CostType> optionalCostType = costTypeRepository.findById(dto.getCostTypeId());
            if (optionalCostType.isEmpty())
                return new ApiResponse("NOT FOUND COST_TYPE", false);
            list.add(new Cost(
                    production,
                    optionalCostType.get(),
                    dto.getSum()
            ));
        }
        costRepository.saveAll(list);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse addContentForTask(Task task, TaskDto taskDto) {
        ApiResponse apiResponse = checkBeforeProduction(task.getBranch(), taskDto.getContentProductDtoList());
        if (!apiResponse.isSuccess()) return apiResponse;

        double cost = taskDto.isCostEachOne() ? taskDto.getGoalAmount() : 1;
        Production production = new Production(
                task.getBranch(),
                task.getStartDate(),
                task.getGoalAmount(),
                task.getGoalAmount(),
                0,
                0,
                0,
                taskDto.getCost() * cost,
                taskDto.isCostEachOne()
        );

        // commit

        production.setDone(false);
        if (task.getContent().getProduct() != null) {
            production.setProduct(task.getContent().getProduct());
        } else {
            production.setProductTypePrice(task.getContent().getProductTypePrice());
        }
        productionRepository.save(production);
        task.setProduction(production);

        List<ContentProduct> contentProductList = new ArrayList<>();
        double contentPrice = saveContentProductList(contentProductList, production, taskDto.getContentProductDtoList());

        if (contentProductList.isEmpty()) return new ApiResponse("NOT FOUND CONTENT PRODUCTS", false);
        ApiResponse apiResponseCost = saveCostList(production, taskDto.getCostDtoList());
        if (!apiResponseCost.isSuccess()) return apiResponseCost;
        contentProductRepository.saveAll(contentProductList);
        production.setContentPrice(contentPrice);
        production.setTotalPrice(task.getTaskPrice() + production.getCost() + contentPrice);
        productionRepository.save(production);

        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse addProductionForTask(ProductionTaskDto productionTaskDto) {
        Optional<Task> optionalTask = taskRepository.findById(productionTaskDto.getTaskId());
        if (optionalTask.isEmpty()) return new ApiResponse("TASK NOT FOUND", false);
        Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(productionTaskDto.getTaskStatusId());
        if (optionalTaskStatus.isEmpty()) {
            return new ApiResponse("NOT FOUND TASK STATUS", false);
        }
        Task task = optionalTask.get();
        TaskStatus taskStatus = optionalTaskStatus.get();
        if (task.getTaskStatus().getOrginalName() != null && task.getTaskStatus().getOrginalName().equals("Completed")) {
            return new ApiResponse("You can not change this task !", false);
        }
        if (task.getDependTask() != null) {
            Task depentTask = task.getDependTask();
            if (depentTask.getTaskStatus().getOrginalName() != null && !depentTask.getTaskStatus().getOrginalName().equals("Completed")) {
                return new ApiResponse("You can not change this task, Complete " + depentTask.getName() + " task", false);
            }
        }
        Branch branch = task.getBranch();
        if (task.getProduction() == null) return new ApiResponse("THIS TASK IS NOT PRODUCTION", false);
        Production production = task.getProduction();

        List<ContentProduct> contentProductList = contentProductRepository.findAllByProductionId(production.getId());
        double lossContentPrice = saveLossContentProductDtoList(contentProductList, production, productionTaskDto.getLossContentProductDtoList());

        if (contentProductList.isEmpty()) return new ApiResponse("NOT FOUND CONTENT PRODUCTS", false);
        contentProductRepository.saveAll(contentProductList);

        production.setQuantity(production.getTotalQuantity() - productionTaskDto.getInvalid());
        production.setInvalid(productionTaskDto.getInvalid());
        production.setTaskPrice(task.getTaskPrice());
        production.setContentPrice(production.getContentPrice() - lossContentPrice);
        production.setTotalPrice(production.getCost() + production.getContentPrice() + task.getTaskPrice());
        production.setDone(true);

        if (production.getProduct() != null) {
            Product product = production.getProduct();
            product.setBuyPrice(production.getTotalPrice() / production.getQuantity());
            productRepository.save(product);
        } else {
            ProductTypePrice productTypePrice = production.getProductTypePrice();
            productTypePrice.setBuyPrice(production.getTotalPrice() / production.getQuantity());
            productTypePriceRepository.save(productTypePrice);
        }

        for (ContentProduct contentProduct : contentProductList) {
            if (contentProduct.isByProduct()) {
                double minusAmount = warehouseService.createOrEditWareHouseHelper(branch, contentProduct.getProduct(), contentProduct.getProductTypePrice(), contentProduct.getQuantity());
                fifoCalculationService.createByProduct(production, contentProduct, minusAmount);
            }
        }
        productionRepository.save(production);
        //        HISTORY
        String name = production.getProduct() != null ? production.getProduct().getName() : production.getProductTypePrice().getName();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyRepository.save(new History(
                HistoryName.ISHLAB_CHIQARISH,
                user,
                branch,
                production.getQuantity() + " " + name + AppConstant.ADD_PRODUCTION
        ));
        double minusAmount;
        try {
            minusAmount = warehouseService.createOrEditWareHouse(production);
        } catch (Exception e) {
            return new ApiResponse("WAREHOUSE ERROR", false);
        }

        try {
            fifoCalculationService.createProduction(production, minusAmount);
        } catch (Exception e) {
            return new ApiResponse("FIFO ERROR", false);
        }

        task.setTaskStatus(taskStatus);
        task.setProduction(production);
        taskRepository.save(task);

        try {
            salaryCountService.addForTask(task);
        } catch (Exception e) {
            return new ApiResponse("SALARY ERROR", false);
        }

        try {
            prizeService.addForTask(task);
        } catch (Exception e) {
            return new ApiResponse("PRIZE ERROR", false);
        }

        return new ApiResponse("SUCCESS", true);
    }

    private ApiResponse checkBeforeProduction(Branch branch, List<ContentProductDto> contentProductDtoList) {
        if (contentProductDtoList.isEmpty()) return new ApiResponse("NOT FOUND PRODUCT_LIST", false);
//        if (!branch.getBusiness().isSaleMinus()) {
        HashMap<UUID, Double> map = new HashMap<>();
        for (ContentProductDto dto : contentProductDtoList) {
            if (dto.getProductId() != null) {
                UUID productId = dto.getProductId();
                if (!productRepository.existsById(productId)) return new ApiResponse("PRODUCT NOT FOUND", false);
                if (!dto.isByProduct())
                    map.put(productId, map.getOrDefault(productId, 0d) + dto.getQuantity());
            } else if (dto.getProductTypePriceId() != null) {
                UUID productId = dto.getProductTypePriceId();
                if (!productTypePriceRepository.existsById(productId))
                    return new ApiResponse("PRODUCT NOT FOUND", false);
                if (!dto.isByProduct())
                    map.put(productId, map.getOrDefault(productId, 0d) + dto.getQuantity());
            } else {
                return new ApiResponse("PRODUCT NOT FOUND", false);
            }
        }
        if (!warehouseService.checkBeforeTrade(branch, map)) return new ApiResponse("NOT ENOUGH PRODUCT", false);
//        }
        return new ApiResponse("SUCCESS", true);
    }

    private double saveLossContentProductDtoList(List<ContentProduct> contentProductList, Production production, List<ContentProductDto> lossContentProductDtoList) {
        double lossContentPrice = 0;
        for (ContentProductDto contentProductDto : lossContentProductDtoList) {
            if (contentProductDto.getQuantity() == 0) continue;
            ContentProduct contentProduct = new ContentProduct();
            contentProduct.setProduction(production);
            contentProduct.setQuantity(contentProductDto.getQuantity());
            contentProduct.setTotalPrice(contentProductDto.getTotalPrice());
            contentProduct.setByProduct(false);
            contentProduct.setLossProduct(true);
            double minusAmount = warehouseService.createByProduct(contentProduct, contentProductDto);
            fifoCalculationService.createByProduct(production, contentProduct, minusAmount);
            lossContentPrice += contentProduct.getTotalPrice();
            contentProductList.add(contentProduct);
        }
        return lossContentPrice;
    }

    public ApiResponse getAll(UUID branchId, int page, int size, String name) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) return new ApiResponse("NOT FOUND BRANCH", false);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Production> all;

        if (name != null && !name.isBlank()) {
            all = productionRepository.findAllByBranchIdAndDoneIsTrueAndProduct_NameContainingIgnoreCaseOrBranchIdAndDoneIsTrueAndProductTypePrice_NameContainingIgnoreCase(branchId, name, branchId, name, pageable);
        } else {
            all = productionRepository.findAllByBranchIdAndDoneIsTrue(branchId, pageable);
        }

        if (all.isEmpty()) return new ApiResponse("NOT FOUND", false);

        Map<String, Object> response = new HashMap<>();
        response.put("getAllProduction", all.toList());
        response.put("currentPage", all.getNumber());
        response.put("totalItems", all.getTotalElements());
        response.put("totalPages", all.getTotalPages());

        return new ApiResponse(true, response);
    }

    public ApiResponse getOne(UUID productionId) {
        Optional<Production> optionalProduction = productionRepository.findById(productionId);
        if (optionalProduction.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Production production = optionalProduction.get();
        List<ContentProduct> contentProductList = contentProductRepository.findAllByProductionId(productionId);
        if (contentProductList.isEmpty()) return new ApiResponse("NOT FOUND CONTENT PRODUCTS", false);
        GetOneContentProductionDto getOneContentProductionDto = new GetOneContentProductionDto(
                production,
                costMapper.toDtoList(costRepository.findAllByProductionId(productionId)),
                contentProductList
        );
        return new ApiResponse(true, getOneContentProductionDto);
    }

    public void editInvalid(Production production, TaskStatus taskStatus, double quantity) {
        production.setInvalid(production.getInvalid() + quantity);
        production.setQuantity(production.getTotalQuantity() - production.getInvalid());
        productionRepository.save(production);
        Optional<LostProduction> optionalLostProduction = lostProductionRepository.findByTaskStatusId(taskStatus.getId());
        if (optionalLostProduction.isPresent()) {
            LostProduction lostProduction = optionalLostProduction.get();
            lostProduction.setQuantity(lostProduction.getQuantity() + quantity);
            lostProductionRepository.save(lostProduction);
            return;
        }
        createLostProduction(taskStatus, quantity);
        editInvalid(production, taskStatus, quantity);
    }

    private void createLostProduction(TaskStatus taskStatus, double quantity) {
        lostProductionRepository.save(new LostProduction(
                taskStatus,
                quantity
        ));
    }

    public ApiResponse getLossProduction(UUID branchId) {
        List<LostProduction> lostProductionList = lostProductionRepository.findByTaskStatus_BranchId(branchId);
        if (lostProductionList.isEmpty())
            return new ApiResponse("NOT FOUND LOST PRODUCTION LIST", true);
        return new ApiResponse("SUCCESS", true, lostProductionToDtoList(lostProductionList));
    }

    private List<LostProductionDto> lostProductionToDtoList(List<LostProduction> lostProductionList) {
        List<LostProductionDto> lostProductionDtoList = new ArrayList<>();
        for (LostProduction lostProduction : lostProductionList) {
            lostProductionDtoList.add(new LostProductionDto(
                    lostProduction.getId(),
                    lostProduction.getTaskStatus().getName(),
                    lostProduction.getQuantity()
            ));
        }
        return lostProductionDtoList;
    }
}
