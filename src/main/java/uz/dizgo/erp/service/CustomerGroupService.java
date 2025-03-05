package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.CustomerGroup;
import uz.dizgo.erp.mapper.CustomerGroupMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CustomerGroupDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.CustomerGroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerGroupService {
    private final CustomerGroupRepository customerGroupRepository;

    private final BusinessRepository businessRepository;

    private final CustomerGroupMapper mapper;

    public ApiResponse addCustomerGroup(CustomerGroupDto customerGroupDto) {

        Optional<Business> optionalBusiness = businessRepository.findById(customerGroupDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("BUSINESS NOT FOUND", false);
        }

        if (customerGroupDto.getName().equals("defaultCustomerGroup")) {
            return new ApiResponse("CUSTOMER GROUP NAME ALREADY EXIST", false);
        }

        CustomerGroup customerGroup = mapper.toEntity(customerGroupDto);
        customerGroupRepository.save(customerGroup);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse getAll(UUID businessId) {
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAllByBusiness_Id(businessId);
        if (customerGroupList.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("ALL_CUSTOMERS", true, mapper.toDtoList(customerGroupList));
    }

    public ApiResponse delete(UUID id) {
        if (!customerGroupRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        customerGroupRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getById(UUID id) {
        Optional<CustomerGroup> optional = customerGroupRepository.findById(id);

        if (optional.isEmpty()) {
            return new ApiResponse("NOT_FOUND", false);
        }

        CustomerGroup customerGroup = optional.get();
        return new ApiResponse("FOUND", true, mapper.toDto(customerGroup));
    }


    public ApiResponse edit(UUID id, CustomerGroupDto customerGroupDto) {
        if (!customerGroupRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        Optional<Business> optionalBusiness = businessRepository.findById(customerGroupDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("BRANCH NOT FOUND", false);
        }

        CustomerGroup customerGroup = customerGroupRepository.getById(id);
        customerGroup.setName(customerGroupDto.getName());
        customerGroup.setPercent(customerGroupDto.getPercent());

        customerGroupRepository.save(customerGroup);
        return new ApiResponse("EDITED", true);
    }
}
