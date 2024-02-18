package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.collection.internal.PersistentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.entity.Tariff;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.exeptions.RescuersNotFoundEx;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.RoleRepository;
import uz.pdp.springsecurity.repository.TariffRepository;
import uz.pdp.springsecurity.repository.UserRepository;
import uz.pdp.springsecurity.util.Constants;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;

    public ApiResponse add(RoleDto roleDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(roleDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);

        boolean exists = roleRepository.existsByNameIgnoreCaseAndBusinessId(roleDto.getName(), roleDto.getBusinessId());
        if (exists || roleDto.getName().equalsIgnoreCase(Constants.SUPERADMIN) || roleDto.getName().equalsIgnoreCase(Constants.ADMIN))
            return new ApiResponse("ROLE ALREADY EXISTS", false);
        Role role = new Role();
        role.setName(roleDto.getName());
        role.setOffice(roleDto.getIsOffice());
        role.setPermissions(roleDto.getPermissions());
        role.setDescription(roleDto.getDescription());
        role.setBusiness(optionalBusiness.get());

        if (roleDto.getParentRole() != null) {
            Optional<Role> optionalRole = roleRepository.findById(roleDto.getParentRole());
            optionalRole.ifPresent(role::setParentRole);
        }


        roleRepository.save(role);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, RoleDto roleDto) {

        Optional<Business> optionalBusiness = businessRepository.findById(roleDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);

        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty()) return new ApiResponse("ROLE NOT FOUND", false);

        boolean exist = roleRepository.existsByNameIgnoreCaseAndBusinessIdAndIdIsNot(roleDto.getName(), roleDto.getBusinessId(), id);
        if (exist || roleDto.getName().equalsIgnoreCase(Constants.SUPERADMIN) || roleDto.getName().equalsIgnoreCase(Constants.ADMIN))
            return new ApiResponse("ROLE ALREADY EXISTS", false);

        List<Role> allByBusinessId = roleRepository.findAllByBusinessId(roleDto.getBusinessId());
        System.out.println(allByBusinessId);
        Role role = optionalRole.get();
        role.setName(roleDto.getName());
        role.setOffice(roleDto.getIsOffice());
        role.setPermissions(roleDto.getPermissions());
        role.setDescription(roleDto.getDescription());
        role.setBusiness(optionalBusiness.get());

        if (roleDto.getParentRole() != null) {
            Optional<Role> optionalParent = roleRepository.findById(roleDto.getParentRole());
            optionalParent.ifPresent(role::setParentRole);
        }

        roleRepository.save(role);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(@NotNull UUID id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        return optionalRole.map(role -> new ApiResponse("FOUND", true, role)).orElseThrow(() -> new RescuersNotFoundEx("Role", "id", id));
    }


    public ApiResponse getAllByBusiness(UUID business_id) {
        List<Role> allByBusiness_id = roleRepository.findAllByBusiness_IdAndNameIsNot(business_id, Constants.SUPERADMIN);

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
        List<Role> allByBusiness_id = roleRepository.findAllByBusiness_IdAndNameIsNot(businessId, Constants.SUPERADMIN);
        List<RoleGetMetDto> roleGetMetDtoList = new ArrayList<>();

        for (Role role : allByBusiness_id) {
            List<User> allByRoleId = userRepository.findAllByRole_Id(role.getId());

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
            roleGetMetDtoList.add(roleGetMetDto);
        }

        if (roleGetMetDtoList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, roleGetMetDtoList);
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
}
