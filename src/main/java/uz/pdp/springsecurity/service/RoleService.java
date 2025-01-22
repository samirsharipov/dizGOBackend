package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.exeptions.RescuersNotFoundEx;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.Constants;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final RoleCategoryRepository roleCategoryRepository;

    public ApiResponse add(RoleDto roleDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(roleDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);

        boolean exists = roleRepository.existsByNameIgnoreCaseAndBusinessId(roleDto.getName(), roleDto.getBusinessId());
        if (exists || roleDto.getName().equalsIgnoreCase(Constants.SUPER_ADMIN) || roleDto.getName().equalsIgnoreCase(Constants.ADMIN))
            return new ApiResponse("ROLE ALREADY EXISTS", false);

        Role role = new Role();
        setRole(roleDto, optionalBusiness.get(), role);

        roleRepository.save(role);
        return new ApiResponse("ADDED", true);
    }


    public ApiResponse edit(UUID id, RoleDto roleDto) {

        Optional<Business> optionalBusiness = businessRepository.findById(roleDto.getBusinessId());
        if (optionalBusiness.isEmpty())
            return new ApiResponse("BUSINESS NOT FOUND", false);

        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty())
            return new ApiResponse("ROLE NOT FOUND", false);

        boolean exist = roleRepository.existsByNameIgnoreCaseAndBusinessIdAndIdIsNot(roleDto.getName(), roleDto.getBusinessId(), id);

        if (exist || roleDto.getName().equalsIgnoreCase(Constants.SUPER_ADMIN) || roleDto.getName().equalsIgnoreCase(Constants.ADMIN))
            return new ApiResponse("ROLE ALREADY EXISTS", false);

        Role role = optionalRole.get();
        setRole(roleDto, optionalBusiness.get(), role);

        roleRepository.save(role);
        return new ApiResponse("EDITED", true);
    }


    public ApiResponse get(@NotNull UUID id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        return optionalRole.map(role -> new ApiResponse("FOUND", true, role)).orElseThrow(() -> new RescuersNotFoundEx("Role", "id", id));
    }


    public ApiResponse getAllByBusiness(UUID business_id) {
        List<Role> allByBusiness_id = roleRepository.findAllByBusiness_IdAndNameIsNot(business_id, Constants.SUPER_ADMIN);

        List<RoleDto> roleDtoList = new ArrayList<>();
        for (Role role : allByBusiness_id) {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(role.getId());
            roleDto.setName(role.getName());
            roleDto.setIsOffice(role.isOffice());
            roleDto.setDescription(role.getDescription());
            roleDto.setPermissions(role.getPermissions());
            List<UserDataByRoleDto> usersDataList = new LinkedList<>();
            for (User user : userRepository.findAllByRole_Id(role.getId())) {
                if (user.getPhoto() != null && user.getPhoto().getId() != null) {
                    UserDataByRoleDto userData = new UserDataByRoleDto();
                    userData.setUsersPhoto(user.getPhoto().getId());
                    usersDataList.add(userData);
                }
            }
            roleDto.setUsers(usersDataList);
            roleDto.setBusinessId(role.getBusiness().getId());
            if (role.getParentRole() != null) {
                roleDto.setParentRole(role.getParentRole().getId());
            }
            roleDtoList.add(roleDto);
        }

        if (roleDtoList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, roleDtoList);
    }


    public ApiResponse getByBusinessRole(UUID businessId) {
        List<Role> allByBusiness_id = roleRepository.findAllByBusiness_IdAndNameIsNot(businessId, Constants.SUPER_ADMIN);
        List<RoleGetMetDto> roleGetMetDtoList = new ArrayList<>();

        for (Role role : allByBusiness_id) {
            List<User> allByRoleId = userRepository.findAllByRole_Id(role.getId());

            RoleGetMetDto roleGetMetDto = getRoleGetMetDto(role, allByRoleId);
            roleGetMetDtoList.add(roleGetMetDto);
        }

        if (roleGetMetDtoList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, roleGetMetDtoList);
    }

    @org.jetbrains.annotations.NotNull
    private static RoleGetMetDto getRoleGetMetDto(Role role, List<User> allByRoleId) {
        RoleGetMetDto roleGetMetDto = new RoleGetMetDto();
        roleGetMetDto.setRoleName(role.getName());

        List<UserGetMetDto> userGetMetDtoList = new ArrayList<>();

        for (User user : allByRoleId) {
            UserGetMetDto userGetMetDto = new UserGetMetDto();
            userGetMetDto.setId(user.getId());
            userGetMetDto.setFio(user.getFirstName() + " " + user.getLastName());
            if (user.getPhoto() != null) {
                userGetMetDto.setAttachmentId(user.getPhoto().getId());
            }
            userGetMetDtoList.add(userGetMetDto);
        }
        roleGetMetDto.setList(userGetMetDtoList);
        return roleGetMetDto;
    }

    public ApiResponse getRolePermissions(UUID businessId) {
        Optional<Role> optional = roleRepository.findByNameAndBusinessId("Admin", businessId);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        Role role = optional.get();
        return new ApiResponse("found", true, role.getPermissions());
    }

    public ApiResponse getRoleByTariff(UUID tariffId) {
        Optional<Tariff> optionalTariff = tariffRepository.findById(tariffId);
        if (optionalTariff.isEmpty()) {
            return new ApiResponse("not found tariff", false);
        }
        Tariff tariff = optionalTariff.get();
        return new ApiResponse("found", true, tariff.getPermissions());
    }

    public ApiResponse getRoleByRoleCategory(UUID roleCategoryId) {
        List<Role> all =
                roleRepository.findAllByRoleCategoryId(roleCategoryId);

        if (all.isEmpty())
            return new ApiResponse("not found role by role category", false);

        return new ApiResponse("found", true, toDto(all));
    }

    private void setRole(RoleDto roleDto, Business business, Role role) {
        role.setName(roleDto.getName());
        role.setPermissions(roleDto.getPermissions());
        role.setDescription(roleDto.getDescription());
        role.setBusiness(business);

        if (roleDto.getRoleCategoryId() != null) {
            Optional<RoleCategory> categoryOptional = roleCategoryRepository.findById(roleDto.getRoleCategoryId());
            categoryOptional.ifPresent(role::setRoleCategory);
        }

        if (roleDto.getIsOffice() != null) {
            role.setOffice(roleDto.getIsOffice());
        }

        if (roleDto.getParentRole() != null) {
            Optional<Role> optionalRole = roleRepository.findById(roleDto.getParentRole());
            optionalRole.ifPresent(role::setParentRole);
        }
    }

    public RoleGetDto toDto(Role role) {
        RoleGetDto roleGetDto = new RoleGetDto();
        roleGetDto.setId(role.getId());
        roleGetDto.setName(role.getName());
        roleGetDto.setDescription(role.getDescription());
        roleGetDto.setBusinessId(role.getBusiness().getId());
        roleGetDto.setParentRoleId(role.getParentRole().getId());
        roleGetDto.setRoleCategoryId(role.getRoleCategory().getId());
        roleGetDto.setIsOffice(role.isOffice());
        return roleGetDto;
    }

    public List<RoleGetDto> toDto(List<Role> roles) {
        return roles.stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public Role getUserRole(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Hibernate.initialize(user.getRole()); // Role obyekti lazy bo‘lgani uchun yuklanadi
        return user.getRole();
    }
}
