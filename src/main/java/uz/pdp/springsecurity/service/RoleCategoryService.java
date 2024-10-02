package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.RoleCategory;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.RoleCategoryDto;
import uz.pdp.springsecurity.repository.RoleCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleCategoryService {

    private final RoleCategoryRepository roleCategoryRepository;

    public ApiResponse create(RoleCategoryDto roleCategoryDto) {

        if (roleCategoryDto.getBusinessId() == null) {
            return new ApiResponse("business id is not null", false);
        }
        RoleCategory roleCategory = new RoleCategory();
        roleCategory.setName(roleCategoryDto.getName());
        roleCategory.setDescription(roleCategoryDto.getDescription());
        roleCategoryRepository.save(roleCategory);

        return new ApiResponse("successfully created role category", true);
    }

    public ApiResponse edit(UUID id, RoleCategoryDto roleCategoryDto) {

        Optional<RoleCategory> optionalRoleCategory = roleCategoryRepository.findById(id);
        if (optionalRoleCategory.isEmpty())
            return new ApiResponse("not found", false);

        RoleCategory roleCategory = optionalRoleCategory.get();
        roleCategory.setName(roleCategoryDto.getName());
        roleCategory.setDescription(roleCategoryDto.getDescription());
        roleCategoryRepository.save(roleCategory);

        return new ApiResponse("successfully edited role category", true);
    }

    public ApiResponse getByBusinessId(UUID businessId) {
        List<RoleCategory> all =
                roleCategoryRepository.findAllByBusinessId(businessId);

        if (all.isEmpty())
            return new ApiResponse("not found", false);

        return new ApiResponse("successfully getByBusinessId", true, toDto(all));
    }

    public ApiResponse getById(UUID id) {

        Optional<RoleCategory> optionalRoleCategory = roleCategoryRepository.findById(id);
        return optionalRoleCategory
                .map(roleCategory ->
                        new ApiResponse("successfully getById",
                                true, toDto(roleCategory)))
                .orElseGet(() -> new ApiResponse("not found", false));
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
}
