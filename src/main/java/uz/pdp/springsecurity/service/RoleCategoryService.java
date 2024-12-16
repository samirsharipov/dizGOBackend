package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.RoleCategory;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.RoleCategoryDto;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.RoleCategoryRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleCategoryService {

    private final RoleCategoryRepository roleCategoryRepository;
    private final BusinessRepository businessRepository;

    public ApiResponse create(RoleCategoryDto roleCategoryDto) {

        if (roleCategoryDto.getBusinessId() == null) {
            return new ApiResponse("business id is not null", false);
        }

        RoleCategory roleCategory = new RoleCategory();
        setRoleCategory(roleCategoryDto, roleCategory);
        return new ApiResponse("successfully created role category", true);
    }


    public ApiResponse edit(UUID id, RoleCategoryDto roleCategoryDto) {
        Optional<RoleCategory> optionalRoleCategory = roleCategoryRepository.findById(id);
        if (optionalRoleCategory.isEmpty())
            return new ApiResponse("not found", false);

        RoleCategory roleCategory = optionalRoleCategory.get();
        setRoleCategory(roleCategoryDto, roleCategory);

        return new ApiResponse("successfully edited role category", true);
    }

    public ApiResponse getById(UUID id) {

        Optional<RoleCategory> optionalRoleCategory = roleCategoryRepository.findById(id);
        return optionalRoleCategory
                .map(roleCategory ->
                        new ApiResponse("successfully getById",
                                true, toDto(roleCategory)))
                .orElseGet(() -> new ApiResponse("not found", false));
    }


    public ApiResponse getRoleCategoriesByBusiness(UUID businessId) {
        // Businessga tegishli barcha RoleCategory-larni olish
        List<RoleCategory> allCategories = roleCategoryRepository.findAllByBusinessId(businessId);

        // Root (parentRoleCategory = null) bo'lgan bo'limlarni topish
        List<RoleCategory> rootCategories = allCategories.stream()
                .filter(roleCategory -> roleCategory.getParentRoleCategory() == null)
                .toList();

        // Iyerarxiyani yaratish
        List<Map<String, Object>> hierarchy = rootCategories.stream()
                .map(rootCategory -> buildHierarchy(rootCategory, allCategories))
                .collect(Collectors.toList());

        return new ApiResponse("Successfully fetched role categories", true, hierarchy);
    }

    /**
     * Iyerarxiya uchun rekursiv metod
     */
    private Map<String, Object> buildHierarchy(RoleCategory roleCategory, List<RoleCategory> allCategories) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", roleCategory.getId());
        data.put("name", roleCategory.getName());
        data.put("description", roleCategory.getDescription());

        // Farzand bo'limlarni topish (child categories)
        List<Map<String, Object>> children = allCategories.stream()
                .filter(child -> child.getParentRoleCategory() != null && child.getParentRoleCategory().getId().equals(roleCategory.getId()))
                .map(child -> buildHierarchy(child, allCategories)) // Rekursiv chaqirish
                .collect(Collectors.toList());

        data.put("children", children);
        return data;
    }
     public RoleCategoryDto toDto(RoleCategory roleCategory) {
        RoleCategoryDto roleCategoryDto = new RoleCategoryDto();
        roleCategoryDto.setId(roleCategory.getId());
        roleCategoryDto.setName(roleCategory.getName());
        roleCategoryDto.setDescription(roleCategory.getDescription());
        roleCategoryDto.setBusinessId(roleCategory.getBusiness().getId());
        return roleCategoryDto;
    }

    public List<RoleCategoryDto> toDto(List<RoleCategory> roleCategories) {
        return roleCategories.stream()
                .map(this::toDto)
                .toList();
    }

    private void setRoleCategory(RoleCategoryDto roleCategoryDto, RoleCategory roleCategory) {
        if (roleCategoryDto.getParentRoleCategoryId() != null) {
            Optional<RoleCategory> optionalRoleCategory = roleCategoryRepository.findById(roleCategoryDto.getParentRoleCategoryId());
            optionalRoleCategory.ifPresent(roleCategory::setParentRoleCategory);
        }
        if (roleCategoryDto.getBusinessId() != null) {
            Optional<Business> optionalRoleCategory = businessRepository.findById(roleCategoryDto.getBusinessId());
            optionalRoleCategory.ifPresent(roleCategory :: setBusiness);
        }

        roleCategory.setName(roleCategoryDto.getName());
        roleCategory.setDescription(roleCategoryDto.getDescription());
        roleCategoryRepository.save(roleCategory);
    }
}