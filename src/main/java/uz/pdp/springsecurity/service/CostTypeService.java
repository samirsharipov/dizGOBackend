package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.CostType;
import uz.pdp.springsecurity.mapper.CostTypeMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CostTypeDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.CostTypeRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CostTypeService {

    private final CostTypeRepository costTypeRepository;
    private final CostTypeMapper costTypeMapper;

    private final BranchRepository branchRepository;

    public ApiResponse create(CostTypeDto costTypeDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(costTypeDto.getBranchId());
        if (optionalBranch.isEmpty()){
            return new ApiResponse("NOT FOUND BRANCH",false);
        }
        costTypeRepository.save(new CostType(
                costTypeDto.getName(),
                optionalBranch.get()
        ));
        return new ApiResponse("SUCCESS",true);
    }

    public ApiResponse edit(UUID costTypeId, CostTypeDto costTypeDto) {
        Optional<CostType> optionalCostType = costTypeRepository.findById(costTypeId);
        if (optionalCostType.isEmpty()){
            return new ApiResponse("NOT FOUND COST_TYPE",false);
        }
        CostType costType = optionalCostType.get();
        costType.setName(costTypeDto.getName());
        costTypeRepository.save(costType);
        return new ApiResponse("SUCCESS",true);
    }

    public ApiResponse delete(UUID costTypeId) {
        Optional<CostType> optionalCostType = costTypeRepository.findById(costTypeId);
        if (optionalCostType.isEmpty()){
            return new ApiResponse("NOT FOUND COST_TYPE",false);
        }
        CostType costType = optionalCostType.get();
        costType.setDelete(true);
        costTypeRepository.save(costType);
        return new ApiResponse("SUCCESS",true);
    }

    public ApiResponse getAllByBranch(UUID branchId) {
        List<CostType> list = costTypeRepository.findAllByBranchIdAndDeleteFalseOrderByCreatedAtDesc(branchId);
        if (list.isEmpty()){
            return new ApiResponse("NOT FOUND COST_TYPE",false);
        }
        return new ApiResponse("Found",true, costTypeMapper.toDtoList(list));
    }

    public ApiResponse getAllBranchPageable(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<CostType> costTypePage = costTypeRepository.findAllByBranch_idAndDeleteFalseOrderByCreatedAtDesc(branchId, pageable);
        if (costTypePage.isEmpty()){
            return new ApiResponse("NOT FOUND COST_TYPE",false);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("getLessProduct", costTypeMapper.toDtoList(costTypePage.getContent()));
        response.put("currentPage", costTypePage.getNumber());
        response.put("totalItem", costTypePage.getTotalElements());
        response.put("totalPage", costTypePage.getTotalPages());
        return new ApiResponse("Found",true, response);
    }
}
